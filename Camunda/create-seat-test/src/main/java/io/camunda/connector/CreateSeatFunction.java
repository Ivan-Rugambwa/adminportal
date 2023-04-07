package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.viewmodels.CreateSeatBaseRequest;
import io.camunda.connector.viewmodels.CreateSeatBaseResponse;
import io.camunda.connector.viewmodels.LoginRequest;
import io.camunda.connector.viewmodels.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@OutboundConnector(
    name = "Create seat base",
    inputVariables = {"email", "password", "accountUuid", "businessUuid", "today"},
    type = "apendo:create:1")
public class CreateSeatFunction implements OutboundConnectorFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(CreateSeatFunction.class);

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var connectorRequest = context.getVariablesAsType(CreateSeatRequest.class);

    context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);

    return executeConnector(connectorRequest);
  }

  private CreateSeatResult executeConnector(final CreateSeatRequest connectorRequest) throws IOException, InterruptedException, ExecutionException {
    // TODO: implement connector logic
    LOGGER.info("Executing my connector with request {}", connectorRequest);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://wsprakt3.apendo.se:9000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    AccountClient client = retrofit.create(AccountClient.class);

    var loginResponse = login(client, connectorRequest);

    var seatBase = CreateSeatBase(client, loginResponse.getAccessToken(), connectorRequest);

    var getDataResult = new CreateSeatResult();
    getDataResult.setSeatBase(seatBase);
    getDataResult.setMessage("Created seat base");

    return getDataResult;
  }

  public static CreateSeatBaseResponse CreateSeatBase(AccountClient client, String accessToken, CreateSeatRequest request) throws ExecutionException, InterruptedException {

    var splitDates = request.getToday().split("-");
    var forYearMonth = (splitDates[0] + "/" + splitDates[1]);

    var createSeatBaseRequest = new CreateSeatBaseRequest();
    createSeatBaseRequest.setAccountUuid(request.getAccountUuid());
    createSeatBaseRequest.setBusinessUuid(request.getBusinessUuid());
    createSeatBaseRequest.setForYearMonth(forYearMonth);

    return client.postSeatBase("Bearer " + accessToken, createSeatBaseRequest).get();
  }

  public static LoginResponse login(AccountClient client, CreateSeatRequest connectorRequest) throws ExecutionException, InterruptedException {

    LoginRequest loginRequest = LoginRequest.builder()
            .email(connectorRequest.getEmail())
            .password(connectorRequest.getPassword())
            .build();

    return client.login(loginRequest).get();
  }
}

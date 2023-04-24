package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.viewmodels.LoginRequest;
import io.camunda.connector.viewmodels.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

@OutboundConnector(
    name = "Save seat data.",
    inputVariables = {"email", "password", "seatUuid", "apiUrl"},
    type = "apendo:save:1")
public class SaveDataFunction implements OutboundConnectorFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(SaveDataFunction.class);

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var connectorRequest = context.getVariablesAsType(SaveDataRequest.class);

    context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);

    return executeConnector(connectorRequest);
  }

  private SaveDataResult executeConnector(final SaveDataRequest connectorRequest) throws InterruptedException, ExecutionException {
    // TODO: implement connector logic
    LOGGER.info("Executing my connector with request {}", connectorRequest);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(connectorRequest.getApiUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    SeatReportClient client = retrofit.create(SeatReportClient.class);

    var loginResponse = login(client, connectorRequest);

    updateSeatReport(client, loginResponse.getAccessToken(), connectorRequest);

    var getDataResult = new SaveDataResult();
    getDataResult.setMessage("Seat report has been saved");
    System.out.println("Seat report saved.");
    return getDataResult;
  }
  public static LoginResponse login(SeatReportClient client, SaveDataRequest connectorRequest) throws ExecutionException, InterruptedException {

    LoginRequest loginRequest = LoginRequest.builder()
            .email(connectorRequest.getEmail())
            .password(connectorRequest.getPassword())
            .build();

    return client.login(loginRequest).get();
  }

  public static void updateSeatReport(SeatReportClient client, String token, SaveDataRequest request){
    var seatReport = new SeatReport();
    seatReport.setStatus("COMPLETE");
    try {
      client.updateSeatReport("Bearer " + token, request.getSeatUuid(), seatReport).get();
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getLocalizedMessage());
    }
  }
}

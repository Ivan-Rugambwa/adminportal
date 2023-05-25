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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@OutboundConnector(
    name = "Get user data.",
    inputVariables = {"email", "password", "businessName", "apiUrl"},
    type = "apendo:get-users-by-business:1")
public class GetDataFunction implements OutboundConnectorFunction {

  private static final Logger LOGGER = LoggerFactory.getLogger(GetDataFunction.class);

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var connectorRequest = context.getVariablesAsType(GetDataRequest.class);

    context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);

    return executeConnector(connectorRequest);
  }

  private GetDataResult executeConnector(final GetDataRequest connectorRequest) throws InterruptedException, ExecutionException {

    System.out.println("Getting users from business: " + connectorRequest.getBusinessName());

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(connectorRequest.getApiUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    AccountClient client = retrofit.create(AccountClient.class);

    System.out.println("Logging in...");
    var loginResponse = login(client, connectorRequest);
    System.out.println("Successfully logged in");

    System.out.println("Getting users...");
    var userAccounts = getUserAccounts(client, loginResponse.getAccessToken(), connectorRequest);

    var getDataResult = new GetDataResult();
    getDataResult.setUsers(userAccounts);
    System.out.println("Successfully got users");
    return getDataResult;
  }
  public static LoginResponse login(AccountClient client, GetDataRequest connectorRequest) throws ExecutionException, InterruptedException {

    LoginRequest loginRequest = LoginRequest.builder()
            .email(connectorRequest.getEmail())
            .password(connectorRequest.getPassword())
            .build();

    return client.login(loginRequest).get();
  }

  public static List<Account> getUserAccounts(AccountClient client, String token, GetDataRequest request){
    List<Account> accounts;
    try {
      accounts = client.getAccounts("Bearer " + token, request.getBusinessName()).get();
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getLocalizedMessage());
    }
    return accounts;
  }
}

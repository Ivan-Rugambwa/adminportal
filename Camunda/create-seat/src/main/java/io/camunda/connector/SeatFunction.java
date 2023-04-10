package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.retrofit.SeatService;
import io.camunda.connector.retrofit.business.BusinessResponse;
import io.camunda.connector.retrofit.login.LoginRequest;
import io.camunda.connector.retrofit.login.LoginResponse;
import io.camunda.connector.retrofit.seat.CreateSeatRequest;
import io.camunda.connector.retrofit.seat.CreateSeatResponse;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@OutboundConnector(
    name = "Create seat",
    inputVariables = {"email", "password", "today"},
    type = "apendo:create-seat:1")
public class SeatFunction implements OutboundConnectorFunction {

  @Override
  public Object execute(OutboundConnectorContext context) throws Exception {
    var connectorRequest = context.getVariablesAsType(SeatRequest.class);

    context.validate(connectorRequest);
    context.replaceSecrets(connectorRequest);

    return executeConnector(connectorRequest);
  }

  private SeatResult executeConnector(final SeatRequest request) {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://83.233.216.66:35462/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build();

    SeatService service = retrofit.create(SeatService.class);

    var loginRequest = LoginRequest.builder().email(request.getEmail()).password(request.getPassword()).build();

    var call = service.LOGIN_CALL(loginRequest);
    LoginResponse loginResponse;
    try {
      loginResponse = call.execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }

    var businessCall = service.GET_BUSINESSES_CALL("Bearer " + loginResponse.getAccessToken());

    Set<BusinessResponse> businesses;

    try {
      businesses = businessCall.execute().body();
    } catch (IOException e) {
      throw new RuntimeException(e.getLocalizedMessage());
    }
    var splitDate = request.getToday().split("-");
    var forYearMonth = splitDate[0] + "/" + splitDate[1];
    Set<CreateSeatResponse> seatResponses = new HashSet<>();
    SeatResult seatResult = null;
    for (var business :
            businesses) {
      var seatRequest = CreateSeatRequest.builder().businessUuid(business.getUuid().toString()).forYearMonth(forYearMonth).build();
      var seatCall = service.CREATE_SEAT_RESPONSE_CALL("Bearer " + loginResponse.getAccessToken(), seatRequest);

      try {
        var response = seatCall.execute();
        if (response.isSuccessful()){
        seatResponses.add(response.body());
        }else throw new IllegalArgumentException(response.code() + ": Failed to create seat: " + response.message());
      } catch (IOException e) {
        throw new RuntimeException(e.getLocalizedMessage());
      }

      seatResult = new SeatResult();
      seatResult.setSeatResponses(seatResponses);
    }
      seatResponses.forEach(System.out::println);
    if (seatResult == null) throw new RuntimeException("Failed to create seats");
    return seatResult;
  }
}

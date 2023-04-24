package io.camunda.connector;

import static io.camunda.connector.SaveDataFunction.updateSeatReport;
import static io.camunda.connector.SaveDataFunction.login;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ExecutionException;

public class MyRequestTest {

//  @Test
//  void shouldReplaceTokenSecretWhenReplaceSecrets() {
//    // given
//    var input = new SaveDataRequest();
//    input.setToken("Hello World!");
//    input.setPassword("secrets.PASSWORD");
//    input.setEmail("secrets.EMAIL");
//    input.setApiUrl("secrets.API_URL");
//    input.setSeatUuid("32210e94-96ec-4c64-80a0-4bf4286e4e1a");
//
//    var context = OutboundConnectorContextBuilder
//            .create()
//            .secret("PASSWORD", "testtest")
//            .secret("EMAIL", "kim.almroth@apendo.se")
//            .secret("API_URL", "http://83.233.216.66:35462/")
//            .build();
//    // when
//    context.replaceSecrets(input);
//    // then
//    assertThat(input)
//      .extracting("password")
//      .isEqualTo("testtest");
//  }

  @Test
  void saveTest() throws Exception {
    // given
    var input = new SaveDataRequest();
    input.setToken("Hello World!");
    input.setPassword("secrets.PASSWORD");
    input.setEmail("secrets.EMAIL");
    input.setApiUrl("secrets.API_URL");
    input.setSeatUuid("d06293f9-47ca-4cc6-a270-c21a87e68619");

    var context = OutboundConnectorContextBuilder
            .create()
            .secret("PASSWORD", "testtest")
            .secret("EMAIL", "kim.almroth@apendo.se")
            .secret("API_URL", "http://localhost:35462/")
            .variables(input)
            .build();
    // when
    context.replaceSecrets(input);

    var function = new SaveDataFunction();
    var result = function.execute(context);

    // then

    assertThat(result)
            .isInstanceOf(SaveDataResult.class)
            .extracting("message")
            .isEqualTo("Seat report has been saved");
  }
//  @Test
//  void getTest() throws ExecutionException, InterruptedException {
//    // given
//    var input = new SaveDataRequest();
//    input.setPassword("secrets.PASSWORD");
//    input.setEmail("secrets.EMAIL");
//    input.setBusinessName("ICA");
//    input.setToken("");
//    var context = OutboundConnectorContextBuilder.create()
//            .secret("PASSWORD", "testtest").secret("EMAIL", "kim@test.com")
//            .build();
//
//    // when
//    context.replaceSecrets(input);
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://83.233.216.66:35462/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//    SeatReportClient client = retrofit.create(SeatReportClient.class);
//
//    var loginResponse = login(client, input);
//    input.setToken(loginResponse.getAccessToken());
//
//    var acc = updateSeatReport(client, input.getToken(), input);
//
//    // then
//
//    assertThat(acc).isNotEmpty();
//    assertThat(acc.get(0)).isNotNull();
//    assertThat(acc.get(0).getUuid().toString()).isNotEqualTo("");
//    assertThat(acc.get(0).getBusinessUuid().toString()).isNotEqualTo("");
//  }
//
//  @Test
//  void shouldFailWhenValidate_NoToken() {
//    // given
//    var input = new MyConnectorRequest();
//    var auth = new Authentication();
//    input.setMessage("Hello World!");
//    input.setAuthentication(auth);
//    auth.setUser("testuser");
//    var context = OutboundConnectorContextBuilder.create().build();
//    // when
//    assertThatThrownBy(() -> context.validate(input))
//      // then
//      .isInstanceOf(ConnectorInputException.class)
//      .hasMessageContaining("token");
//  }
//
//  @Test
//  void shouldFailWhenValidate_NoMesage() {
//    // given
//    var input = new MyConnectorRequest();
//    var auth = new Authentication();
//    input.setAuthentication(auth);
//    auth.setUser("testuser");
//    auth.setToken("xobx-test");
//    var context = OutboundConnectorContextBuilder.create().build();
//    // when
//    assertThatThrownBy(() -> context.validate(input))
//      // then
//      .isInstanceOf(ConnectorInputException.class)
//      .hasMessageContaining("message");
//  }
//
//  @Test
//  void shouldFailWhenValidate_TokenWrongPattern() {
//    // given
//    var input = new MyConnectorRequest();
//    var auth = new Authentication();
//    input.setMessage("foo");
//    input.setAuthentication(auth);
//    auth.setUser("testuser");
//    auth.setToken("test");
//    var context = OutboundConnectorContextBuilder.create().build();
//    // when
//    assertThatThrownBy(() -> context.validate(input))
//      // then
//      .isInstanceOf(ConnectorInputException.class)
//      .hasMessageContaining("Token must start with \"xobx\"");
//  }
}
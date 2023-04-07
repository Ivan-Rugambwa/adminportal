package io.camunda.connector;

import static io.camunda.connector.CreateSeatFunction.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ExecutionException;

public class MyRequestTest {

  @Test
  void shouldReplaceTokenSecretWhenReplaceSecrets() {
    // given
    var input = new CreateSeatRequest();
    input.setToken("Hello World!");
    input.setPassword("secrets.PASSWORD");
    input.setEmail("secrets.EMAIL");

    var context = OutboundConnectorContextBuilder.create()
      .secret("PASSWORD", "testtest").secret("EMAIL", "kim@test.com")
      .build();
    // when
    context.replaceSecrets(input);
    // then
    assertThat(input)
      .extracting("password")
      .isEqualTo("testtest");
  }

  @Test
  void loginTest() throws ExecutionException, InterruptedException {
    // given
    var input = new CreateSeatRequest();
    input.setPassword("secrets.PASSWORD");
    input.setEmail("secrets.EMAIL");
    input.setToken("");
    var context = OutboundConnectorContextBuilder.create()
            .secret("PASSWORD", "testtest").secret("EMAIL", "kim@test.com")
            .build();

    // when
    context.replaceSecrets(input);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://wsprakt3.apendo.se:9000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    AccountClient client = retrofit.create(AccountClient.class);

    var loginResponse = login(client, input);
    input.setToken(loginResponse.getAccessToken());

    // then

    assertThat(input)
            .extracting("token")
            .isNotEqualTo("");
  }
  @Test
  void createSeatTest() throws ExecutionException, InterruptedException {
    // given
    var input = new CreateSeatRequest();
    input.setPassword("secrets.PASSWORD");
    input.setEmail("secrets.EMAIL");
    input.setAccountUuid("55c282d4-ed4c-407b-a1c9-47d94cca6432");
    input.setBusinessUuid("e1e5c01f-84cd-415c-bd5b-faf62bff30a1");
    input.setToday("2020-03-23");
    input.setToken("");
    var context = OutboundConnectorContextBuilder.create()
            .secret("PASSWORD", "testtest").secret("EMAIL", "kim@test.com")
            .build();

    // when
    context.replaceSecrets(input);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://wsprakt3.apendo.se:9000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    AccountClient client = retrofit.create(AccountClient.class);

    var loginResponse = login(client, input);
    input.setToken(loginResponse.getAccessToken());

    var acc = CreateSeatBase(client, loginResponse.getAccessToken(), input);

    // then

    assertThat(acc)
            .isNotNull();
  }
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
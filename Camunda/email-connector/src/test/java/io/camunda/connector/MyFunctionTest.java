package io.camunda.connector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import io.camunda.connector.api.error.ConnectorException;
import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

public class MyFunctionTest {

//  @Test
//  void sendEmail() throws Exception {
//    // given
//    var input = new EmailRequest();
//    input.setHost("secrets.HOST");
//    input.setFrom("secrets.FROM");
//    input.setPort("secrets.PORT");
//    input.setPassword("secrets.PASSWORD");
//    input.setText("det h\\u00E4r \\u00E4r ett test & test");
//    input.setTo("kim.almroth@apendo.se");
//    input.setSubject("test from test");
//
//    var context = OutboundConnectorContextBuilder.create()
//            .secret("HOST", "mail.smtp2go.com")
//            .secret("FROM", "apendo.operations@outlook.com")
//            .secret("PASSWORD", "qq")
//            .secret("PORT", "2525")
//            .variables(input)
//            .build();
//    // when
//    context.replaceSecrets(input);
//    var function = new EmailFunction();
//
//    var result = function.execute(context);
//    // then
//
//    assertThat(input)
//            .isInstanceOf(EmailRequest.class)
//            .extracting("text")
//            .isEqualTo("det här är ett test & test");
//
//    assertThat(result)
//      .isInstanceOf(EmailResult.class)
//      .extracting("message")
//      .isEqualTo("Success!");
//  }

//  @Test
//  void shouldThrowWithErrorCodeWhenMessageStartsWithFail() {
//    // given
//    var input = new MyConnectorRequest();
//    var auth = new Authentication();
//    input.setMessage("Fail: unauthorized");
//    input.setAuthentication(auth);
//    auth.setToken("xobx-test");
//    auth.setUser("testuser");
//    var function = new MyConnectorFunction();
//    var context = OutboundConnectorContextBuilder.create()
//        .variables(input)
//        .build();
//    // when
//    var result = catchThrowable(() -> function.execute(context));
//    // then
//    assertThat(result)
//        .isInstanceOf(ConnectorException.class)
//        .hasMessageContaining("started with 'fail'")
//        .extracting("errorCode").isEqualTo("FAIL");
//  }
}
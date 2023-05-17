package io.camunda.connector;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;

import static org.assertj.core.api.Assertions.assertThat;

public class MyFunctionTest {

    @Test
    void sendEmail() {
        // given
        var input = new EmailRequest();
        input.setHost("secrets.HOST");
        input.setFrom("secrets.FROM");
        input.setPort("secrets.PORT");
        input.setPassword("secrets.PASSWORD");
        input.setType("seat-report");
        input.setTo("kim.almroth@apendo.se");
        input.setSubject("test from test");
        input.setEmailFrequency("MONTHLY");
        input.setFirstName("Kim");
        input.setLastName("Almroth");
        input.setReportUrl("http://camcaas.apendo.se/");
        input.setSeatUuid("9262191e-02d9-4ffa-816c-d0b145af80bb");
        input.setForDate("2023/06");
        input.setDenialMessage("Ser inte bra ut");
        input.setBusinessName("IKEA");
        input.setBaseline("45");
        input.setSeatOverUsage("3");
        input.setSeatUsedAmount("48");

        var context = OutboundConnectorContextBuilder.create()
                .secret("HOST", "mail.smtp2go.com")
                .secret("FROM", "apendo.operations@outlook.com")
                .secret("PASSWORD", "qq")
                .secret("PORT", "2525")
                .variables(input)
                .build();
        // when
        context.replaceSecrets(input);
        var function = new EmailFunction();

        var result = function.execute(context);
        // then

        assertThat(result)
                .isInstanceOf(EmailResult.class)
                .extracting("result")
                .isEqualTo("Sent monthly email");
    }

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
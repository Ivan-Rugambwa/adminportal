package io.camunda.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

public class SeatTest {

  @Test
  void testSeat() throws Exception {
    // given
    var input = new SeatSingleRequest();
    input.setEmail("secrets.EMAIL");
    input.setPassword("secrets.PASSWORD");
    input.setApiUrl("secrets.API_URL");
    input.setForDate("2020-04-01");
    input.setBusinessUuid("4d14eaa7-42b9-4dba-88d7-bee747dfcd53");
    var function = new SeatSingleFunction();
    var context = OutboundConnectorContextBuilder.create()
            .secret("EMAIL", "kim.almroth@apendo.se")
            .secret("PASSWORD", "testtest")
            .secret("API_URL", "http://192.168.1.62:35462/")
      .variables(input)
      .build();
    // when
    var result = function.execute(context);
    // then
    assertThat(result)
      .isInstanceOf(SeatSingleResult.class)
      .extracting("seatResponse")
      .isNotNull();
  }

}
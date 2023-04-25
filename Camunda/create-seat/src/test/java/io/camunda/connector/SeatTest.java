package io.camunda.connector;

import static org.assertj.core.api.Assertions.assertThat;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

public class SeatTest {

  @Test
  void testSeat() throws Exception {
    // given
    var input = new SeatRequest();
    input.setEmail("secrets.EMAIL");
    input.setPassword("secrets.PASSWORD");
    input.setToday("2020-01-01");
    input.setApiUrl("http://localhost:35462");
    var function = new SeatFunction();
    var context = OutboundConnectorContextBuilder.create()
            .secret("EMAIL", "kim.almroth@apendo.se")
            .secret("PASSWORD", "testtest")
            .secret("API_URL", "http://localhost:35462")
      .variables(input)
      .build();
    // when
    var result = function.execute(context);
    // then
    assertThat(result)
      .isInstanceOf(SeatResult.class)
      .extracting("seatResponses")
      .isNotNull();
  }

}
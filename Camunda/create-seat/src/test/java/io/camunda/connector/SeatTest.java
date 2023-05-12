package io.camunda.connector;

import io.camunda.connector.test.outbound.OutboundConnectorContextBuilder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SeatTest {

    @Test
    void testSeat() throws Exception {
        // given
        var input = new SeatRequest();
        input.setEmail("secrets.EMAIL");
        input.setPassword("secrets.PASSWORD");
        input.setToday("2023-05-11");
        input.setApiUrl("secrets.API_URL");
        var function = new SeatFunction();
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
                .isInstanceOf(SeatResult.class)
                .extracting("seatResponses")
                .isNotNull();
    }

}
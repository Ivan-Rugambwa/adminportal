package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class SeatRequest {
    @Secret
    private String email;
    @Secret
    private String password;
    private String today;
}

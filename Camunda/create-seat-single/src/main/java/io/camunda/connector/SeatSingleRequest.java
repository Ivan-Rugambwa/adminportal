package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class SeatSingleRequest {
    @Secret
    private String email;
    @Secret
    private String password;
    @Secret
    private String apiUrl;
    private String forDate;
    private String businessUuid;
}

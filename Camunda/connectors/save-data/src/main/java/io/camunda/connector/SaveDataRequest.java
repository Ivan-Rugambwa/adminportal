package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class SaveDataRequest {

    @Secret
    private String apiUrl;
    @Secret
    private String email;
    @Secret
    private String password;
    private String seatUuid;
    private String status;
    private String token;

}

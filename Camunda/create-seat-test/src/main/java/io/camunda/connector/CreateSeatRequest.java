package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class CreateSeatRequest {


    @Secret
    private String email;
    @Secret
    private String password;
    private String token;
    private String accountUuid;
    private String businessUuid;
    private String today;

}

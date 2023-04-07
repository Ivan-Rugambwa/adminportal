package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class GetDataRequest {

    @Secret
    private String email;
    @Secret
    private String password;
    private String token;

}

package io.camunda.connector;

import lombok.Data;

import java.util.UUID;

@Data
public class Account {
    private UUID uuid;
    private UUID businessUuid;
    private String email;
    private String firstName;
    private String lastName;
}

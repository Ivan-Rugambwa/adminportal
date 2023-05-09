package io.camunda.connector.retrofit.business;

import lombok.Data;

import java.util.UUID;

@Data
public class BusinessResponse {
    private UUID uuid;
    private String name;
    private Integer seatAmount;
}

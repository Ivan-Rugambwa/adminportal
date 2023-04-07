package io.camunda.connector.viewmodels;

import lombok.Data;

@Data
public class CreateSeatBaseRequest {
    private String accountUuid;
    private String businessUuid;
    private String forYearMonth;
}

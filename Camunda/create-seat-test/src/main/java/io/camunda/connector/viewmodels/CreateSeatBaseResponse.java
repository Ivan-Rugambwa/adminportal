package io.camunda.connector.viewmodels;

import lombok.Data;

import java.util.Date;

@Data
public class CreateSeatBaseResponse {
    private String businessName;
    private String assignedAccountEmail;
    private Boolean isCompleted;
    private Date lastChangeDate;
    private Integer seatUsed;
    private String forYearMonth;
}

package io.camunda.connector.retrofit.seat;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CreateSeatResponse {
    private UUID uuid;
    private String businessName;
    private Integer businessBaseline;
    private String completedByEmail;
    private Boolean isCompleted;
    private Date lastChangeDate;
    private Integer seatUsed;
    private String forYearMonth;
}

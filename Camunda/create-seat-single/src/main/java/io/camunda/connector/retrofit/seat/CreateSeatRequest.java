package io.camunda.connector.retrofit.seat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSeatRequest {
    private String businessUuid;
    private String forYearMonth;
}

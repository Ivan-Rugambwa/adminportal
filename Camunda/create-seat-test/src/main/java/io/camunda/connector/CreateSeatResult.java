package io.camunda.connector;

import io.camunda.connector.viewmodels.CreateSeatBaseResponse;
import lombok.Data;

@Data
public class CreateSeatResult {

  // TODO: define connector result properties, which are returned to the process engine
  private CreateSeatBaseResponse seatBase;
  private String message;


}

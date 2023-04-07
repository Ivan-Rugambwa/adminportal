package io.camunda.connector;

import lombok.Data;

import java.util.List;

@Data
public class GetDataResult {

  // TODO: define connector result properties, which are returned to the process engine
  private List<Account> users;
  private String message;


}

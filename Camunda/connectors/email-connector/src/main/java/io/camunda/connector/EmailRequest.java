package io.camunda.connector;

import io.camunda.connector.api.annotation.Secret;
import lombok.Data;

@Data
public class EmailRequest {

    @Secret
    private String from;
    @Secret
    private String password;
    @Secret
    private String host;
    @Secret
    private String port;
    private String to;
    private String subject;
    private String emailFrequency = "";
    private String firstName = "";
    private String lastName = "";
    private String reportUrl = "";
    private String seatUuid = "";
    private String type = "";
    private String forDate = "";
    private String denialMessage = "";
    private String businessName = "";
    private String baseline = "";
    private String seatUsedAmount = "";
    private String seatOverUsage = "";

}

package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import io.camunda.connector.email.Email;
import org.apache.commons.text.StringEscapeUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

import static io.camunda.connector.email.Email.sendMail;

@OutboundConnector(
        name = "Send email",
        inputVariables = {"from", "to", "port", "password", "host", "subject",
                "text", "emailFrequency", "type", "firstName", "lastName",
                "reportUrl", "seatUuid", "forDate", "denialMessage", "businessName",
                "baseline", "seatUsedAmount", "seatOverUsage"},
        type = "apendo:send-email:1")
public class EmailFunction implements OutboundConnectorFunction {

    @Override
    public Object execute(OutboundConnectorContext context) {
        var connectorRequest = context.getVariablesAsType(EmailRequest.class);

        context.validate(connectorRequest);
        context.replaceSecrets(connectorRequest);

        return executeConnector(connectorRequest);
    }

    private EmailResult executeConnector(final EmailRequest request) {
        System.out.println("\nSending email...");
        System.out.println(request);

        request.setSubject(StringEscapeUtils.unescapeJava(request.getSubject()));

        var currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        var emailResult = new EmailResult();
        var message = "Month is " + currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
        message += " and report email frequency is " + request.getEmailFrequency();
        emailResult.setMessage(message);
        System.out.println(message);

        var emailText = Email.getEmailText(request);

        if (Objects.equals(request.getEmailFrequency(), "MONTHLY")) {
            emailResult = sendMail(request, emailText);
            emailResult.setResult("Sent monthly email");
            System.out.println("Sent monthly email");

        } else if (Objects.equals(request.getEmailFrequency(), "QUARTERLY") &&
                (currentMonth == Month.MARCH || currentMonth == Month.JUNE
                        || currentMonth == Month.SEPTEMBER || currentMonth == Month.DECEMBER)) {
            emailResult = sendMail(request, emailText);
            emailResult.setResult("Sent quarterly email");
            System.out.println("Sent quarterly email");

        } else if (Objects.equals(request.getEmailFrequency(), "SEMIANNUALLY") &&
                (currentMonth == Month.JUNE || currentMonth == Month.DECEMBER)) {
            emailResult = sendMail(request, emailText);
            emailResult.setResult("Sent semiannual email");
            System.out.println("Sent semiannual email");

        } else if (Objects.equals(request.getEmailFrequency(), "ANNUALLY") && currentMonth == Month.DECEMBER) {
            emailResult = sendMail(request, emailText);
            emailResult.setResult("Sent annual email");
            System.out.println("Sent annual email");
        } else {
            emailResult.setResult("Will not send email due to it not being correct month and frequency type");
            System.out.println("Will not send email due to it not being correct month and frequency type");
        }
        return emailResult;
    }
}

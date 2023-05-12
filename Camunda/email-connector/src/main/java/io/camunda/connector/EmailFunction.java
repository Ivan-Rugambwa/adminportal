package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.apache.commons.text.StringEscapeUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

@OutboundConnector(
        name = "Send email",
        inputVariables = {"from", "to", "port", "password", "host", "subject", "text", "emailFrequency"},
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
        request.setText(StringEscapeUtils.unescapeJava(request.getText()));

        var currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        var emailResult = new EmailResult();
        var message = "Month is " + currentMonth.getDisplayName(TextStyle.FULL, Locale.getDefault());
        message += " and report email frequency is " + request.getEmailFrequency();
        emailResult.setMessage(message);
        System.out.println(message);

        if (Objects.equals(request.getEmailFrequency(), "MONTHLY")) {
            emailResult = sendMail(request);
            emailResult.setResult("Sent monthly email");
            System.out.println("Sent monthly email");

        } else if (Objects.equals(request.getEmailFrequency(), "QUARTERLY") &&
                (currentMonth == Month.MARCH || currentMonth == Month.JUNE
                        || currentMonth == Month.SEPTEMBER || currentMonth == Month.DECEMBER)) {
            emailResult = sendMail(request);
            emailResult.setResult("Sent quarterly email");
            System.out.println("Sent quarterly email");

        } else if (Objects.equals(request.getEmailFrequency(), "SEMIANNUALLY") &&
                (currentMonth == Month.JUNE || currentMonth == Month.DECEMBER)) {
            emailResult = sendMail(request);
            emailResult.setResult("Sent semiannual email");
            System.out.println("Sent semiannual email");

        } else if (Objects.equals(request.getEmailFrequency(), "ANNUALLY") && currentMonth == Month.DECEMBER) {
            emailResult = sendMail(request);
            emailResult.setResult("Sent annual email");
            System.out.println("Sent annual email");
        } else {
            emailResult.setResult("Will not send email due to it not being correct month and frequency type");
            System.out.println("Will not send email due to it not being correct month and frequency type");
        }
        return emailResult;
    }

    private EmailResult sendMail(EmailRequest request) {
        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", request.getHost());
        mailProperties.put("mail.smtp.port", request.getPort());
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(mailProperties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(request.getFrom(), request.getPassword());
            }
        });

        EmailResult emailResult = new EmailResult();

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(request.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getTo()));
            message.setSubject(request.getSubject());
            message.setText(request.getText());

            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException mex) {
            System.out.println("Something went wrong sending email: " + mex.getLocalizedMessage());
            throw new IllegalStateException(mex.getLocalizedMessage());
        }

        return emailResult;
    }
}

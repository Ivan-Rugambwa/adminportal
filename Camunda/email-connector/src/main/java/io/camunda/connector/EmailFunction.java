package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@OutboundConnector(
        name = "Test Secrets",
        inputVariables = {"from", "to", "password", "host", "subject", "text"},
        type = "apendo:get-secrets:1")
public class EmailFunction implements OutboundConnectorFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailFunction.class);

    @Override
    public Object execute(OutboundConnectorContext context) throws Exception {
        var connectorRequest = context.getVariablesAsType(EmailRequest.class);

        context.validate(connectorRequest);
        context.replaceSecrets(connectorRequest);

        return executeConnector(connectorRequest);
    }

    private EmailResult executeConnector(final EmailRequest connectorRequest) {
        // TODO: implement connector logic
        LOGGER.info("Executing my connector with request {}", connectorRequest);

        EmailRequest emailRequest = new EmailRequest();

        String from = connectorRequest.getFrom();
        String password = connectorRequest.getPassword();
        String host = connectorRequest.getHost();
        String to = connectorRequest.getTo();

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", host);
        mailProperties.put("mail.smtp.port", "587");
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        mailProperties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

        Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        EmailResult emailResult = new EmailResult();

        try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                message.setSubject(connectorRequest.getSubject());
                message.setText(connectorRequest.getText());

                Transport.send(message);
                emailResult.setMessage("Success!");
            System.out.println("Email sent successfully!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            emailResult.setMessage("Could not send email...");
        }
//        emailResult.setMessage(connectorRequest.toString());
        return emailResult;
    }
}

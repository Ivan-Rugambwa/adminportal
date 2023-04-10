package io.camunda.connector;

import io.camunda.connector.api.annotation.OutboundConnector;
import io.camunda.connector.api.outbound.OutboundConnectorContext;
import io.camunda.connector.api.outbound.OutboundConnectorFunction;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@OutboundConnector(
        name = "Send email",
        inputVariables = {"from", "to", "port", "password", "host", "subject", "text"},
        type = "apendo:send-email:1")
public class EmailFunction implements OutboundConnectorFunction {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailFunction.class);

    @Override
    public Object execute(OutboundConnectorContext context) throws Exception {
        var connectorRequest = context.getVariablesAsType(EmailRequest.class);

        context.validate(connectorRequest);
        context.replaceSecrets(connectorRequest);

        return executeConnector(connectorRequest);
    }

    private EmailResult executeConnector(final EmailRequest request) {
        // TODO: implement connector logic
        LOGGER.info("Executing my connector with request {}", request);

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", request.getHost());
        mailProperties.put("mail.smtp.port", request.getPort());
        mailProperties.put("mail.smtp.starttls.enable", "true");
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.ssl.protocols", "TLSv1.2");
//        mailProperties.put("mail.smtp.ssl.ciphersuites", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256");

        Session session = Session.getInstance(mailProperties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(request.getFrom(), request.getPassword());
            }
        });

        request.setSubject(StringEscapeUtils.unescapeJava(request.getSubject()));
        request.setText(StringEscapeUtils.unescapeJava(request.getText()));

        EmailResult emailResult = new EmailResult();

        try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(request.getFrom()));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(request.getTo()));
                message.setSubject(request.getSubject());
                message.setText(request.getText());

                Transport.send(message);
                emailResult.setMessage("Success!");
            System.out.println("Email sent successfully!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            emailResult.setMessage("Could not send email...");
        }
        return emailResult;
    }
}

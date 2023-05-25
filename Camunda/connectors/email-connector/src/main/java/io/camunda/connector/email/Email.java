package io.camunda.connector.email;

import io.camunda.connector.EmailRequest;
import io.camunda.connector.EmailResult;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class Email {
    protected static String formatFrequencyText(String frequency) {
        switch (frequency) {
            case "MONTHLY":
                return "denna månad";
            case "QUARTERLY":
                return "detta kvartal";
            case "SEMIANNUALLY":
                return "detta halvår";
            case "ANNUALLY":
                return "detta år";
            default:
                throw new IllegalStateException("Email frequency month is invalid: " + frequency);
        }
    }
    protected static String formatReportUrl(EmailRequest request) {
        switch (request.getEmailFrequency()) {
            case "MONTHLY":
                return request.getReportUrl() + "seat/report?uuid=" + request.getSeatUuid();
            case "QUARTERLY":
            case "SEMIANNUALLY":
            case "ANNUALLY":
                return request.getReportUrl() + "seat/";
            default:
                throw new IllegalStateException("Email frequency month is invalid: " + request.getEmailFrequency());
        }

    }

    public static String getEmailText(EmailRequest request){
        switch (request.getType()){
            case "seat-report":
                return SeatReport.getText(request);
            case "remainder":
                return Remainder.getText(request);
            case "denial":
                return Denied.getText(request);
            case "invoice":
                return Invoice.getText(request);
            case "all-done":
                return AllDone.getText(request);
            default:
                throw new IllegalArgumentException("Invalid email type: " + request.getType());
        }
    }
    public static EmailResult sendMail(EmailRequest request, String body) {
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
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(request.getTo()));
            message.setSubject(request.getSubject());
            message.setContent(body, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("Email sent successfully");
        } catch (MessagingException mex) {
            System.out.println("Something went wrong sending email: " + mex.getLocalizedMessage());
            throw new IllegalStateException(mex.getLocalizedMessage());
        }

        return emailResult;
    }
}

package io.camunda.connector.email;

import io.camunda.connector.EmailRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Invoice extends Email {
    public static String getText(EmailRequest request){
        try {
            var path = Path.of("/opt/Email/invoice.html");
            String email = new String(Files.readAllBytes(path));
            email = email.replaceAll(Pattern.quote("{businessName}"), request.getBusinessName());
            email = email.replaceAll(Pattern.quote("{baseLine}"), request.getBaseline());
            email = email.replaceAll(Pattern.quote("{seatUsedAmount}"), request.getSeatUsedAmount());
            email = email.replaceAll(Pattern.quote("{seatOverUsage}"), request.getSeatOverUsage());
            email = email.replaceAll(Pattern.quote("{forDate}"), request.getForDate());
            return email;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }
}

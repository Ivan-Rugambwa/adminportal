package io.camunda.connector.email;

import io.camunda.connector.EmailRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class Denied extends Email{
    public static String getText(EmailRequest request){
        try {
            var path = Path.of("/opt/Email/denied.html");
            String email = new String(Files.readAllBytes(path));
            email = email.replaceAll(Pattern.quote("{firstName}"), request.getFirstName());
            email = email.replaceAll(Pattern.quote("{lastName}"), request.getLastName());
            email = email.replaceAll(Pattern.quote("{forDate}"), request.getForDate());
            email = email.replaceAll(Pattern.quote("{reasonForDenial}"), request.getDenialMessage());
            email = email.replaceAll(Pattern.quote("{reportUrl}"), request.getReportUrl() + "seat/report?uuid=" + request.getSeatUuid());
            return email;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }
}

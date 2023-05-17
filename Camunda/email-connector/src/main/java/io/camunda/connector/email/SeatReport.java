package io.camunda.connector.email;

import io.camunda.connector.EmailRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class SeatReport extends Email {

    public static String getText(EmailRequest request){
        try {
        var path = Path.of("/opt/Email/seat-report.html");
            String email = new String(Files.readAllBytes(path));
            email = email.replaceAll(Pattern.quote("{firstName}"), request.getFirstName());
            email = email.replaceAll(Pattern.quote("{lastName}"), request.getLastName());
            email = email.replaceAll(Pattern.quote("{emailFrequency}"), formatFrequencyText(request.getEmailFrequency()));
            email = email.replaceAll(Pattern.quote("{reportUrl}"), formatReportUrl(request));
            return email;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            throw new IllegalStateException(e.getLocalizedMessage());
        }
    }
}

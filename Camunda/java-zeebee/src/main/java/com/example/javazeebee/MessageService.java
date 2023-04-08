package com.example.javazeebee;

import com.example.javazeebee.message.dto.PublishRequest;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;

@Data
@Service
public class MessageService {
    @Qualifier("zeebeClientLifecycle")
    @Autowired
    private ZeebeClient client;

    public Long publish(PublishRequest request) {
        var correlationKey = request.getBusiness() + "-" + request.getForYearMonth();
        var response = client.newPublishMessageCommand()
                .messageName(request.getMessage())
                .correlationKey(correlationKey)
                .messageId(UUID.randomUUID().toString())
                .timeToLive(Duration.ofSeconds(10))
                .send().join();
        return response.getMessageKey();
    }

    public boolean isTokenValid(String token) throws Exception {
        var authUrl = "http://83.233.216.66:35462/api/auth/validate";
        token = token.substring(7).trim();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"token\":" + "\"" + token + "\"}"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == HttpStatus.OK.value();

    }
    public void updateSeat(PublishRequest request, String token) throws IOException, InterruptedException {

        var authUrl = "http://83.233.216.66:35462/api/admin/seat/" + request.getSeatUuid();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(authUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .method("PATCH", HttpRequest.BodyPublishers.ofString("{\"usedSeat\":" + request.getAmountOfSeatsUsed() +
                        ",\"updatedByEmail\":\"" + request.getEmail() +
                        "\",\"seatUuid\":\"" + request.getSeatUuid() + "\"}"))
                .build();

        HttpResponse<String> response = client.send(patchRequest, HttpResponse.BodyHandlers.ofString());

    }
}

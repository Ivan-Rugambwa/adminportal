package com.example.javazeebee;

import com.example.javazeebee.message.dto.PublishRequest;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Data
@Service
public class MessageService {
    @Qualifier("zeebeClientLifecycle")
    @Autowired
    private ZeebeClient client;

    public Long publish(PublishRequest request) {
        var correlationKey = request.getEmail() + "-" + request.getYear() + "/" + request.getMonth();
        var response = client.newPublishMessageCommand()
                .messageName(request.getMessage())
                .correlationKey(correlationKey)
                .messageId(UUID.randomUUID().toString())
                .timeToLive(Duration.ofSeconds(10))
                .send().join();
        return response.getMessageKey();
    }

    public boolean isTokenValid(String token) throws Exception {
        var authUrl = "http://localhost:9000/api/auth/validate";
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
}

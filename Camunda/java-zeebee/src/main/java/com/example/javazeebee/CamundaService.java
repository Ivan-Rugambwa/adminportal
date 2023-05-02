package com.example.javazeebee;

import com.example.javazeebee.message.dto.PublishRequest;
import com.example.javazeebee.message.dto.UpdateSeatRequest;
import com.example.javazeebee.start.dto.StartRequest;
import io.camunda.zeebe.client.ZeebeClient;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Data
@Service
public class CamundaService {
    @Qualifier("zeebeClientLifecycle")
    @Autowired
    private ZeebeClient client;

    public Long publish(PublishRequest request) {
        System.out.println("Publishing message...");
        var correlationKey = request.getBusiness() + "-" + request.getForYearMonth();
        var response = client.newPublishMessageCommand()
                .messageName(request.getMessage())
                .correlationKey(correlationKey)
                .messageId(UUID.randomUUID().toString())
                .timeToLive(Duration.ofSeconds(10))
                .variables(Map.of("seatUsedAmount", request.getAmountOfSeatsUsed(), "filledByEmail", request.getEmail()))
                .send().join();
        System.out.println("Message published");

        return response.getMessageKey();
    }

    public boolean isTokenValid(String token) throws Exception {
        var authUrl = "http://localhost:35462/api/auth/validate";
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

    public boolean updateSeat(PublishRequest request, String token) throws IOException, InterruptedException {
        System.out.println("Updating seat in db...");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:35462/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        var client = retrofit.create(RetroFitClient.class);

        var updateRequest = new UpdateSeatRequest();
        updateRequest.setUpdatedByEmail(request.getEmail());
        updateRequest.setUsedSeat(request.getAmountOfSeatsUsed());
        updateRequest.setStatus("REVIEW");

        var response = client.updateDbSeat(request.getSeatUuid(), updateRequest, token).execute();

        if (response.isSuccessful()) {
            System.out.println("Db updated");
            return true;
        } else {
            System.out.println("Failed to update db");
        }
        System.out.println(response.code());
        return true;
    }

    public void startInstance(StartRequest request, String token) throws ExecutionException, InterruptedException {
        System.out.println("Starting new instance of process: " + request.getProcessId());
        client.newCreateInstanceCommand()
                .bpmnProcessId(request.getProcessId())
                .latestVersion()
                .variables(request.getVariables())
                .send()
                .get();
    }
}

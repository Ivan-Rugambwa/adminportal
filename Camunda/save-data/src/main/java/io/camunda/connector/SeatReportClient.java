package io.camunda.connector;

import io.camunda.connector.viewmodels.LoginRequest;
import io.camunda.connector.viewmodels.LoginResponse;
import retrofit2.http.*;

import java.util.concurrent.CompletableFuture;

public interface SeatReportClient {
    @PATCH("api/admin/seat/{seatUuid}")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<Void> updateSeatReport(@Header("Authorization") String token, @Path("seatUuid") String seatUuid, @Body SeatReport seatReport);

    @POST("api/auth/authenticate")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<LoginResponse> login(@Body LoginRequest request);
}

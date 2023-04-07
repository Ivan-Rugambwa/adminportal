package io.camunda.connector;

import io.camunda.connector.viewmodels.CreateSeatBaseRequest;
import io.camunda.connector.viewmodels.CreateSeatBaseResponse;
import io.camunda.connector.viewmodels.LoginRequest;
import io.camunda.connector.viewmodels.LoginResponse;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AccountClient {
    @POST("api/admin/seat")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<CreateSeatBaseResponse> postSeatBase(@Header("Authorization") String token, @Body CreateSeatBaseRequest request);

    @POST("api/auth/authenticate")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<LoginResponse> login(@Body LoginRequest request);
}

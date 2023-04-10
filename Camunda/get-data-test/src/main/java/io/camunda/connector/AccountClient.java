package io.camunda.connector;

import io.camunda.connector.viewmodels.LoginRequest;
import io.camunda.connector.viewmodels.LoginResponse;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AccountClient {
    @GET("api/admin/user/by/business/name/{businessName}")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<List<Account>> getAccounts(@Header("Authorization") String token, @Path("businessName") String businessName);

    @POST("api/auth/authenticate")
    @Headers({"Content-Type: application/json"})
    CompletableFuture<LoginResponse> login(@Body LoginRequest request);
}

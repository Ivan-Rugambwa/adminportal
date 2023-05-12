package io.camunda.connector.retrofit;

import io.camunda.connector.retrofit.business.BusinessResponse;
import io.camunda.connector.retrofit.login.LoginRequest;
import io.camunda.connector.retrofit.login.LoginResponse;
import io.camunda.connector.retrofit.seat.CreateSeatRequest;
import io.camunda.connector.retrofit.seat.CreateSeatResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.Set;

public interface SeatService {
    @GET("/api/admin/business")
    public Call<Set<BusinessResponse>> GET_BUSINESSES_CALL(@Header("Authorization") String token);

    @POST("/api/auth/authenticate")
    public Call<LoginResponse> LOGIN_CALL(@Body LoginRequest loginRequest);

    @POST("/api/admin/seat")
    public Call<CreateSeatResponse> CREATE_SEAT_RESPONSE_CALL(@Header("Authorization") String token, @Body CreateSeatRequest createSeatRequest);
}

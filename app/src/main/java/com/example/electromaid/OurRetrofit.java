package com.example.electromaid;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OurRetrofit {

    @POST("/v1/auth/signup")
    Call<RegisterResponse> PostData(@Body OurDataSet ourDataSet);

    @POST("/v1/auth/login")
    Call<User> login(@Body OurLogin ourLogin);

    @GET("/v1/sensor")
    Call<ResponseBody> getSercret(@Header("Authorization") String authToken);

    @POST("/v1/auth/forgot")
    Call<ReqForgotPassword> forgotPassword(@Body OurForgotPassword ourForgotPassword);

    @POST("/v1/auth/reset")
    Call<ResResetPassword> resetPassword(@Body OurResetPassword ourResetPassword);

    @Headers("Content-Type: application/json")
    @GET("/v1/sensor")
    Call<List<SensorData>> getSensorData();

    @PUT("/v1/sensor/5fe1542d34c511565e0fb8a4")
    Call<List<Device>> putPost(@Body SensorDataPut sensorDataPut, @Header("x-api-key") String value);

    @POST("/v1/notif")
    Call<List<NotifRes>> postNotif(@Body NotifRes notifRes);

    @GET("/v1/notif")
    Call<List<NotifRes>> getNotif();


}

package com.example.examportal.ApiService;

import com.example.examportal.model.JwtRequest;
import com.example.examportal.model.JwtToken;
import com.example.examportal.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserApiService {

    @POST("/generate-token")
    Call<JwtToken> login(@Body JwtRequest jwtRequest);

    @GET("/current-user")
    Call<User> currentUser(@Header("Authorization") String token);

    @POST("/user/")
    Call<User>addUser(@Body User user);

    @PUT("/user/")
    Call<User> updateUser(@Header("Authorization") String token,@Body User user);

}

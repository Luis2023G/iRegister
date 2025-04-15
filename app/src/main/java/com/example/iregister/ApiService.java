package com.example.iregister;

import com.example.iregister.LoginRequest;
import com.example.iregister.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/usuarios/alumnos/")
    Call<User> getPerfil(@Header("Authorization") String token);
    @POST("api/usuarios/login/")
    Call<LoginResponse> login(@Body LoginRequest request);
}

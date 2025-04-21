package com.example.iregister;

import com.example.iregister.LoginRequest;
import com.example.iregister.LoginResponse;

import com.example.iregister.AlumnoResponse;
import com.example.iregister.SeccionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/usuarios/alumnos/")
    Call<AlumnoListResponse> getDatosAlumno();

    @GET("api/inscripciones/alumnos-secciones/")
    Call<AlumnoSeccionResponse> getSeccionesInscritas(@Query("alumno_id") String alumnoId);

    @GET("api/usuarios/docentes/")
    Call<UserResponse> getPerfilDocente(@Header("Authorization") String token);

    @GET("api/usuarios/alumnos/")
    Call<UserResponse> getPerfilAlumno(@Header("Authorization") String token);


    @POST("api/usuarios/login/")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/asistencia/codigos-qr/verificar/")
    Call<VerificarQrResponse> verificarQr(
            @Header("Authorization") String token,
            @Body VerificarQrRequest request
    );

}

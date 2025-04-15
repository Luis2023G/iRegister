package com.example.iregister;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class LoginActivity extends AppCompatActivity {

    EditText inputUser, inputPass;
    String token;
    Button btnLogin;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputUser = findViewById(R.id.editUsername);
        inputPass = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://8abd-38-25-10-65.ngrok-free.app/")
                .client(client) // 👈 este es el que incluye el interceptor de logging
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(ApiService.class);

        btnLogin.setOnClickListener(v -> {
            String email = inputUser.getText().toString().trim();
            String password = inputPass.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            System.out.println("Mensaje de llamada del request");
            System.out.println(request);
            apiService.login(request).enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    System.out.println("Mensaje de llamada al login");
                    System.out.println(response);
                    if (response.isSuccessful()) {
                        LoginResponse loginResponse = response.body();
                        token = loginResponse.getToken();
                        System.out.println(token);
                        Toast.makeText(LoginActivity.this, "Bienvenido " + loginResponse.getUser_id(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("token", token); // si quieres pasarlo
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LOGIN", "Error:", t);
                }
            });
            String token2 = "Token " + token;
            apiService.getPerfil(token2).enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    System.out.println("Mensaje de llamada al token");
                    System.out.println(response);
                    if (response.isSuccessful()) {
                        User UserResponse = response.body();

                        Toast.makeText(LoginActivity.this, "Bienvenido" + UserResponse.getStr_nombres(), Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error al obtener lista de usuarios", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("LOGIN", "Error:", t);
                }

                ;

        });
    });
}}

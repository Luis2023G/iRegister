package com.example.iregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText inputUser, inputPass;
    Button btnLogin;
    CheckBox checkboxRecordar;
    SharedPreferences preferences;
    ApiService apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUser = findViewById(R.id.editUsername);
        inputPass = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        checkboxRecordar = findViewById(R.id.checkboxRecordar);

// Inicializa SharedPreferences
        preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

// Carga datos si existen
        String savedEmail = preferences.getString("email", "");
        String savedPassword = preferences.getString("password", "");
        boolean isChecked = preferences.getBoolean("recordar", false);

        inputUser.setText(savedEmail);
        inputPass.setText(savedPassword);
        checkboxRecordar.setChecked(isChecked);


        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.redactHeader("Authorization");
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://355b-38-25-10-65.ngrok-free.app/")
                .client(client)
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
            if (checkboxRecordar.isChecked()) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.putBoolean("recordar", true);
                editor.apply();
            } else {
                preferences.edit().clear().apply();
            }

            doLoginFlow(email, password);
        });
    }

    private void doLoginFlow(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    boolean isDocente = response.body().isIs_docente();
                    fetchUserProfile(token, email, isDocente);
                } else {
                    Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN", "Error en login:", t);
            }
        });
    }

    private void fetchUserProfile(String token, String emailLogin, boolean isDocente) {
        String authHeader = "Token " + token;
        Call<UserResponse> call = isDocente
                ? apiService.getPerfilDocente(authHeader)
                : apiService.getPerfilAlumno(authHeader);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<User> users = response.body().getResults();

                    User matchingUser = null;
                    for (User user : users) {
                        if (emailLogin.equalsIgnoreCase(user.getEmail()))
                        {
                            matchingUser = user;
                            break;
                        }
                    }

                    if (matchingUser != null) {
                        Toast.makeText(LoginActivity.this, "Bienvenido " + matchingUser.getStr_nombres(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("nombre", matchingUser.getStr_nombres());
                        intent.putExtra("email", matchingUser.getEmail());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Usuario no encontrado con ese correo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Error al obtener perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error de conexión al obtener perfil: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN", "Error en perfil:", t);
            }
        });
    }
}

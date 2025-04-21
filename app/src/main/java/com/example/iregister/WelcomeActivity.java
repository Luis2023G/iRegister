package com.example.iregister;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iregister.ApiClient;
import com.example.iregister.ApiService;
import com.example.iregister.AlumnoResponse;
import com.example.iregister.Seccion;
import com.example.iregister.SeccionResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    Button btnAsistencia, btnCerrarSesion;
    TextView txtBienvenida, txtNombreCompleto, txtCodigoAlumno, txtCorreo, txtNacimiento, txtSecciones;

    private String token;
    private ApiService apiService;
    private String emailUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Referencias a vistas
        txtBienvenida = findViewById(R.id.txtBienvenida);
        btnAsistencia = findViewById(R.id.btnAsistencia);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        txtNombreCompleto = findViewById(R.id.txtNombreCompleto);
        txtCodigoAlumno = findViewById(R.id.txtCodigoAlumno);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtNacimiento = findViewById(R.id.txtNacimiento);
        txtSecciones = findViewById(R.id.txtSecciones);

        // Obtener datos del Intent
        String nombreUsuario = getIntent().getStringExtra("nombre");
        token = getIntent().getStringExtra("token");
        emailUsuario = getIntent().getStringExtra("email");


        // Mostrar nombre en bienvenida
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            txtBienvenida.setText("Dashboard del alumno");
        }

        // Inicializar Retrofit con token
        apiService = ApiClient.getClient(token).create(ApiService.class);

        // Llamar al endpoint de datos del alumno
        obtenerDatosAlumno();

        btnCerrarSesion.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void obtenerDatosAlumno() {
        Call<AlumnoListResponse> call = apiService.getDatosAlumno();
        call.enqueue(new Callback<AlumnoListResponse>() {
            @Override
            public void onResponse(Call<AlumnoListResponse> call, Response<AlumnoListResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getResults().isEmpty()) {
                    List<AlumnoResponse> alumnos = response.body().getResults();
                    AlumnoResponse alumno = null;

                    for (AlumnoResponse a : alumnos) {
                        if (a.getStr_email().equalsIgnoreCase(emailUsuario)) {
                            alumno = a;
                            break;
                        }
                    }


                    android.util.Log.d("ALUMNO_DEBUG", "Recibido: " + alumno.getStr_nombres() + " " + alumno.getStr_apellidos());

                    txtNombreCompleto.setText("Nombre completo: " + alumno.getStr_nombres() + " " + alumno.getStr_apellidos());
                    txtCodigoAlumno.setText("Código de alumno: " + alumno.getStr_idAlumno());
                    txtCorreo.setText("Correo electrónico: " + alumno.getStr_email());
                    txtNacimiento.setText("Fecha de nacimiento: " + alumno.getDt_fecha_nacimiento());

                    AlumnoResponse finalAlumno = alumno;
                    btnAsistencia.setOnClickListener(v -> {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("alumnoId", finalAlumno.getStr_idAlumno());
                        startActivity(intent);
                        finish();
                    });
                    obtenerSeccionesAlumno(alumno.getStr_idAlumno());
                } else {
                    Toast.makeText(WelcomeActivity.this, "No se encontró información del alumno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AlumnoListResponse> call, Throwable t) {
                Toast.makeText(WelcomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void obtenerSeccionesAlumno(String alumnoId) {
        Call<AlumnoSeccionResponse> call = apiService.getSeccionesInscritas(alumnoId);
        call.enqueue(new Callback<AlumnoSeccionResponse>() {
            @Override
            public void onResponse(Call<AlumnoSeccionResponse> call, Response<AlumnoSeccionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<AlumnoSeccion> secciones = response.body().getResults();

                    StringBuilder texto = new StringBuilder();
                    for (AlumnoSeccion s : secciones) {
                        texto.append("- ID Sección: ").append(s.getIdSeccion()).append("\n");
                    }

                    txtSecciones.setText(texto.toString());
                } else {
                    txtSecciones.setText("No se pudieron cargar las secciones.");
                }
            }

            @Override
            public void onFailure(Call<AlumnoSeccionResponse> call, Throwable t) {
                txtSecciones.setText("Error al cargar secciones: " + t.getMessage());
            }
        });
    }

}

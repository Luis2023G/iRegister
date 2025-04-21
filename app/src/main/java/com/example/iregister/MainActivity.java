package com.example.iregister;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private ImageView resultImage;
    private String alumnoId;
    private String token;
    private ApiService apiService;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String codigoQR = result.getContents(); // Este es el código escaneado, no el token

                    Toast.makeText(this, "Código escaneado", Toast.LENGTH_SHORT).show();

                    if (codigoQR.startsWith("http") && codigoQR.matches("(?i).*\\.(jpg|jpeg|png|gif)$")) {
                        resultImage.setVisibility(ImageView.VISIBLE);
                        resultText.setVisibility(TextView.GONE);
                        Glide.with(this).load(codigoQR).into(resultImage);
                    } else {
                        resultImage.setVisibility(ImageView.GONE);
                        resultText.setVisibility(TextView.VISIBLE);
                        resultText.setText("Verificando asistencia...");
                    }

                    verificarCodigoQR(codigoQR); // Este no es el token del login, sino el código del QR
                } else {
                    Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImage);
        Button btnScan = findViewById(R.id.btnScan);

        alumnoId = getIntent().getStringExtra("alumnoId");
        token = getIntent().getStringExtra("token");

        if (token == null || alumnoId == null) {
            Toast.makeText(this, "Token o Alumno ID no proporcionado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Inicializar ApiService con el token del login
        apiService = ApiClient.getClient(token).create(ApiService.class);

        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Escanea el código QR de asistencia");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivity.class);
            barcodeLauncher.launch(options);
        });
    }

    private void verificarCodigoQR(String codigoEscaneado) {
        VerificarQrRequest request = new VerificarQrRequest(codigoEscaneado, alumnoId);

        Call<VerificarQrResponse> call = apiService.verificarQr("Token " + token, request);

        call.enqueue(new Callback<VerificarQrResponse>() {
            @Override
            public void onResponse(Call<VerificarQrResponse> call, Response<VerificarQrResponse> response) {
                System.out.println("Cuerpo completo de la respuesta:");
                System.out.println(response);

                if (response.isSuccessful() && response.body() != null) {
                    VerificarQrResponse data = response.body();
                    String mensaje = "✅ Asistencia registrada:\n\n" +
                            "📌 ID Asistencia: " + data.getInt_idAsistencia() + "\n" +
                            "🕒 Fecha: " + data.getDt_fecha_registro() + "\n" +
                            "Hora: " + data.getDt_hora_registro() + "\n" +
                            "🎓 Asistió: " + (data.isBool_asistio() ? "Sí" : "No");

                    resultText.setText(mensaje);
                } else {
                    resultText.setText("❌ Error al verificar asistencia.");
                    Toast.makeText(MainActivity.this, "Respuesta no válida de la API", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VerificarQrResponse> call, Throwable t) {
                resultText.setText("❌ Error de red.");
                Toast.makeText(MainActivity.this, "Fallo: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.example.iregister;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private ImageView resultImage;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    String contenido = result.getContents();
                    Toast.makeText(this, "Código escaneado", Toast.LENGTH_SHORT).show();

                    resultText.setText(contenido);

                    if (contenido.startsWith("http") && contenido.matches("(?i).*\\.(jpg|jpeg|png|gif)$")) {
                        // Es una imagen: mostramos la imagen y ocultamos el texto
                        resultImage.setVisibility(ImageView.VISIBLE);
                        resultText.setVisibility(TextView.GONE);
                        Glide.with(this).load(contenido).into(resultImage);
                    } else {
                        // No es imagen: mostramos el texto y ocultamos la imagen
                        resultImage.setVisibility(ImageView.GONE);
                        resultText.setVisibility(TextView.VISIBLE);
                        resultText.setText(contenido);
                    }


                } else {
                    Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnScan = findViewById(R.id.btnScan);
        resultText = findViewById(R.id.resultText);
        resultImage = findViewById(R.id.resultImage);

        btnScan.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Escanea el código QR de asistencia");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivity.class);

            barcodeLauncher.launch(options);
        });
    }
}

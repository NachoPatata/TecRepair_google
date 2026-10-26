package com.example.tecrepair;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class PerfilActivity extends AppCompatActivity {

    // Declaración de los componentes de la interfaz
    ImageView btnVolver;
    MaterialButton btnEditar, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        btnVolver = findViewById(R.id.btnVolver);
        btnEditar = findViewById(R.id.btnEditar);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // ajuste de márgenes del sistema (barras de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVolver.setOnClickListener(v -> finish());

        // Botones creados sin propisito en  la vida
        btnEditar.setOnClickListener(v ->
                Toast.makeText(PerfilActivity.this, "Función pendiente", Toast.LENGTH_SHORT).show());

        btnCerrarSesion.setOnClickListener(v ->
                Toast.makeText(PerfilActivity.this, "Función pendiente", Toast.LENGTH_SHORT).show());
    }
}

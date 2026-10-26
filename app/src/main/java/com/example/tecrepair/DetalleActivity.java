package com.example.tecrepair;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetalleActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Declaración de los componentes de la interfaz
    ImageView btnVolver, imgLocal, btnCopiar;
    TextView txtEspecialidad, txtUbicacion, txtTelefono, txtHorario, txtEstacionamiento,
            txtGarantia, txtPagos, txtPuntuacion, txtCalificacion, txtDiagnostico;
    LinearLayout contenedorResenas;
    RatingBar ratingBarCalificar;
    FloatingActionButton fabBoton;

    Tecnico tecnico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_detalle);

        btnVolver = findViewById(R.id.btnVolver);
        imgLocal = findViewById(R.id.imgLocal); // Foto grande del local
        btnCopiar = findViewById(R.id.btnCopiar); // Ícono para copiar el teléfono
        txtEspecialidad = findViewById(R.id.txtEspecialidad);
        txtUbicacion = findViewById(R.id.txtUbicacion);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtHorario = findViewById(R.id.txtHorario);
        txtEstacionamiento = findViewById(R.id.txtEstacionamiento);
        txtGarantia = findViewById(R.id.txtGarantia);
        txtPagos = findViewById(R.id.txtPagos);
        txtDiagnostico = findViewById(R.id.txtDiagnostico);
        txtPuntuacion = findViewById(R.id.txtPuntuacion);
        txtCalificacion = findViewById(R.id.txtCalificacion); // Texto que muestra las estrellas actuales
        contenedorResenas = findViewById(R.id.contenedorResenas); // Aquí se dibujan las reseñas
        ratingBarCalificar = findViewById(R.id.ratingBarCalificar); // Barra de estrellas para calificar
        fabBoton = findViewById(R.id.fabBoton); // Botón flotante (FloatingActionButton)

        // ajuste de márgenes del sistema (barras de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Busca el técnico que se tocó en la lista (llega por el Intent)
        int id = getIntent().getIntExtra("id", 1);
        tecnico = DataSource.buscarPorId(id);

        // Muestra los datos del técnico
        imgLocal.setImageResource(tecnico.foto);
        txtEspecialidad.setText(tecnico.especialidad);
        txtUbicacion.setText(tecnico.ubicacion);
        txtTelefono.setText(tecnico.telefono);
        txtHorario.setText(tecnico.horario);
        txtEstacionamiento.setText(tecnico.estacionamiento ? "Sí" : "No");
        txtGarantia.setText(tecnico.garantia ? "Sí" : "No");
        txtDiagnostico.setText(tecnico.diagnosticoGratis ? "Gratis" : "Con costo");
        txtPagos.setText(tecnico.pagos);
        mostrarPuntuacion();
        mostrarResenas();

        // Flecha de volver
        btnVolver.setOnClickListener(v -> finish());

        // Copia el teléfono al portapapeles
        btnCopiar.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("telefono", tecnico.telefono));
            Toast.makeText(DetalleActivity.this, "Teléfono copiado", Toast.LENGTH_SHORT).show();
        });

        // evento de ratingbar que detecta cuando el usuario hace un cambio en el rating
        ratingBarCalificar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Actualiza el texto con la nueva cantidad de estrellas seleccionadas
                if (rating == 0) {
                    txtCalificacion.setText("Toca las estrellas y luego el botón para escribir tu reseña");
                } else {
                    txtCalificacion.setText("Tu calificación: " + (int) rating + " estrellas");
                }
            }
        });

        // el FloatingActionButton se activa al presionar el boton inferior derecho
        fabBoton.setOnClickListener(v -> escribirResena());

        // Carga el mapa de Google (cuando está listo se llama a onMapReady)
        SupportMapFragment mapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapa.getMapAsync(this);
    }

    // Se ejecuta cuando el mapa ya está listo para usarse
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Coordenadas exactas del local
        LatLng posicion = new LatLng(tecnico.latitud, tecnico.longitud);

        // Marcador rojo (pin) con el nombre del local
        googleMap.addMarker(new MarkerOptions().position(posicion).title(tecnico.nombre));

        // Centra el mapa en el local (zoom 16)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 16));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    // Muestra el promedio y la cantidad de reseñas del local
    void mostrarPuntuacion() {
        txtPuntuacion.setText("★ " + String.valueOf(tecnico.calificacion).replace('.', ',')
                + " (" + tecnico.cantidadResenas + " reseñas)");
    }

    // Dibuja todas las reseñas del local (la más nueva arriba)
    void mostrarResenas() {
        contenedorResenas.removeAllViews();
        for (Resena r : tecnico.resenas) {
            View vista = getLayoutInflater().inflate(R.layout.item_resena, contenedorResenas, false);
            ((TextView) vista.findViewById(R.id.txtAutorResena)).setText(r.autor);
            ((TextView) vista.findViewById(R.id.txtFechaResena)).setText(r.fecha);
            ((TextView) vista.findViewById(R.id.txtTextoResena)).setText(r.texto);
            ((TextView) vista.findViewById(R.id.txtEstrellasResena)).setText("★ " + r.estrellas);
            contenedorResenas.addView(vista);
        }
    }

    // Abre un cuadro para escribir la reseña (usa las estrellas marcadas en el RatingBar)
    void escribirResena() {
        float estrellas = ratingBarCalificar.getRating();
        if (estrellas == 0) {
            Toast.makeText(this, "Primero marca las estrellas", Toast.LENGTH_SHORT).show();
            return;
        }

        View vista = getLayoutInflater().inflate(R.layout.dialog_resena, null);
        TextView txtEstrellasDialogo = vista.findViewById(R.id.txtEstrellasDialogo);
        EditText edtResena = vista.findViewById(R.id.edtResena);
        txtEstrellasDialogo.setText("Tu calificación: " + (int) estrellas + " estrellas");

        new AlertDialog.Builder(this)
                .setTitle("Escribir reseña")
                .setView(vista)
                .setPositiveButton("Publicar", (dialog, which) -> {
                    String comentario = edtResena.getText().toString().trim();
                    if (comentario.isEmpty()) {
                        Toast.makeText(DetalleActivity.this, "La reseña no puede estar vacía",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        publicarResena(comentario, (int) estrellas);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Guarda la reseña en memoria y actualiza la pantalla
    void publicarResena(String comentario, int estrellas) {
        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        tecnico.resenas.add(0, new Resena("Usuario de ejemplo", fecha, comentario, estrellas));

        // Recalcula el promedio con la nueva calificación
        float suma = tecnico.calificacion * tecnico.cantidadResenas + estrellas;
        tecnico.cantidadResenas++;
        tecnico.calificacion = Math.round(suma / tecnico.cantidadResenas * 10) / 10f;

        ratingBarCalificar.setRating(0);
        mostrarPuntuacion();
        mostrarResenas();
        Toast.makeText(this, "¡Reseña publicada!", Toast.LENGTH_SHORT).show();
    }
}

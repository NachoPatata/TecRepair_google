package com.example.tecrepair;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declaración de los componentes de la interfaz
    DrawerLayout drawer;
    LinearLayout contenido;
    ImageView btnMenu, imgAvatar;
    TextView txtTitulo, txtVacio;
    EditText edtBuscar;
    ChipGroup chipsCategorias;
    Chip chipDiagnostico; // Filtro: solo locales con diagnóstico gratis
    RecyclerView recyclerTecnicos;
    BottomNavigationView bottomNav;
    NavigationView navigationView;

    TecnicoAdapter adapter;

    boolean modoFavoritos = false;      // false = pantalla Inicio, true = pantalla Favoritos
    String categoriaSeleccionada = "";  // vacío = todas las categorías
    boolean soloDiagnosticoGratis = false; // true = muestra solo los que dan diagnóstico gratis
    Tecnico tecnicoSeleccionado;        // tarjeta sobre la que se abrió el menú flotante

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.main); // Menú lateral (Navigation Drawer)
        contenido = findViewById(R.id.contenido);
        btnMenu = findViewById(R.id.btnMenu); // Ícono de hamburguesa
        imgAvatar = findViewById(R.id.imgAvatar); // Avatar de usuario
        txtTitulo = findViewById(R.id.txtTitulo);
        txtVacio = findViewById(R.id.txtVacio); // Mensaje cuando no hay resultados
        edtBuscar = findViewById(R.id.edtBuscar); // Buscador
        chipsCategorias = findViewById(R.id.chipsCategorias); // Chips de categoría
        chipDiagnostico = findViewById(R.id.chipDiagnostico); // Chip de diagnóstico gratis
        recyclerTecnicos = findViewById(R.id.recyclerTecnicos); // Lista de técnicos
        bottomNav = findViewById(R.id.bottomNav); // Barra inferior
        navigationView = findViewById(R.id.navigationView); // Contenido del menú lateral

        // ajuste de márgenes del sistema (barras de estado y navegación)
        ViewCompat.setOnApplyWindowInsetsListener(drawer, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            contenido.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Configura el RecyclerView con su adapter
        adapter = new TecnicoAdapter(this);
        recyclerTecnicos.setLayoutManager(new LinearLayoutManager(this));
        recyclerTecnicos.setAdapter(adapter);

        // Hamburguesa: abre el menú lateral
        btnMenu.setOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        // Avatar: abre la pantalla de perfil
        imgAvatar.setOnClickListener(v -> startActivity(new Intent(this, PerfilActivity.class)));

        // Buscador: filtra la lista mientras el usuario escribe
        edtBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aplicarFiltros();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Chips de categoría: filtran la lista (si se desmarca el chip, se muestran todos)
        chipsCategorias.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                categoriaSeleccionada = "";
            } else {
                Chip chip = findViewById(checkedIds.get(0));
                categoriaSeleccionada = chip.getText().toString();
            }
            aplicarFiltros();
        });

        // Chip de diagnóstico gratis: se puede combinar con la categoría elegida
        chipDiagnostico.setOnCheckedChangeListener((buttonView, isChecked) -> {
            soloDiagnosticoGratis = isChecked;
            aplicarFiltros();
        });

        // Barra inferior: Inicio, Explorar y Favoritos
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inicio) {
                modoFavoritos = false;
                txtTitulo.setText("TecRepair");
                edtBuscar.setVisibility(View.VISIBLE);
                aplicarFiltros();
                return true;
            } else if (id == R.id.nav_favoritos) {
                modoFavoritos = true;
                txtTitulo.setText("Favoritos");
                edtBuscar.setVisibility(View.GONE);
                aplicarFiltros();
                return true;
            } else {
                Toast.makeText(MainActivity.this, "Función pendiente", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Menú lateral: Inicio, Favoritos y Perfil
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.drawer_inicio) {
                bottomNav.setSelectedItemId(R.id.nav_inicio);
            } else if (id == R.id.drawer_favoritos) {
                bottomNav.setSelectedItemId(R.id.nav_favoritos);
            } else if (id == R.id.drawer_perfil) {
                startActivity(new Intent(MainActivity.this, PerfilActivity.class));
            }
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        aplicarFiltros(); // Muestra la lista por primera vez
    }

    // Al volver del detalle se refresca la lista (puede haber cambiado la calificación)
    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            aplicarFiltros();
        }
    }

    // Arma la lista según: pantalla (Inicio/Favoritos), categoría elegida y texto buscado
    public void aplicarFiltros() {
        String texto = "";
        if (!modoFavoritos) {
            texto = edtBuscar.getText().toString().trim().toLowerCase();
        }

        ArrayList<Tecnico> resultado = new ArrayList<>();
        for (Tecnico t : DataSource.lista) {
            if (modoFavoritos && !t.favorito) {
                continue;
            }
            if (!categoriaSeleccionada.isEmpty() && !t.categoria.equals(categoriaSeleccionada)) {
                continue;
            }
            if (soloDiagnosticoGratis && !t.diagnosticoGratis) {
                continue;
            }
            if (!texto.isEmpty() && !coincide(t, texto)) {
                continue;
            }
            resultado.add(t);
        }
        adapter.actualizar(resultado);

        // Si no hay nada que mostrar, se ve el mensaje de lista vacía
        if (resultado.isEmpty()) {
            if (modoFavoritos) {
                txtVacio.setText("Aún no tienes favoritos");
            } else {
                txtVacio.setText("No se encontraron técnicos");
            }
            txtVacio.setVisibility(View.VISIBLE);
        } else {
            txtVacio.setVisibility(View.GONE);
        }
    }

    // Revisa si el texto buscado está en el nombre, la especialidad o los trabajos
    boolean coincide(Tecnico t, String texto) {
        if (t.nombre.toLowerCase().contains(texto)) {
            return true;
        }
        if (t.especialidad.toLowerCase().contains(texto)) {
            return true;
        }
        for (String trabajo : t.trabajos) {
            if (trabajo.toLowerCase().contains(texto)) {
                return true;
            }
        }
        return false;
    }

    // Abre la pantalla de detalle del técnico
    public void abrirDetalle(Tecnico t) {
        Intent intent = new Intent(this, DetalleActivity.class);
        intent.putExtra("id", t.id);
        startActivity(intent);
    }

    // FloatingContextMenu se abre al mantener presionada una tarjeta (solo en Favoritos)
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (!modoFavoritos) {
            return; // en Inicio no se muestra el menú
        }
        menu.setHeaderTitle(tecnicoSeleccionado.nombre);
        getMenuInflater().inflate(R.menu.menu_contextual, menu);
    }

    // opciones activas al abrir el floatingcontextmenu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.opcion_detalles) {
            // Abre el detalle del local
            abrirDetalle(tecnicoSeleccionado);
            return true;
        } else if (id == R.id.opcion_copiar) {
            // Copia el teléfono al portapapeles
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("telefono", tecnicoSeleccionado.telefono));
            Toast.makeText(this, "Teléfono copiado", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.opcion_quitar) {
            // Quita el local de favoritos y refresca la lista
            tecnicoSeleccionado.favorito = false;
            aplicarFiltros();
            Toast.makeText(this, "Quitado de favoritos", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }
}

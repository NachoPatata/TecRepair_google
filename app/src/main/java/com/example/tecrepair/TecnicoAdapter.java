package com.example.tecrepair;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

// Adapter del RecyclerView: dibuja una tarjeta por cada técnico
public class TecnicoAdapter extends RecyclerView.Adapter<TecnicoAdapter.ViewHolder> {

    MainActivity actividad;
    ArrayList<Tecnico> lista = new ArrayList<>();

    public TecnicoAdapter(MainActivity actividad) {
        this.actividad = actividad;
    }

    // Reemplaza la lista mostrada y refresca la pantalla
    public void actualizar(ArrayList<Tecnico> nuevaLista) {
        lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tecnico, parent, false);
        // Cada tarjeta abre el FloatingContextMenu al mantener presionada
        actividad.registerForContextMenu(vista);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tecnico t = lista.get(position);

        holder.imgFoto.setImageResource(t.foto);
        holder.txtEspecialidad.setText(t.especialidad);
        holder.txtNombre.setText(t.nombre);
        holder.ratingBar.setRating(t.calificacion);
        String promedio = String.valueOf(t.calificacion).replace('.', ',');
        holder.txtResenas.setText(promedio + " (" + t.cantidadResenas + " reseñas)");

        // Etiquetas (chips) con los trabajos que hace el técnico
        holder.chipsTrabajos.removeAllViews();
        for (String trabajo : t.trabajos) {
            Chip chip = (Chip) LayoutInflater.from(actividad)
                    .inflate(R.layout.item_chip_trabajo, holder.chipsTrabajos, false);
            chip.setText(trabajo);
            holder.chipsTrabajos.addView(chip);
        }

        // Etiqueta verde si el local ofrece diagnóstico gratis
        if (t.diagnosticoGratis) {
            Chip chip = (Chip) LayoutInflater.from(actividad)
                    .inflate(R.layout.item_chip_trabajo, holder.chipsTrabajos, false);
            chip.setText("Diagnóstico gratis");
            chip.setChipBackgroundColorResource(R.color.verde_claro);
            holder.chipsTrabajos.addView(chip);
        }

        // Corazón lleno si es favorito, vacío si no
        if (t.favorito) {
            holder.imgCorazon.setImageResource(R.drawable.ic_favorito_lleno);
        } else {
            holder.imgCorazon.setImageResource(R.drawable.ic_favorito);
        }

        // Tocar el corazón agrega o quita de favoritos
        holder.imgCorazon.setOnClickListener(v -> {
            t.favorito = !t.favorito;
            actividad.aplicarFiltros();
        });

        // Tocar la tarjeta abre el detalle del local
        holder.itemView.setOnClickListener(v -> actividad.abrirDetalle(t));

        // Al mantener presionado se guarda cuál tarjeta es, y luego se abre el menú flotante
        holder.itemView.setOnLongClickListener(v -> {
            actividad.tecnicoSeleccionado = t;
            return false; // false = deja que se abra el FloatingContextMenu
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    // Guarda las vistas de cada tarjeta
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFoto, imgCorazon;
        TextView txtEspecialidad, txtNombre, txtResenas;
        ChipGroup chipsTrabajos;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFoto = itemView.findViewById(R.id.imgFoto);
            imgCorazon = itemView.findViewById(R.id.imgCorazon);
            txtEspecialidad = itemView.findViewById(R.id.txtEspecialidad);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtResenas = itemView.findViewById(R.id.txtResenas);
            chipsTrabajos = itemView.findViewById(R.id.chipsTrabajos);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
}

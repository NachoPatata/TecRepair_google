package com.example.tecrepair;

import java.util.ArrayList;

// Modelo simple: guarda los datos de un técnico / taller
public class Tecnico {

    int id;
    String nombre;
    String especialidad;
    String categoria;       // Computación, Telefonía, Impresoras o Consolas
    String[] trabajos;      // Etiquetas de lo que hace (ej: "Armado de PC")
    float calificacion;     // Promedio de estrellas
    int cantidadResenas;
    String telefono;
    double latitud;         // Coordenadas para el mapa
    double longitud;
    String horario;
    boolean estacionamiento;
    boolean garantia;
    String pagos;
    String ubicacion;
    ArrayList<Resena> resenas = new ArrayList<>(); // Reseñas (la primera es de ejemplo)
    int foto;               // Imagen de res/drawable
    boolean diagnosticoGratis; // Ofrece diagnóstico sin costo
    boolean favorito = false;

    public Tecnico(int id, String nombre, String especialidad, String categoria,
                   String[] trabajos, float calificacion, int cantidadResenas,
                   String telefono, double latitud, double longitud, String horario,
                   boolean estacionamiento, boolean garantia, String pagos,
                   String ubicacion, String autorResena, String textoResena, int foto,
                   boolean diagnosticoGratis) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.categoria = categoria;
        this.trabajos = trabajos;
        this.calificacion = calificacion;
        this.cantidadResenas = cantidadResenas;
        this.telefono = telefono;
        this.latitud = latitud;
        this.longitud = longitud;
        this.horario = horario;
        this.estacionamiento = estacionamiento;
        this.garantia = garantia;
        this.pagos = pagos;
        this.ubicacion = ubicacion;
        resenas.add(new Resena(autorResena, "19/09/2026", textoResena, 5));
        this.foto = foto;
        this.diagnosticoGratis = diagnosticoGratis;
    }
}

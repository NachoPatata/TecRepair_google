package com.example.tecrepair;

// Modelo simple: una reseña de un usuario
public class Resena {

    String autor;
    String fecha;
    String texto;
    int estrellas;

    public Resena(String autor, String fecha, String texto, int estrellas) {
        this.autor = autor;
        this.fecha = fecha;
        this.texto = texto;
        this.estrellas = estrellas;
    }
}

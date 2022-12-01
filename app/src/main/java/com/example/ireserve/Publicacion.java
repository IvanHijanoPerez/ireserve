package com.example.ireserve;

import java.util.List;

public class Publicacion {

    String creador, descripcion, direccion, foto, precio, titulo;
    List<String> dias, horas;

    public Publicacion(String creador, String descripcion, String direccion, String foto, String precio, String titulo, List<String> dias, List<String> horas) {
        this.creador = creador;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.foto = foto;
        this.precio = precio;
        this.titulo = titulo;
        this.dias = dias;
        this.horas = horas;
    }
    public String getFoto() {
        return foto;
    }
}

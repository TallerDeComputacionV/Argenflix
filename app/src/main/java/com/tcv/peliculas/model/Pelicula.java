package com.tcv.peliculas.model;

import java.util.List;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private String imagen;
    private String lanzamiento;
    private int duracion;
    private List<String> artistas;
    private double puntuacion;
    private String video_id;
    private String descripcion;

    public String getDirector() {
        return director;
    }

    public String getDescripcion() {
        return descripcion;
    }

    private String director;

    public Pelicula(String titulo,
                    String genero, String imagen) {
        this.titulo = titulo;
        this.genero = genero;
        this.imagen = imagen;
    }



    public String getTitulo() {
        return titulo;
    }

    public String getGenero() {
        return genero;
    }

    public String getImagen()
    {
        return imagen;
    }

    public int getId() {
        return id;
    }

    public String getLanzamiento() {
        return lanzamiento;
    }

    public List<String> getArtistas() {
        return artistas;
    }

    public int getDuracion() {
        return duracion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public String getVideoId() {
        return video_id;
    }
}

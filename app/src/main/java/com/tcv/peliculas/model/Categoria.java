package com.tcv.peliculas.model;

import java.util.ArrayList;

public class Categoria {

    private String titulo;
    private ArrayList<Pelicula> peliculas;

    public String getTitulo() {
        return titulo;
    }
    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }
}

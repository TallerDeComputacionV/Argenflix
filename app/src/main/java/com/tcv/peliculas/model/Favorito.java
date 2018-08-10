package com.tcv.peliculas.model;

public class Favorito {
    private int id;
    private int peliculaId;
    private String titulo;
    private String imagen;
    private String genero;

    public void setId(int id) {
        this.id = id;
    }

    public void setPeliculaId(int peliculaId) {
        this.peliculaId = peliculaId;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public int getPeliculaId() {
        return peliculaId;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public static final String TABLE_NAME = "favoritos";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PELICULA_ID = "pelicula_id";
    public static final String COLUMN_PELICULA_TITULO = "titulo";
    public static final String COLUMN_PELICULA_GENERO = "genero";
    public static final String COLUMN_PELICULA_IMAGEN = "imagen";

    public static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_PELICULA_ID + " INT NOT NULL,"
                    + COLUMN_PELICULA_TITULO + " TEXT NOT NULL,"
                    + COLUMN_PELICULA_GENERO + " TEXT NOT NULL,"
                    + COLUMN_PELICULA_IMAGEN + " TEXT NOT NULL"
                    + ")";
}

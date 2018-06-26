package com.tcv.peliculas.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.database.DBHelper;
import com.tcv.peliculas.model.Pelicula;

public class PeliculaDetailsActivity extends AppCompatActivity {
    private Pelicula pelicula;
    private Boolean esFavorita;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pelicula_details);

        inicializarToolbar();

        Bundle args = getIntent().getExtras();
        pelicula = new Gson().fromJson(args.getString("pelicula"), Pelicula.class);

        dbHelper = new DBHelper(PeliculaDetailsActivity.this);
        esFavorita = dbHelper.esFavorito(pelicula);

        TextView titulo = findViewById(R.id.titulo);
        TextView genero = findViewById(R.id.genero);
        TextView director = findViewById(R.id.director);
        TextView duracion = findViewById(R.id.duracion);
        TextView lanzamiento = findViewById(R.id.lanzamiento);
        TextView puntuacion = findViewById(R.id.puntuacion);
        TextView artistas = findViewById(R.id.artistas_principales);
        TextView descripcion = findViewById(R.id.descripcion);
        ImageView imagen = findViewById(R.id.imagen);
        Button btnVerPelicula = findViewById(R.id.btnVerPelicula);

        titulo.setText(pelicula.getTitulo());
        genero.setText(pelicula.getGenero());
        director.setText(pelicula.getDirector());
        duracion.setText(getString(R.string.duracion, String.valueOf(pelicula.getDuracion())));
        lanzamiento.setText(pelicula.getLanzamiento());
        puntuacion.setText(String.valueOf(pelicula.getPuntuacion()));
        artistas.setText(pelicula.getArtistas().toString());
        descripcion.setText(pelicula.getDescripcion());

        int drawable = PeliculaDetailsActivity.this.getResources().getIdentifier(pelicula.getImagen(), "drawable",
                PeliculaDetailsActivity.this.getPackageName());
        imagen.setImageResource(drawable);

        View.OnClickListener loadPeliculaViewer = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeliculaDetailsActivity.this, PeliculaViewerActivity.class);
                intent.putExtra("videoId", pelicula.getVideoId());
                PeliculaDetailsActivity.this.startActivity(intent);
            }
        };

        imagen.setOnClickListener(loadPeliculaViewer);
        btnVerPelicula.setOnClickListener(loadPeliculaViewer);
    }


    private void inicializarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_info_pelicula, menu);
        MenuItem mnuFavorito;
        if (esFavorita) {
            mnuFavorito = menu.findItem(R.id.favorito).setIcon(R.drawable.ic_star_full);
        } else {
            mnuFavorito = menu.findItem(R.id.favorito).setIcon(R.drawable.ic_star_empty);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        dbHelper = new DBHelper(PeliculaDetailsActivity.this);
        MenuItem mnuFavorito;

        switch (item.getItemId()) {
            case R.id.favorito:
                if (esFavorita) {
                    dbHelper.quitarFavorito(pelicula);
                    item.setIcon(R.drawable.ic_star_empty);
                    Toast.makeText(this, getString(R.string.quitadoDeFavoritos, pelicula.getTitulo()), Toast.LENGTH_SHORT).show();
                } else {
                    dbHelper.agregarFavorito(pelicula);
                    item.setIcon(R.drawable.ic_star_full);
                    Toast.makeText(this, getString(R.string.agregadoAFavoritos, pelicula.getTitulo()), Toast.LENGTH_SHORT).show();
                }
                esFavorita = !esFavorita;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}


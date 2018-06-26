package com.tcv.peliculas.view;

import android.Manifest;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.categorias.CategoriaAdapter;
import com.tcv.peliculas.model.Categoria;
import com.tcv.peliculas.model.Pelicula;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        inicializarNavHeader();
        getCategorias();
//        inicializarBuscador();
    }

//    private void inicializarBuscador() {
//        SearchView searchBar = findViewById(R.id.search);
//
//        //ADAPTER
//        BuscadorAdapter adapter = new BuscadorAdapter(getPeliculas());
//        rv.setAdapter(adapter);
//        //SEARCHBAR TEXT CHANGE LISTENER
//        searchBar.addTextChangeListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                //SEARCH FILTER
//                adapter.getFilter().filter(charSequence);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//    }
//
//    private ArrayList<Pelicula> getPeliculas() {
//        ArrayList<Pelicula> peliculas = new ArrayList<Pelicula>();
//        peliculas.add(new Pelicula("titulo1", "accion1", "imagen1"));
//        peliculas.add(new Pelicula("titulo2", "accion1", "imagen1"));
//        peliculas.add(new Pelicula("titulo3", "accion1", "imagen1"));
//
//        return peliculas;
//    }


    private void inicializarNavHeader() {
        View view = navigationView.getHeaderView(0);
        TextView tvUsername = view.findViewById(R.id.tvUsername);

        if (getIntent().hasExtra("usuario")) {
            tvUsername.setText(capitalize(getIntent().getExtras().getString("usuario")));
        } else {
            SharedPreferences sp = CategoriasActivity.this.getSharedPreferences(
                    getString(R.string.app_name), Context.MODE_PRIVATE);
            tvUsername.setText(capitalize(sp.getString("usuario", "")));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriasActivity.this,
                        PerfilActivity.class);
                CategoriasActivity.this.startActivity(intent);
            }
        });
    }

    private String capitalize(String usuario) {
        String capitalized = usuario.substring(0, 1).toUpperCase() + usuario.substring(1);
        return capitalized;
    }


    private void getCategorias() {
        ApiClient.getClient(this).getCategorias().enqueue(new Callback<List<Categoria>>() {
            @Override
            public void onResponse(Call<List<Categoria>> call, Response<List<Categoria>> response) {
                llenarListaCategorias(response.body());
            }

            @Override
            public void onFailure(Call<List<Categoria>> call, Throwable throwable) {
                Toast.makeText(CategoriasActivity.this, "Ocurrió un error al querer obtener la lista de peliculas.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarListaCategorias(List<Categoria> categorias) {
        RecyclerView rv = findViewById(R.id.rvCategorias);
        rv.setHasFixedSize(true);

        CategoriaAdapter adapter = new CategoriaAdapter(categorias, CategoriasActivity.this);

        rv.setLayoutManager(new LinearLayoutManager(CategoriasActivity.this, LinearLayoutManager.VERTICAL, false));

        rv.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_favoritos:
                Intent intentFavoritos = new Intent(this, FavoritosActivity.class);
                startActivity(intentFavoritos);
                break;
            case R.id.nav_buscador:
                Intent intentBuscador = new Intent(this, BuscadorActivity.class);
                startActivity(intentBuscador);
                break;
            case R.id.nav_cerrar_sesion:
                cerrarSesion();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void cerrarSesion() {

        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.app_name),
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}

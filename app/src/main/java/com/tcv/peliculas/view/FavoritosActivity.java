package com.tcv.peliculas.view;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.favoritos.FavoritosAdapter;
import com.tcv.peliculas.database.DBHelper;
import com.tcv.peliculas.model.Favorito;
import com.tcv.peliculas.model.Pelicula;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {
    private FavoritosAdapter favAdapter;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        inicializarToolbar();
        getFavoritos();
    }

    private void getFavoritos() {
        DBHelper dbHelper = new DBHelper(this);

        favAdapter = new FavoritosAdapter(dbHelper.getFavoritos(), this);

        RecyclerView rvFavoritos = findViewById(R.id.rvFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvFavoritos.setAdapter(favAdapter);

        if (favAdapter.getItemCount() == 0) {
            TextView tvNoHayFavoritos = findViewById(R.id.tvSinFavoritos);
            tvNoHayFavoritos.setVisibility(View.VISIBLE);
            rvFavoritos.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoritos();
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
        getMenuInflater().inflate(R.menu.menu_favoritos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (favAdapter.getItemCount() == 0) {
            Toast.makeText(this, R.string.noHayFavoritos, Toast.LENGTH_LONG).show();
            return true;
        }

        dbHelper = new DBHelper(this);
        switch (item.getItemId()) {
            case R.id.btnEliminarFavoritos: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.limpiarFavoritos);
                builder.setMessage(R.string.confirmarEliminacionFavoritos);
                builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                            dbHelper.eliminarFavoritos();
                            onResume();
                            dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


package com.tcv.peliculas.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.controller.buscador.BuscadorAdapter;
import com.tcv.peliculas.model.Pelicula;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscadorActivity extends AppCompatActivity {
    private BuscadorAdapter buscadorAdapter;
    private ListView lvPeliculas;
    private TextView tvNoHayPeliculas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        lvPeliculas = findViewById(R.id.lvPeliculas);
        tvNoHayPeliculas = findViewById(R.id.tvNoHayPeliculas);

        inicializarToolbar();
        getPeliculas();
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

    private void getPeliculas() {
        ApiClient.getClient(this).getPeliculas().enqueue(new Callback<List<Pelicula>>() {
            @Override
            public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                buscadorAdapter = new BuscadorAdapter(BuscadorActivity.this, response.body());
                lvPeliculas.setAdapter(buscadorAdapter);
                lvPeliculas.setEmptyView(tvNoHayPeliculas);
            }

            @Override
            public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                Toast.makeText(BuscadorActivity.this, getString(R.string.errorAlObtenerPeliculas),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem mSearch = menu.findItem(R.id.buscar);

        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Buscar");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscadorAdapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

package com.tcv.peliculas.controller.favoritos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.api.ApiClient;
import com.tcv.peliculas.model.Favorito;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.PeliculaDetailsActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritosViewHolder> {

    private List<Favorito> favoritos;
    private Context context;

    public FavoritosAdapter(List<Favorito> favoritos, Context context) {
        this.favoritos = favoritos;
        this.context = context;
    }

    @Override
    public FavoritosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_favorito, parent, false);
        return new FavoritosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritosViewHolder holder, int position) {
        ((FavoritosAdapter.FavoritosViewHolder) holder).bind(favoritos.get(position));
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }

    class FavoritosViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvGenero;
        ImageView imagen;

        FavoritosViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvGenero = itemView.findViewById(R.id.tvGenero);
            imagen = itemView.findViewById(R.id.imagen);
        }

        public void bind(final Favorito favorito) {
            tvTitulo.setText(favorito.getTitulo());
            tvGenero.setText(favorito.getGenero());

            int drawable = context.getResources().getIdentifier(favorito.getImagen(), "drawable",
                    context.getPackageName());
            imagen.setImageResource(drawable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPelicula(favorito.getPeliculaId());
                }

                private void getPelicula(int peliculaId) {
                    ApiClient.getClient(context).getPeliculas()
                            .enqueue(new Callback<List<Pelicula>>() {
                                @Override
                                public void onResponse(Call<List<Pelicula>> call, Response<List<Pelicula>> response) {
                                    List<Pelicula> peliculas = response.body();
                                    for (Pelicula pelicula : peliculas) {
                                        if (pelicula.getId() == favorito.getPeliculaId()) {
                                            Intent intent = new Intent(context, PeliculaDetailsActivity.class);
                                            intent.putExtra("pelicula", new Gson().toJson(pelicula));
                                            context.startActivity(intent);
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<List<Pelicula>> call, Throwable throwable) {
                                    Toast.makeText(context, context.getString(R.string.errorAlObtenerPeliculas), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }
}

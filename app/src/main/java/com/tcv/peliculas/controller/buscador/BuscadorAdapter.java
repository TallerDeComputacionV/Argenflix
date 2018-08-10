package com.tcv.peliculas.controller.buscador;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.PeliculaDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class BuscadorAdapter extends BaseAdapter implements Filterable {

    private List<Pelicula> peliculasTodas = null;
    private List<Pelicula> peliculasFiltradas = null;
    private LayoutInflater layoutInflater;
    private ItemFilter itemFilter = new ItemFilter();
    private Context context;

    public BuscadorAdapter(Context context, List<Pelicula> peliculas) {
        this.peliculasFiltradas = peliculas;
        this.peliculasTodas = peliculas;
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return peliculasFiltradas.size();
    }

    public Object getItem(int position) {
        return peliculasFiltradas.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.card_favorito, null);
        }

        TextView tvTitulo = convertView.findViewById(R.id.tvTitulo);
        TextView tvGenero = convertView.findViewById(R.id.tvGenero);
        ImageView imagen = convertView.findViewById(R.id.imagen);

        final Pelicula pelicula = peliculasFiltradas.get(position);
        tvTitulo.setText(String.format("%s (%s)", pelicula.getTitulo(), pelicula.getLanzamiento()));
        tvGenero.setText(pelicula.getGenero());
        int drawable = context.getResources().getIdentifier(peliculasFiltradas.get(position).getImagen(), "drawable",
                context.getPackageName());
        imagen.setImageResource(drawable);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PeliculaDetailsActivity.class);
                intent.putExtra("pelicula", new Gson().toJson(pelicula));
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public Filter getFilter() {
        return itemFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String texto = constraint.toString().toLowerCase();
            FilterResults resultados = new FilterResults();

            final List<Pelicula> list = peliculasTodas;

            int count = list.size();
            final List<Pelicula> listaFiltrada = new ArrayList<Pelicula>(count);

            Pelicula pelicula;

            for (int i = 0; i < count; i++) {
                pelicula = list.get(i);
                if (normalizar(pelicula.getTitulo()).contains(texto)) {
                    listaFiltrada.add(pelicula);
                }
            }

            resultados.values = listaFiltrada;
            resultados.count = listaFiltrada.size();

            return resultados;
        }

        protected String normalizar(String texto) {
            texto = texto.toLowerCase().replaceAll("á", "a").replaceAll("é","e").
                    replaceAll("í","i").replaceAll("ó","o").
                    replaceAll("ú","u");
            return texto;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            peliculasFiltradas = (ArrayList<Pelicula>) results.values;
            notifyDataSetChanged();
        }
    }
}
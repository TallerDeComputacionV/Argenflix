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
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.tcv.peliculas.R;
import com.tcv.peliculas.model.Pelicula;
import com.tcv.peliculas.view.PeliculaDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class BuscadorAdapter extends BaseAdapter implements Filterable {

    private List<Pelicula> originalData = null;
    private List<Pelicula> filteredData = null;
    private LayoutInflater mInflater;
    private ItemFilter mFilter = new ItemFilter();
    private Context context;

    public BuscadorAdapter(Context context, List<Pelicula> data) {
        this.filteredData = data;
        this.originalData = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
        return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.card_favorito, null);

            holder = new ViewHolder();
            holder.tvTitulo =  convertView.findViewById(R.id.tvTitulo);
            holder.tvGenero =  convertView.findViewById(R.id.tvGenero);
            holder.imagen =  convertView.findViewById(R.id.imagen);

            // Bind the data efficiently with the holder.

            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }

        // If weren't re-ordering this you could rely on what you set last time
        final Pelicula pelicula = filteredData.get(position);
        holder.tvTitulo.setText(String.format("%s (%s)",pelicula.getTitulo(),pelicula.getLanzamiento()));
        holder.tvGenero.setText(pelicula.getGenero());
        int drawable = context.getResources().getIdentifier(filteredData.get(position).getImagen(), "drawable",
                context.getPackageName());
        holder.imagen.setImageResource(drawable);

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

    static class ViewHolder {
        TextView tvTitulo;
        TextView tvGenero;
        ImageView imagen;
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<Pelicula> list = originalData;

            int count = list.size();
            final List<Pelicula> nlist = new ArrayList<Pelicula>(count);

            Pelicula filterableString;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getTitulo().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<Pelicula>) results.values;
            notifyDataSetChanged();
        }

    }
}
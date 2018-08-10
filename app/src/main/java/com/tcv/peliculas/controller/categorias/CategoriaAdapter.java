package com.tcv.peliculas.controller.categorias;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tcv.peliculas.R;
import com.tcv.peliculas.controller.peliculas.ListaPeliculasAdapter;
import com.tcv.peliculas.model.Categoria;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private List<Categoria> categorias;
    private Context context;

    public CategoriaAdapter(List<Categoria> categorias, Context context)
    {
        this.categorias = categorias;
        this.context = context;
    }

    @Override
    public CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.card_categoria, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoriaViewHolder holder, int position) {
        holder.tvTitulo.setText(categorias.get(position).getTitulo());
        ListaPeliculasAdapter adapter = new ListaPeliculasAdapter(categorias.get(position).getPeliculas(), context);
        holder.rvPeliculas.setHasFixedSize(true);
        holder.rvPeliculas.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvPeliculas.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

     class CategoriaViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        RecyclerView rvPeliculas;

        CategoriaViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            rvPeliculas = itemView.findViewById(R.id.rvPeliculas);
        }
}}
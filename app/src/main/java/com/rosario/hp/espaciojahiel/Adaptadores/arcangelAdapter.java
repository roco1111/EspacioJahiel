package com.rosario.hp.espaciojahiel.Adaptadores;


import android.content.Context;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rosario.hp.espaciojahiel.Entidades.arcangel;

import com.rosario.hp.espaciojahiel.R;


import java.util.ArrayList;
import java.util.List;

public class arcangelAdapter extends RecyclerView.Adapter<arcangelAdapter.HolderArcangel> {

    private Context context;
    private List<arcangel> items;
    private Integer dia;
    private Boolean visible;


    public arcangelAdapter(List<arcangel> items, Context context, Integer dia, Boolean visible) {
        this.context = context;
        this.items = items;
        this.dia = dia;
        this.visible = visible;
    }

    @Override
    public HolderArcangel onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_arcangel,viewGroup,false);
        context = v.getContext();
        return new HolderArcangel(v);
    }

    public void onBindViewHolder(HolderArcangel holder, int position) {
        //holder.imagenMensaje.setText(items.);//ver con firebase
        holder.textoMensaje.setText(items.get(position).getMensaje());
        holder.textoTitulo.setText(items.get(position).getTitulo());
        if(!visible)
        {
            holder.configuracion.setVisibility(View.INVISIBLE);
        }

    }


    public static class HolderArcangel extends RecyclerView.ViewHolder {

        public TextView textoTitulo;
        public TextView textoMensaje;
        public ImageView imagen;
        public ImageButton configuracion;
        public HolderArcangel(View v) {
            super(v);
            textoTitulo = v.findViewById(R.id.textViewTitulo);
            textoMensaje = v.findViewById(R.id.textViewtexto);
            imagen = v.findViewById(R.id.imageViewArcangel);
            configuracion = v.findViewById(R.id.buttonConfiguracion);

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

   }


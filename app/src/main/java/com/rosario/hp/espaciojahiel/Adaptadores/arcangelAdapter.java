package com.rosario.hp.espaciojahiel.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.espaciojahiel.Entidades.arcangel;
import com.rosario.hp.espaciojahiel.Entidades.espacioAmigo;
import com.rosario.hp.espaciojahiel.Entidades.imagen;
import com.rosario.hp.espaciojahiel.GlideApp;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.activity_imagen;
import com.rosario.hp.espaciojahiel.activity_mensaje;

import java.util.ArrayList;
import java.util.List;

public class arcangelAdapter extends RecyclerView.Adapter<arcangelAdapter.HolderArcangel> {

    private Context context;
    private List<arcangel> items;
    private Integer dia;


    public arcangelAdapter(List<arcangel> items, Context context, Integer dia) {
        this.context = context;
        this.items = items;
        this.dia = dia;
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

    }


    public static class HolderArcangel extends RecyclerView.ViewHolder {

        public TextView textoTitulo;
        public TextView textoMensaje;
        public ImageView imagen;
        public HolderArcangel(View v) {
            super(v);
            textoTitulo = v.findViewById(R.id.textViewTitulo);
            textoMensaje = v.findViewById(R.id.textViewtexto);
            imagen = v.findViewById(R.id.imageViewArcangel);

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

   }


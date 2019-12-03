package com.rosario.hp.espaciojahiel.Adaptadores;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.espaciojahiel.GlideApp;
import com.rosario.hp.espaciojahiel.R;

import com.rosario.hp.espaciojahiel.Entidades.espacioAmigo;
import com.rosario.hp.espaciojahiel.WebActivity;

import java.util.ArrayList;

public class espacioAdapter extends BaseAdapter
        implements ItemClickListener5{
    private Context context;
    private ArrayList<espacioAmigo> Espacios;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private ImageView imagen_espacio;
    private Bitmap loadedImage;
    private String id;
    private ArrayList<Bitmap> images;
    private FirebaseAuth mAuth;

    public espacioAdapter(Context context, ArrayList<espacioAmigo> Espacios, ArrayList<Bitmap> images) {
        this.context = context;
        this.Espacios = Espacios;
        this.images = images;
    }

    @Override
    public int getCount() {
        return Espacios.size();
    }
    @Override
    public espacioAmigo getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_espacio, viewGroup, false);
            mAuth = FirebaseAuth.getInstance();
        }


        imagen_espacio =  view.findViewById(R.id.imagen_espacio);
        ImageView estrella = view.findViewById(R.id.imageViewDestacado);
        TextView nombre_evento = view.findViewById(R.id.nombre_evento);
        TextView contacto = view.findViewById(R.id.contacto);
        ImageView ver_pagina = view.findViewById(R.id.imageViewpagina);
        ImageView face = view.findViewById(R.id.imageButtonFace);
        ImageView mail = view.findViewById(R.id.mail);
        ImageView instagram = view.findViewById(R.id.instagram);


        nombre_evento.setText(Espacios.get(position).getNombre());
        contacto.setText("Contacto: " + Espacios.get(position).getContacto());
        id = Espacios.get(position).getId();

        if(Espacios.get(position).getDestacado().equals("1")){
            estrella.setVisibility(View.VISIBLE);
            view.setBackgroundColor(context.getResources().getColor(R.color.colorRippleMini));
        }else{
            estrella.setVisibility(View.INVISIBLE);
            view.setBackgroundColor(context.getResources().getColor(R.color.secondaryText));
        }

        imagen_espacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_link;

                ls_link = Espacios.get(position).getPagina();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", ls_link);
                editor.apply();

                Intent intent = new Intent(context, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();
            }
        });

        ver_pagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_link;

                ls_link = Espacios.get(position).getPagina();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", ls_link);
                editor.apply();

                Intent intent = new Intent(context, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_link;

                ls_link = Espacios.get(position).getInstagram();
                String uri1 = "https://instagram.com/_u/" + ls_link;
                String uri2 = "https://instagram.com/" + ls_link;

                Uri uri = Uri.parse(uri1);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    context.startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(uri2)));
                }
            }
        });

        face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_link;

                ls_link = Espacios.get(position).getFace();

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", ls_link);
                editor.apply();

                Intent intent = new Intent(context, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Espacios.get(position).getMail()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
                //Recordad que la barra invertida más "n" es un salto de linea "n" así, escribiremos el email con varios saltos de linea.
                String textoApp = "Envio un email";
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                emailIntent.setType("message/rfc822");
                //Damos la opción al usuario que elija desde que app enviamos el email.
                context.startActivity(emailIntent);
            }
        });

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();


        String mChild = "espacios/" + id  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        GlideApp.with(context)
                .load(filepath)
                .into(imagen_espacio);


        return view;
    }
    @Override
    public void onItemClick(View view, int position) {

    }


}

interface ItemClickListener5 {
    void onItemClick(View view, int position);
}


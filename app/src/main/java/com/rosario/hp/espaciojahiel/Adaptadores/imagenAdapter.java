package com.rosario.hp.espaciojahiel.Adaptadores;

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
import com.rosario.hp.espaciojahiel.Entidades.espacioAmigo;
import com.rosario.hp.espaciojahiel.Entidades.imagen;
import com.rosario.hp.espaciojahiel.GlideApp;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.activity_imagen;
import com.rosario.hp.espaciojahiel.activity_mensaje;

import java.util.ArrayList;
import java.util.List;

public class imagenAdapter extends BaseAdapter
        implements ItemClickListener1 {

    private Context context;
    private ArrayList<imagen> Imagenes;
    private ArrayList<Bitmap> images;
    ImageView imagen;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;

    public imagenAdapter(Context context, ArrayList<imagen> Imagenes, ArrayList<Bitmap> images) {
        this.context = context;
        this.Imagenes = Imagenes;
        this.images = images;
    }
    @Override
    public int getCount() {
        return Imagenes.size();
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
            view = inflater.inflate(R.layout.item_imagen, viewGroup, false);
            mAuth = FirebaseAuth.getInstance();
        }

        imagen =  view.findViewById(R.id.imagen);
        TextView nombre = view.findViewById(R.id.nombre_imagen);
        //TextView descripcion = view.findViewById(R.id.descripcion_imagen);

        nombre.setText(Imagenes.get(position).getNombre());
        //descripcion.setText(Imagenes.get(position).getDescripcion());

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String id = Imagenes.get(position).getId();
        String mChild = "imagenes/" + id  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        GlideApp.with(context)
                .load(filepath)
                .into(imagen);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("id", Imagenes.get(position).getId());
                editor.apply();

                Intent intent = new Intent(context, activity_imagen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();}
            });

        return view;
    }
    @Override
    public void onItemClick(View view, int position) {

    }

}


interface ItemClickListener1 {
    void onItemClick(View view, int position);
}

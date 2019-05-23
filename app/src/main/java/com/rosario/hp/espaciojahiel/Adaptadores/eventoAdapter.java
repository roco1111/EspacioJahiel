package com.rosario.hp.espaciojahiel.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.espaciojahiel.GlideApp;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.Entidades.evento;
import com.rosario.hp.espaciojahiel.WebActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class eventoAdapter extends RecyclerView.Adapter<eventoAdapter.HolderEvento>
        implements ItemClickListener3 {

    private Context context;
    private ArrayList<evento> items;
    private ArrayList<Bitmap> images;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;


    public eventoAdapter(ArrayList<evento> items, Context context) {
        this.context = context;
        this.items = items;

    }

    @Override
    public HolderEvento onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_eventos,viewGroup,false);
        mAuth = FirebaseAuth.getInstance();

        return new HolderEvento(v,this);
    }

    public void onBindViewHolder(HolderEvento holder, int position) {
        Date date = null ;
        String fecha_alta;
        String fecha_inicio;
        Integer dias = 5;


        //es nuevo si no paso 5 dias desde la publicacion
        fecha_alta = items.get(position).getFecha_alta();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
             date = format.parse(fecha_alta);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date); // Configuramos la fecha que se recibe

        calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0

        Calendar c = Calendar.getInstance();

        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        if (c.compareTo(calendar)< 0){
            holder.imagenNuevo.setVisibility(View.VISIBLE);
            holder.txtNuevo.setVisibility(View.VISIBLE);

        }else{
            holder.imagenNuevo.setVisibility(View.INVISIBLE);
            holder.txtNuevo.setVisibility(View.INVISIBLE);

        }
        //ver si finalizo

        fecha_inicio = items.get(position).getFecha();
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format2.parse(fecha_inicio);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar2 = Calendar.getInstance();

        calendar2.setTime(date); // Configuramos la fecha que se recibe

        calendar2.add(Calendar.DAY_OF_YEAR, 0);  // numero de días a añadir, o restar en caso de días<0

        Calendar c2 = Calendar.getInstance();

        System.out.println("Current time => " + c2.getTime());
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        if (c2.compareTo(calendar2)< 0){
            holder.finalizado.setVisibility(View.INVISIBLE);

        }else{
            holder.finalizado.setVisibility(View.VISIBLE);

        }



        //hora
        String ls_fecha = items.get(position).getFecha();
        SimpleDateFormat inDateFmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outDateFmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = inDateFmt.parse(ls_fecha);
            ls_fecha= outDateFmt.format(date);
        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }
        String ls_hora = items.get(position).getHora();
        if(!ls_hora.equals("")){
        ls_fecha = ls_fecha + " - " + ls_hora + "hs";}
        holder.textoFecha.setText(ls_fecha);
        holder.textoNombre.setText(items.get(position).getNombre());
        holder.textoLugar.setText(items.get(position).getLugar());
        String id = items.get(position).getId();

        if(items.get(position).getDestacado().equals("1")){
            holder.estrella.setVisibility(View.VISIBLE);
        }else{
            holder.estrella.setVisibility(View.INVISIBLE);
        }

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();


        String mChild = "eventos/" + id  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        GlideApp.with(context)
                .load(filepath)
                .fitCenter()
                .into(holder.imagenEvento);


        final String ls_link = items.get(position).getLink();
        final Integer posicion = position;

        holder.btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", ls_link);
                editor.apply();

                Intent intent = new Intent(context, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();
            }});

        holder.imagenEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.popup_imagen, null);

                // Initialize a new instance of popup window
                final PopupWindow mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if (Build.VERSION.SDK_INT >= 21) {
                    mPopupWindow.setElevation(5.0f);
                }
                ImageView imgLocal_grande = customView.findViewById(R.id.imgLocal);

                GlideApp.with(context)
                        .load(filepath)
                        .into(imgLocal_grande);

                //imgLocal_grande.setImageBitmap(Bitmap.createScaledBitmap(images.get(posicion), 200 , 200 , false));


                imgLocal_grande.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });


                mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

    }
    @Override
    public void onItemClick(View view, int position) {

    }

    public static class HolderEvento extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public ImageView imagenEvento;
        public TextView textoFecha;
        public TextView textoNombre;
        public TextView textoLugar;
        public ImageView txtNuevo;
        public ImageView imagenNuevo;
        public ImageView estrella;
        public Button btn_ver;
        public  ImageView finalizado;

        public ItemClickListener3 listener;
        public HolderEvento(View v, ItemClickListener3 listener) {
            super(v);
            imagenEvento = v.findViewById(R.id.imageEvento);
            textoFecha = v.findViewById(R.id.textViewFecha);
            textoNombre = v.findViewById(R.id.textViewNombre);
            textoLugar = v.findViewById(R.id.textViewLugar);
            txtNuevo = v.findViewById(R.id.txt_nuevo);
            imagenNuevo = v.findViewById(R.id.imageNuevo);
            estrella = v.findViewById(R.id.imageViewDestacado);
            btn_ver = v.findViewById(R.id.button_ver);
            finalizado = v.findViewById(R.id.finalizado);
            this.listener = listener;

            v.setOnClickListener(this);
        }
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        if (items != null)
            return items.size();
        return 0;
    }
}
interface ItemClickListener3 {
    void onItemClick(View view, int position);
}

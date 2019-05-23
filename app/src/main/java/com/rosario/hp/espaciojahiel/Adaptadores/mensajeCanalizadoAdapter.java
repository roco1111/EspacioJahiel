package com.rosario.hp.espaciojahiel.Adaptadores;

import android.content.Context;
import com.rosario.hp.espaciojahiel.Entidades.mensajeUsuario;
import com.rosario.hp.espaciojahiel.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.rosario.hp.espaciojahiel.activity_mensaje;
import com.rosario.hp.espaciojahiel.activity_mis_mensajes;

import java.util.List;

public class mensajeCanalizadoAdapter extends RecyclerView.Adapter<mensajeCanalizadoAdapter.HolderMensajeCanalizado>
        implements ItemClickListener2 {

    private Context context;
    private List<mensajeUsuario> items;


    public mensajeCanalizadoAdapter(List<mensajeUsuario> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public HolderMensajeCanalizado onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_mensaje,viewGroup,false);
        context = v.getContext();
        return new HolderMensajeCanalizado(v,this);
    }

    public void onBindViewHolder(HolderMensajeCanalizado holder, int position) {
        //holder.imagenMensaje.setText(items.);//ver con firebase
        holder.textoMensaje.setText(items.get(position).getMensaje());
        holder.textoFecha.setText(items.get(position).getFecha());
        final String ls_mensaje = items.get(position).getId_mensaje();
        final String posicion = String.valueOf(position);
        holder.btn_ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("id", ls_mensaje);
                editor.putString("position",posicion);
                editor.apply();

                Intent intent = new Intent(context, activity_mis_mensajes.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                editor.commit();
            }});


        switch (mod(position,12)) {
            case 0:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje12));
                break;
            case 1:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje1));
                break;
            case 2:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje2));
                break;
            case 3:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje3));
                break;
            case 4:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje4));
                break;
            case 5:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje5));
                break;
            case 6:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje6));
                break;
            case 7:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje7));
                break;
            case 8:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje8));
                break;
            case 9:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje9));
                break;
            case 10:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje10));
                break;
            case 11:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje11));
                break;
            case 12:
                holder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje12));
                break;

        }

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public static class HolderMensajeCanalizado extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView textoFecha;
        public TextView textoMensaje;
        public ImageView imagen;
        public Button btn_ver;

        public ItemClickListener2 listener;
        public HolderMensajeCanalizado(View v, ItemClickListener2 listener) {
            super(v);
            textoFecha = v.findViewById(R.id.textViewFecha);
            textoMensaje = v.findViewById(R.id.textViewmensaje);
            imagen = v.findViewById(R.id.imageMensaje);
            btn_ver = v.findViewById(R.id.buttonVer);

            this.listener = listener;

            v.setOnClickListener(this);
        }
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

}
interface ItemClickListener2 {
    void onItemClick(View view, int position);
}

package com.rosario.hp.espaciojahiel.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rosario.hp.espaciojahiel.R;

import java.util.List;
import com.rosario.hp.espaciojahiel.Entidades.notificacion;

public class notificacionesAdapter extends RecyclerView.Adapter<notificacionesAdapter.NotificacionesViewHolder>
        implements ItemClickListener5 {
    private List<notificacion> items;

    private Context context;
    private String ls_id_pedido;

    public notificacionesAdapter(List<notificacion> items, Context context) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotificacionesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list_notification, viewGroup, false);
        context = v.getContext();
        return new NotificacionesViewHolder(v, this);
    }

    @Override
    public void onItemClick(View view, int position) {

        //ls_id_pedido = items.get(position).getId_pedido();
        //Intent intent3 = new Intent(view.getContext(),mainPedidos.class);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor2 = settings.edit();
        editor2.putString("id_pedido", ls_id_pedido);
        editor2.apply();
        editor2.commit();

    }

    public void onBindViewHolder(NotificacionesViewHolder viewHolder, int i) {

        viewHolder.id_notificacion.setText( items.get(i).getId());
        viewHolder.titulo.setText(items.get(i).getTitulo());
        viewHolder.texto.setText(items.get(i).getTexto());
        viewHolder.fecha.setText(items.get(i).getFecha());
        viewHolder.hora.setText(items.get(i).getTiempo());

        Integer numero = (int) (Math.random() * 12) + 1;

        switch (numero) {
            case 1:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje1));
                break;
            case 2:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje2));
                break;
            case 3:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje3));
                break;
            case 4:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje4));
                break;
            case 5:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje5));
                break;
            case 6:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje6));
                break;
            case 7:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje7));
                break;
            case 8:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje8));
                break;
            case 9:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje9));
                break;
            case 10:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje10));
                break;
            case 11:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje11));
                break;
            case 12:
                viewHolder.imagen.setImageDrawable(context.getResources().getDrawable(R.drawable.mensaje12));
                break;

        }


    }

    public static class NotificacionesViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        TextView id_notificacion;
        TextView titulo;
        TextView texto;
        TextView fecha;
        TextView hora;
        ImageView imagen;

        public ItemClickListener5 listener;

        public NotificacionesViewHolder(View v, ItemClickListener5 listener) {
            super(v);

            id_notificacion = v.findViewById(R.id.tv_codigo);
            titulo =  v.findViewById(R.id.tv_title);
            texto =  v.findViewById(R.id.tv_description);
            fecha = v.findViewById(R.id.fecha) ;
            hora = v.findViewById(R.id.hora);
            imagen = v.findViewById(R.id.imageNotificacion);
            this.listener = listener;

            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {

            listener.onItemClick(v, getAdapterPosition());
            //Intent intent4 = new Intent(v.getContext(),mainPedidos.class);
            //v.getContext().startActivity(intent4);

        }
    }
    public String getIdNotificacion( int i){
        return items.get(i).getId();
    }

}

interface ItemClickListener7 {
    void onItemClick(View view, int position);
}


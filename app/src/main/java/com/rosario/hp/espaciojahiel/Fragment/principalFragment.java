package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosario.hp.espaciojahiel.MainActivity;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.WebActivity;
import com.rosario.hp.espaciojahiel.activity_arcangel;
import com.rosario.hp.espaciojahiel.activity_arcangel_general;
import com.rosario.hp.espaciojahiel.activity_espacio;
import com.rosario.hp.espaciojahiel.activity_evento;
import com.rosario.hp.espaciojahiel.activity_imagen;
import com.rosario.hp.espaciojahiel.activity_imagen_principal;
import com.rosario.hp.espaciojahiel.activity_mensaje;
import com.rosario.hp.espaciojahiel.activity_videos;


public class principalFragment extends Fragment {
    public static final String ARG_ARTICLES_NUMBER = "principal";

    TextView mensaje_canalizado;
    TextView mi_arcangel;
    TextView eventos;
    TextView videos_meditacion;
    TextView fondos_pantalla;
    TextView tu_arcangel;
    TextView espacio;
    String ls_nombre;
    Activity activity;
    private int posicion;
    private String posicion_string;

    public principalFragment(){
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.principal, container, false);
        mensaje_canalizado =  rootView.findViewById(R.id.mensaje_canalizado);
        mi_arcangel =  rootView.findViewById(R.id.mi_arcangel);
        eventos = rootView.findViewById(R.id.eventos);
        videos_meditacion = rootView.findViewById(R.id.videos_meditacion);
        fondos_pantalla = rootView.findViewById(R.id.fondos_pantalla);
        espacio = rootView.findViewById(R.id.espacios);
        tu_arcangel = rootView.findViewById(R.id.tu_arcangel);

        activity = getActivity();

        setHasOptionsMenu(true);

        Resources res = getActivity().getApplicationContext().getResources();

        final Bundle args1 = new Bundle();


        mensaje_canalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_mensaje.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        mi_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), activity_arcangel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);

            }
        });

        tu_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_arcangel_general.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);

            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_evento.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        videos_meditacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_videos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });
        espacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_espacio.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });

        fondos_pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), activity_imagen_principal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);


            }
        });



        return rootView;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mensajes_guardados:
                getActivity().getActionBar().setTitle(item.getTitle());
                break;

        }
        return false;
    }

}

package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.WebActivity;
import com.rosario.hp.espaciojahiel.activity_arcangel;
import com.rosario.hp.espaciojahiel.activity_arcangel_general;
import com.rosario.hp.espaciojahiel.activity_espacio;
import com.rosario.hp.espaciojahiel.activity_evento;
import com.rosario.hp.espaciojahiel.activity_imagen_principal;
import com.rosario.hp.espaciojahiel.activity_mensaje;
import com.rosario.hp.espaciojahiel.activity_videos;
import com.rosario.hp.espaciojahiel.activity_videos_programa;


public class principalFragment extends Fragment {
    public static final String ARG_ARTICLES_NUMBER = "principal";

    TextView mensaje_canalizado;
    TextView mi_arcangel;
    TextView eventos;
    TextView videos_meditacion;
    TextView videos_programa;
    TextView fondos_pantalla;
    TextView tu_arcangel;
    TextView espacio;
    Activity activity;
    ImageView instagram;
    ImageView facebook;
    ImageView youtube;
    ImageView pagina;
    ImageView menu_mensaje;
    ImageView menu_arcangel;
    ImageView menu_tu_arcangel;
    ImageView menu_eventos;
    ImageView menu_meditacion;
    ImageView menu_angel;
    ImageView menu_fondo;
    ImageView menu_espacio;

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
        videos_programa = rootView.findViewById(R.id.videos_programa);
        fondos_pantalla = rootView.findViewById(R.id.fondos_pantalla);
        espacio = rootView.findViewById(R.id.espacios);
        tu_arcangel = rootView.findViewById(R.id.tu_arcangel);
        instagram = rootView.findViewById(R.id.instagram);
        facebook = rootView.findViewById(R.id.imageButtonFace);
        youtube = rootView.findViewById(R.id.youtube);
        pagina = rootView.findViewById(R.id.imageViewpagina);
        menu_mensaje = rootView.findViewById(R.id.imagen_mensaje);
        menu_arcangel = rootView.findViewById(R.id.imagen_arcangel);
        menu_tu_arcangel= rootView.findViewById(R.id.imagen_tu_arcangel);
        menu_eventos = rootView.findViewById(R.id.imagen_evento);
        menu_meditacion = rootView.findViewById(R.id.imagen_meditacion);
        menu_angel = rootView.findViewById(R.id.imagen_angel);
        menu_fondo = rootView.findViewById(R.id.imagen_fondo);
        menu_espacio = rootView.findViewById(R.id.imagen_espacios);

        activity = getActivity();

        setHasOptionsMenu(true);

        Resources res = getActivity().getApplicationContext().getResources();

        final Bundle args1 = new Bundle();

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com/_u/espaciojahiel");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://instagram.com/espaciojahiel")));
                }
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://youtube.com/user/silvanaandreasotelo");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.google.android.youtube");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://youtube.com/user/silvanaandreasotelo")));
                }
            }
        });

        pagina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ls_link;

                ls_link = "http://espaciojahiel.com/";

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", ls_link);
                editor.apply();

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
                editor.commit();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent facebookAppIntent;
                try { facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/211895659607269"));
                    startActivity(facebookAppIntent); }
                catch (ActivityNotFoundException e)
                { facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://facebook.com/pg/espacioangelicojahiel"));
                startActivity(facebookAppIntent); }
            }
        });


        mensaje_canalizado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_mensaje.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        menu_mensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_mensaje.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });



        mi_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), activity_arcangel.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);

            }
        });

        menu_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_arcangel.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

        tu_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_arcangel_general.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

        menu_tu_arcangel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_arcangel_general.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

        eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_evento.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        menu_eventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_evento.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        videos_programa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_videos_programa.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        menu_angel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_videos_programa.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        videos_meditacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_videos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        menu_meditacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_videos.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        espacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_espacio.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        menu_espacio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_espacio.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });

        fondos_pantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), activity_imagen_principal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);


            }
        });

        menu_fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), activity_imagen_principal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);

            }
        });

        return rootView;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mensajes_guardados:
                activity.getActionBar().setTitle(item.getTitle());
                break;

        }
        return false;
    }

}

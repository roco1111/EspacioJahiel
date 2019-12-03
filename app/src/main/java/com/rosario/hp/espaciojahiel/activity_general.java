package com.rosario.hp.espaciojahiel;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.rosario.hp.espaciojahiel.Fragment.acercaFragment;
import com.rosario.hp.espaciojahiel.Fragment.datosUsuarios;
import com.rosario.hp.espaciojahiel.Fragment.imagenesUsuarioFragment;

import com.rosario.hp.espaciojahiel.Fragment.mensajesUsuarioFragment;
import com.rosario.hp.espaciojahiel.Fragment.notificacionesFragment;
import com.rosario.hp.espaciojahiel.Fragment.principalFragment;

public class activity_general extends AppCompatActivity {

    private String posicion_string;
    private int posicion;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener instancia FirebaseAuth

        setContentView(R.layout.lista_main_inicial);

        setToolbar(); // Setear Toolbar como action bar

        SharedPreferences settings3 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings3.edit();
        posicion_string = settings3.getString("posicion", "0");

        posicion = Integer.parseInt(posicion_string);


        Fragment fragment = null;

        Bundle args1 = new Bundle();

        if (posicion != 0) {

            switch (posicion) {
                case R.id.nav_home:
                    fragment = new principalFragment();
                    args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, posicion);
                    title="Principal";
                    break;
                case R.id.nav_notificaciones:
                    fragment = new notificacionesFragment();
                    args1.putInt(notificacionesFragment.ARG_ARTICLES_NUMBER, posicion);
                    title="Notificaciones";
                    break;

                case R.id.nav_mensajes_guardados:
                    fragment = new mensajesUsuarioFragment();
                    args1.putInt(mensajesUsuarioFragment.ARG_ARTICLES_NUMBER, posicion);
                    title="Mis Mensajes Guardados";
                    break;
                case R.id.nav_mis_fondo:
                    fragment = new imagenesUsuarioFragment();
                    args1.putInt(imagenesUsuarioFragment.ARG_ARTICLES_NUMBER, posicion);
                    title="Mis Fondos";
                    break;

                case R.id.nav_configuracion:
                    fragment = new datosUsuarios();
                    args1.putInt(datosUsuarios.ARG_ARTICLES_NUMBER, posicion);
                    title="Configuración";
                    break;
                case R.id.nav_acerca:
                    fragment = new acercaFragment();
                    args1.putInt(acercaFragment.ARG_ARTICLES_NUMBER, posicion);
                    title="Acerca";
                    break;
                default:
                    posicion_string = String.valueOf(R.id.nav_home);

                    editor.putString("posicion",posicion_string);
                    editor.apply();

                    editor.commit();
                    fragment = new principalFragment();
                    args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, R.id.nav_home);
                    title="Principal";
                    break;

            }

            fragment.setArguments(args1);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();

        }

        getSupportActionBar().setTitle(title);

}

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

    }



}

package com.rosario.hp.espaciojahiel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rosario.hp.espaciojahiel.Fragment.listaVideos;
import com.rosario.hp.espaciojahiel.Fragment.mensajePropioFragment;

public class activity_videos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener instancia FirebaseAuth

        setContentView(R.layout.lista_main_inicial);

        setToolbar(); // Setear Toolbar como action bar


        Fragment fragment = null;
        fragment = new listaVideos();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();

        }
        getSupportActionBar().setTitle("Videos de Meditación");

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
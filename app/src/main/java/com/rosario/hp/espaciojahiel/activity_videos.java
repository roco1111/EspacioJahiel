package com.rosario.hp.espaciojahiel;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.rosario.hp.espaciojahiel.Fragment.listaVideos;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR;


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

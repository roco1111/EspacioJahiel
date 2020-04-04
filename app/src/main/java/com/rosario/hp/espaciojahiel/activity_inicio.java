package com.rosario.hp.espaciojahiel;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.view.MenuItem;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.rosario.hp.espaciojahiel.Fragment.acercaFragment;
import com.rosario.hp.espaciojahiel.Fragment.arcangelGeneralFragment;
import com.rosario.hp.espaciojahiel.Fragment.fragment_presentacion;

public class activity_inicio extends AppCompatActivity implements fragment_presentacion.Callback{

    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1;
    private int posicion;
    private String posicion_string;
    private int posicion_nue;
    private DrawerLayout drawerLayout;
    private FirebaseAuth mFirebaseAuth;

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences settings3 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        posicion_string = settings3.getString("posicion", "0");

        posicion = Integer.parseInt(posicion_string);

        Fragment fragment = null;

        Bundle args1 = new Bundle();

        if (posicion != 0) {

            switch (posicion) {
                case R.id.nav_ingreso:
                    fragment = new fragment_presentacion();
                    args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, posicion);

                    break;
                case R.id.nav_arcangel_general:
                    fragment = new arcangelGeneralFragment();
                    args1.putInt(arcangelGeneralFragment.ARG_ARTICLES_NUMBER, posicion);

                    break;
                case R.id.nav_acerca:
                    fragment = new acercaFragment();
                    args1.putInt(acercaFragment.ARG_ARTICLES_NUMBER, posicion);
                    break;
                default:
                    fragment = new fragment_presentacion();
                    args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, posicion);

            }
            if(args1 != null ) {
                fragment.setArguments(args1);
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, fragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        posicion_string = String.valueOf(posicion);
        savedInstanceState.putString("posicion", posicion_string);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        posicion_nue = Integer.parseInt(savedInstanceState.getString("posicion"));

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings.edit();

        posicion_string = String.valueOf(posicion_nue);

        editor.putString("posicion",posicion_string);

        editor.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Obtener instancia FirebaseAuth

        setContentView(R.layout.lista_main_inicial);

        setToolbar(); // Setear Toolbar como action bar

        drawerLayout =  findViewById(R.id.drawer_layout);
        NavigationView navigationView =  findViewById(R.id.nav_view);

        Fragment fragment = null;
        fragment = new fragment_presentacion();


        Bundle args1 = new Bundle();
        args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, R.id.nav_home);
        fragment.setArguments(args1);


        SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = settings1.edit();

        posicion_string = String.valueOf(R.id.nav_ingreso);

        editor.putString("posicion",posicion_string);
        editor.apply();

        editor.commit();


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        boolean fragmentTransaction = false;
                        Fragment fragment = null;

                        Bundle args1 = new Bundle();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_ingreso:
                                fragment = new fragment_presentacion();
                                args1.putInt(fragment_presentacion.ARG_ARTICLES_NUMBER, menuItem.getItemId());
                                fragmentTransaction = true;
                                break;
                            case R.id.nav_arcangel_general:
                                fragment = new arcangelGeneralFragment();
                                args1.putInt(arcangelGeneralFragment.ARG_ARTICLES_NUMBER, menuItem.getItemId());
                                fragmentTransaction = true;
                                break;
                            case R.id.nav_acerca:
                                fragment = new acercaFragment();
                                args1.putInt(acercaFragment.ARG_ARTICLES_NUMBER, menuItem.getItemId());
                                fragmentTransaction = true;
                                break;
                        }
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                        SharedPreferences.Editor editor = settings.edit();

                        posicion = menuItem.getItemId();

                        posicion_string = String.valueOf(posicion);

                        editor.putString("posicion", posicion_string);
                        editor.apply();

                        editor.commit();

                        if (fragmentTransaction) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.main_content, fragment)
                                    .commit();

                            // menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }

                        drawerLayout.closeDrawers();

                        return true;
                }
    });


}

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_drawer));
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInvokeGooglePlayServices(int errorCode) {
        showPlayServicesErrorDialog(errorCode);
    }

    void showPlayServicesErrorDialog(
            final int errorCode) {
        Dialog dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(
                        activity_inicio.this,
                        errorCode,
                        REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

}

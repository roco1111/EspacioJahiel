package com.rosario.hp.espaciojahiel.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rosario.hp.espaciojahiel.BuildConfig;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.WebActivity;


public class acercaFragment extends Fragment {
    public static final String ARG_ARTICLES_NUMBER = "acerca";

    TextView version;
    TextView derechos1;
    TextView derechos2;
    TextView terminos;
    TextView privacidad;

    public acercaFragment(){
        // Constructor vacío
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.acerca, container, false);
        version =  rootView.findViewById(R.id.version);
        derechos1 =  rootView.findViewById(R.id.derechos1);
        derechos2 = rootView.findViewById(R.id.derechos2);
        terminos = rootView.findViewById(R.id.textViewTerminos);
        privacidad = rootView.findViewById(R.id.textViewPrivacidad);

        Resources res = getActivity().getApplicationContext().getResources();

        String versionName = BuildConfig.VERSION_NAME;

        version.setText(versionName);
        derechos1.setText(res.getText(R.string.derechos1));
        derechos2.setText(res.getText(R.string.derechos2));
        //String i = getArguments().getString(ARG_ARTICLES_NUMBER);

        terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", "https://app.espaciojahiel.com/movil_jahiel/privacidad.php");
                editor.apply();

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                editor.commit();
            }
        });

        privacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();

                editor.putString("url", "https://www.terclind.com.ar/movil/privacidad.php");
                editor.apply();

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                editor.commit();
            }
        });



        return rootView;
    }


}

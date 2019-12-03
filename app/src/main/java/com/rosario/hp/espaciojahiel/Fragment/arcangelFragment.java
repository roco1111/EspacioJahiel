package com.rosario.hp.espaciojahiel.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.rosario.hp.espaciojahiel.Adaptadores.mensajeCanalizadoAdapter;
import com.rosario.hp.espaciojahiel.Entidades.arcangel;
import com.rosario.hp.espaciojahiel.Entidades.usuario;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class arcangelFragment extends Fragment {
    private static final String TAG = arcangelFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "arcangel";

    private mensajeCanalizadoAdapter adapter;

    private TextView mensaje_arcangel;
    private TextView titulo_arcangel;
    private JsonObjectRequest myRequest;
    private String ls_cod_usuario;
    private String ls_fecha;
    private Gson gson = new Gson();
    private ImageButton configuracion;
    String posicion_string;

    ProgressDialog progress1;

    public arcangelFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_arcangel, container, false);

        mensaje_arcangel = v.findViewById(R.id.textViewtexto);
        titulo_arcangel = v.findViewById(R.id.textViewTitulo);

        configuracion = v.findViewById(R.id.buttonConfiguracion);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_cod_usuario     = settings.getString("cod_usuario","");
        progress1 = ProgressDialog.show(getContext(), "Cargando Datos", "Por favor, espere..", true);
        cargarDatos(getContext());

        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getActivity().onBackPressed();

                posicion_string = String.valueOf(R.id.nav_configuracion);

                SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings1.edit();

                editor.putString("posicion",posicion_string);
                editor.apply();

                editor.commit();

                Bundle args1 = new Bundle();

                Fragment fragment = new datosUsuarios();
                args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, R.id.nav_configuracion);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_content, fragment)
                        .commit();
            }
        });


        return v;
    }
    public void cargarAdaptador() {
        // Petición GET



        int dia;

        if(ls_fecha == "null"){
            ls_fecha = "";
        }

        if (ls_fecha.equals("") || ls_fecha.equals("0000-00-00") || ls_fecha.equals("0002-11-30")){
            mensaje_arcangel.setText("Debe ingresar su fecha de nacimiento en Configuración");
            titulo_arcangel.setText("Atención");
            configuracion.setVisibility(View.VISIBLE);
            progress1.dismiss();
        }else {

            configuracion.setVisibility(View.INVISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date datetxt1 = null;
            Calendar selectedDate1 = Calendar.getInstance();
            try {
                datetxt1 = sdf.parse(ls_fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedDate1.setTime(datetxt1);

            dia = selectedDate1.get(Calendar.DAY_OF_WEEK);

            int anio = selectedDate1.get(Calendar.YEAR);

            if (anio < 1000) {
                mensaje_arcangel.setText("Debe ingresar el año de nacimiento con 4 dígitos");
                titulo_arcangel.setText("Atención");
                configuracion.setVisibility(View.VISIBLE);
                progress1.dismiss();
            } else {


                String newURL = Constantes.GET_ARCANGEL_FECHA + "?id=" + String.valueOf(dia);
                Log.d(TAG, newURL);

                VolleySingleton.
                        getInstance(getActivity()).
                        addToRequestQueue(
                                new JsonObjectRequest(
                                        Request.Method.POST,
                                        newURL,
                                        null,
                                        new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // Procesar la respuesta Json
                                                procesarRespuesta(response);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d(TAG, "Error Volley: " + error.toString());
                                                progress1.dismiss();
                                            }
                                        }

                                )
                        );
            }
        }
    }
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("arcangel");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        arcangel arc = new arcangel("","","","");

                        String id = object.getString("id");

                        arc.setId(id);

                        String titulo = object.getString("titulo");

                        arc.setTitulo(titulo);

                        String mensaje_dia = object.getString("mensaje");

                        arc.setMensaje(mensaje_dia);

                        mensaje_arcangel.setText(mensaje_dia);

                        titulo_arcangel.setText(titulo);
                        progress1.dismiss();

                    }


                    break;
                case "2": // FALLIDO
                    progress1.dismiss();

                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    public void cargarDatos(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_ID_USUARIO + "?id=" + ls_cod_usuario;
        Log.d(TAG,newURL);

        // Realizar petición GET_BY_ID
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuestaArcangel(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                progress1.dismiss();
                            }
                        }
                )
        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuestaArcangel(JSONObject response, Context context) {

        try {
            // Obtener atributo "mensaje"
            String estado = response.getString("estado");

            switch (estado) {
                case "1":
                    // Obtener objeto "cliente"
                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++) {
                        JSONObject object = mensaje.getJSONObject(i);

                        //Parsear objeto
                        usuario codigoUsuario = gson.fromJson(object.toString(), usuario.class);

                        ls_fecha = object.getString("fecha_nac");
                    }

                    cargarAdaptador();

                    break;

                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    progress1.dismiss();
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    progress1.dismiss();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

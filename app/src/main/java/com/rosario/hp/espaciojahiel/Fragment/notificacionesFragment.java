package com.rosario.hp.espaciojahiel.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.rosario.hp.espaciojahiel.Adaptadores.notificacionesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


import com.rosario.hp.espaciojahiel.Entidades.notificacion;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;


public class notificacionesFragment extends Fragment {

    private static final String TAG = notificacionesFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "notificaciones";

    private notificacionesAdapter adapter;

    private RecyclerView lista;

    private TextView texto;
    private ImageView imagen;

    private RecyclerView.LayoutManager lManager;
    private Gson gson = new Gson();
    private BroadcastReceiver mNotificationsReceiver;

    private ArrayList<notificacion> notificaciones;


    public notificacionesFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets de argumentos
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_basico, container, false);

        new ArrayList<>();

        lista =  v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        texto =  v.findViewById(R.id.TwEmpty);
        imagen =  v.findViewById(R.id.ImEmpty);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);

        notificaciones = new ArrayList<>();


        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);

        cargarAdaptador();

        return v;
    }

    public void cargarAdaptador() {

        String newURL = Constantes.GET_NOTIFICACIONES;
        VolleySingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
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
                                    }
                                }

                        )
                );
    }

    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            notificaciones.clear();

            switch (estado) {
                case "1": // EXITO
                    // Obtener array "presupuestos" Json
                    JSONArray mensaje = response.getJSONArray("notificacion");
                    // Parsear con Gson
                    for(int i = 0; i < mensaje.length(); i++) {
                        JSONObject object = mensaje.getJSONObject(i);
                        notificacion not = new notificacion("", "", "", "", "");

                        String id = object.getString("id");
                        not.setId(id);

                        String titulo = object.getString("titulo");
                        not.setTitulo(titulo);

                        String texto = object.getString("texto");
                        not.setTexto(texto);

                        String fecha = object.getString("fecha");
                        not.setFecha(fecha);

                        String hora = object.getString("hora");
                        not.setTiempo(hora);

                        notificaciones.add(not);
                    }

                    adapter = new notificacionesAdapter(notificaciones,getActivity());

                    // Setear adaptador a la lista
                    lista.setAdapter(adapter);

                    break;
                case "2": // FALLIDO

                    imagen.setVisibility(getView().VISIBLE);
                    texto.setVisibility(getView().VISIBLE);

                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
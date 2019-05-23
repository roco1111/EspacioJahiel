package com.rosario.hp.espaciojahiel.Fragment;

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
import com.rosario.hp.espaciojahiel.Adaptadores.eventoAdapter;
import com.rosario.hp.espaciojahiel.Adaptadores.mensajeCanalizadoAdapter;
import com.rosario.hp.espaciojahiel.Entidades.mensajeUsuario;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class mensajesUsuarioFragment extends Fragment {
    private static final String TAG = mensajesUsuarioFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "mensajes_usuarios";

    private mensajeCanalizadoAdapter adapter;

    private RecyclerView lista;
    private TextView texto;
    private ImageView imagen;

    private RecyclerView.LayoutManager lManager;

    private ArrayList<mensajeUsuario> datos;
    private String ls_cod_usuario;
    public mensajesUsuarioFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_basico, container, false);

        datos = new ArrayList<>();
        //listView = (ListView) v.findViewById(R.id.li)

        lista =  v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        texto =  v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);


        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        ls_cod_usuario     = settings.getString("cod_usuario","");

        // Cargar datos en el adaptador
        cargarAdaptador();

        return v;
    }
    public void cargarAdaptador() {
        // Petición GET

        String newURL = Constantes.GET_MENSAJES_USUARIO + "?id_usuario=" + ls_cod_usuario  ;
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

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        mensajeUsuario mu = new mensajeUsuario("","");

                        String mensaje_canalizado = object.getString("mensaje");

                        mu.setMensaje(mensaje_canalizado);

                        String fecha = object.getString("fecha");

                        mu.setFecha(fecha);

                        String id_mensaje = object.getString("id_mensaje");

                        mu.setId_mensaje(id_mensaje);



                        datos.add(mu);

                    }

                    adapter = new mensajeCanalizadoAdapter(datos, getActivity());
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

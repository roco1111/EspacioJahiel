package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rosario.hp.espaciojahiel.Adaptadores.mensajeCanalizadoAdapter;
import com.rosario.hp.espaciojahiel.Entidades.mensajeCanalizado;
import com.rosario.hp.espaciojahiel.Entidades.mensajeUsuario;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class mensajePropioFragment extends Fragment {
    private static final String TAG = mensajePropioFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "mensaje_canalizado";

    private mensajeCanalizadoAdapter adapter;

    private TextView mensaje_canalizado;
    private ArrayList<mensajeCanalizado> datos;
    private ImageButton meGusta;
    Calendar c1 = Calendar.getInstance();
    private JsonObjectRequest myRequest;
    private String ls_mensaje;
    private ImageView imagen;

    public mensajePropioFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mensaje_canalizado, container, false);

        imagen = v.findViewById(R.id.imageViewMensaje);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_mensaje     = settings.getString("id","");

        Integer li_mensaje = Integer.parseInt(ls_mensaje);

        switch (mod(li_mensaje,12)) {
            case 0:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje12));
                break;
            case 1:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje1));
                break;
            case 2:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje2));
                break;
            case 3:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje3));
                break;
            case 4:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje4));
                break;
            case 5:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje5));
                break;
            case 6:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje6));
                break;
            case 7:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje7));
                break;
            case 8:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje8));
                break;
            case 9:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje9));
                break;
            case 10:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje10));
                break;
            case 11:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje11));
                break;
            case 12:
                imagen.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje12));
                break;

        }

        mensaje_canalizado = v.findViewById(R.id.textViewMensaje);
        meGusta = v.findViewById(R.id.floatingMeGusta);

        datos = new ArrayList<>();

        meGusta.setVisibility(View.INVISIBLE);

        cargarAdaptador();
        return v;
    }
    public void cargarAdaptador() {
        // Petición GET


        String newURL = Constantes.GET_MENSAJE + "?id=" + ls_mensaje; ;
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
            datos.clear();

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("mensajeCanalizado");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        mensajeCanalizado men = new mensajeCanalizado("","","");

                        String id = object.getString("id");

                        men.setId(id);


                        String mensaje_canalizado = object.getString("mensaje");

                        men.setMensaje(mensaje_canalizado);

                        datos.add(men);

                    }

                    mensaje_canalizado.setText(datos.get(0).getMensaje());

                    break;
                case "2": // FALLIDO


                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    private int mod(int x, int y)
    {
        int result = x % y;
        return result < 0? result + y : result;
    }

}

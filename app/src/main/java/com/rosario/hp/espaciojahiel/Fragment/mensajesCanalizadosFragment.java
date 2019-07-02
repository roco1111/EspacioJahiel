package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
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
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class mensajesCanalizadosFragment extends Fragment {
    private static final String TAG = mensajesCanalizadosFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "mensaje_canalizado";

    private mensajeCanalizadoAdapter adapter;

    private TextView mensaje_canalizado;
    private ArrayList<mensajeCanalizado> datos;
    private ImageButton meGusta;
    Calendar c1 = Calendar.getInstance();
    private JsonObjectRequest myRequest;
    private String ls_cod_usuario;
    private String ls_fecha;
    private String ls_mensaje;
    private ImageView imageViewMensaje;
    ProgressDialog progress1;

    public mensajesCanalizadosFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mensaje_canalizado, container, false);

        progress1 = ProgressDialog.show(getContext(), "Cargando Datos", "Por favor, espere..", true);

        mensaje_canalizado = v.findViewById(R.id.textViewMensaje);
        meGusta = v.findViewById(R.id.floatingMeGusta);
        imageViewMensaje = v.findViewById(R.id.imageViewMensaje);

        datos = new ArrayList<>();

        meGusta.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        verificarMensaje();

                    }
                });

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        ls_cod_usuario     = settings.getString("cod_usuario","");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        ls_fecha = df.format(c.getTime());

        Integer numero = (int) (Math.random() * 12) + 1;

        switch (numero) {
            case 1:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje1));
                break;
            case 2:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje2));
                break;
            case 3:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje3));
                break;
            case 4:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje4));
                break;
            case 5:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje5));
                break;
            case 6:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje6));
                break;
            case 7:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje7));
                break;
            case 8:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje8));
                break;
            case 9:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje9));
                break;
            case 10:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje10));
                break;
            case 11:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje11));
                break;
            case 12:
                imageViewMensaje.setImageDrawable(getContext().getResources().getDrawable(R.drawable.mensaje12));
                break;

        }
        verificarMensajeFecha();

        return v;
    }

    public void verificarMensajeFecha() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_usuario", ls_cod_usuario);
        map.put("fecha", ls_fecha);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.VERIFICAR_MENSAJE_FECHA + "?" + encodedParams;
        Log.d(TAG,newURL);

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
                                        procesarVerificarFecha(response);
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
    private void procesarVerificarFecha(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");


            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);

                        int cantidad = Integer.parseInt(object.getString("cantidad"));

                        if(cantidad == 0) {

                            cargarAdaptador();

                        }else{

                            cargarMensaje();

                        }


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

    public void cargarMensaje() {
        // Petición GET
        HashMap<String, String> map = new HashMap<>();// Mapeo previo



        map.put("id_usuario", ls_cod_usuario);
        map.put("fecha", ls_fecha);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.GET_MENSAJES_FECHA + "?" + encodedParams;
        Log.d(TAG,newURL);

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
                                        procesarRespuesta_mensaje_fecha(response);
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
    private void procesarRespuesta_mensaje_fecha(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");
            datos.clear();

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        mensajeUsuario men = new mensajeUsuario("","");


                        String mensaje_can = object.getString("mensaje");

                        men.setMensaje(mensaje_can);

                        String id = object.getString("id");

                        men.setId_mensaje(id);

                        ls_mensaje = id;

                        mensaje_canalizado.setText(mensaje_can);

                    }

                    progress1.dismiss();

                    break;
                case "2": // FALLIDO

                    progress1.dismiss();

                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }
    public void cargarAdaptador() {
        // Petición GET


        ls_mensaje = String.valueOf((int) (Math.random() * 3) + 1);

        String newURL = Constantes.GET_MENSAJE + "?id=" + ls_mensaje;
        Log.d(TAG,newURL);

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
                    guardarMensajeFecha();


                    break;
                case "2": // FALLIDO

                    progress1.dismiss();
                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }
    private void guardarMensajeFecha() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_usuario", ls_cod_usuario);
        map.put("id_mensaje", ls_mensaje);
        map.put("fecha", ls_fecha);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.INSERT_USUARIO_FECHA + "?" + encodedParams;
        Log.d(TAG,newURL);
        // Actualizar datos en el servidor
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaInsertFecha(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                                progress1.dismiss();
                            }
                        }


                ) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                }

        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void procesarRespuestaInsertFecha(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    progress1.dismiss();
                    getActivity().setResult(Activity.RESULT_OK);


                    break;

                case "2":
                    // Mostrar mensaje
                    progress1.dismiss();
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void guardarMensaje() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_usuario", ls_cod_usuario);
        map.put("id_mensaje", ls_mensaje);
        map.put("fecha", ls_fecha);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.INSERT_USUARIO_MENSAJE + "?" + encodedParams;
        Log.d(TAG,newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaInsert(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }


                ) {

                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }


                }

        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void procesarRespuestaInsert(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    getActivity().setResult(Activity.RESULT_OK);


                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();

                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void verificarMensaje() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_usuario", ls_cod_usuario);
        map.put("id_mensaje", ls_mensaje);
        map.put("fecha", ls_fecha);

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jobject = new JSONObject(map);


        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8"));
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.VERIFICAR_MENSAJE + "?" + encodedParams;
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
                                        procesarVerificar(response);
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
    private void procesarVerificar(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");


            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        mensajeUsuario men = new mensajeUsuario("","");

                        int cantidad = Integer.parseInt(object.getString("cantidad"));

                       if(cantidad == 0) {

                           guardarMensaje();

                       }


                    }

                    break;
                case "2": // FALLIDO


                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}

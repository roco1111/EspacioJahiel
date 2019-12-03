package com.rosario.hp.espaciojahiel.Fragment;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.Adaptadores.eventoAdapter;
import com.rosario.hp.espaciojahiel.Entidades.evento;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class eventosFragment extends Fragment {
    private static final String TAG = eventosFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "eventos";

    private eventoAdapter adapter;

    private RecyclerView lista;
    private TextView texto;
    private ImageView imagen;

    private RecyclerView.LayoutManager lManager;

    private ArrayList<evento> datos;

    private FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    ArrayList<Bitmap> images;

    public eventosFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }

        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_basico, container, false);

        datos = new ArrayList<>();
        //listView = (ListView) v.findViewById(R.id.li)
        mAuth = FirebaseAuth.getInstance();

        lista =  v.findViewById(R.id.reciclador);
        lista.setHasFixedSize(true);

        texto =  v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);
        images = new ArrayList<>();

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(lManager);

        // Cargar datos en el adaptador
        cargarAdaptador();

        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
    public void cargarAdaptador() {
        // Petición GET

        String newURL = Constantes.GET_EVENTOS ;

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

                    JSONArray mensaje = response.getJSONArray("evento");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        evento ev = new evento("","","","","","");

                        String id = object.getString("ID");

                        ev.setId(id);

                        String fecha = object.getString("FECHA");

                        ev.setFecha(fecha);

                        String nombre = object.getString("NOMBRE");

                        ev.setNombre(nombre);

                        String lugar = object.getString("LUGAR");

                        ev.setLugar(lugar);

                        String link = object.getString("LINK");

                        ev.setLink(link);

                        String fecha_alta = object.getString("FECHA_ALTA");

                        ev.setFecha_alta(fecha_alta);

                        String hora = object.getString("HORA");

                        ev.setHora(hora);

                        String destacado = object.getString("destacado");

                        ev.setDestacado(destacado);

                        String cupo_lleno = object.getString("CUPO_LLENO");

                        ev.setCupo_lleno(cupo_lleno);

                        datos.add(ev);

                    }

                    adapter = new eventoAdapter(datos,getContext());
                    // Setear adaptador a la lista
                    lista.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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
    class GetImageToURL extends AsyncTask<Bitmap, Bitmap, Bitmap> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Bitmap doInBackground(Bitmap... params) {


            return params[0];
        }

        protected void onPostExecute(Bitmap result, ImageView imageView) {
            imageView.setImageBitmap(result);
        }
    }

}

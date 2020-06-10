package com.rosario.hp.espaciojahiel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.rosario.hp.espaciojahiel.Entidades.imagen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rosario.hp.espaciojahiel.R;

import com.rosario.hp.espaciojahiel.Adaptadores.imagenAdapter;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class listaImagenesFragment extends Fragment {

    private imagenAdapter imagenAdapter;
    public static final String ARG_ARTICLES_NUMBER = "imagenes";
    private static final String TAG = listaImagenesFragment.class.getSimpleName();
    private GridView gridView;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private TextView texto;
    private ImageView imagen;
    private FirebaseAuth mAuth;
    private ArrayList<imagen> datos;
    public listaImagenesFragment(){}
    public Context context;
    ArrayList<Bitmap> images;
    SwipeRefreshLayout swipeRefreshLayout;

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

        View v = inflater.inflate(R.layout.imagenes_grid, container, false);

        gridView = v.findViewById(R.id.grid);

        mAuth = FirebaseAuth.getInstance();

        context = getContext();

        datos= new ArrayList<>();
        texto = v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);
        images = new ArrayList<>();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("mi_imagen", "0");
        editor.apply();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Tarea a realizar

                swipeRefreshLayout.setRefreshing(true);
                cargarAdaptador();
            }
        });

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

        String newURL = Constantes.GET_IMAGENES ;
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
                                        swipeRefreshLayout.setRefreshing(false);
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

                    JSONArray mensaje = response.getJSONArray("imagen");

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        imagen ima = new imagen("","","","","","");

                        String id = object.getString("ID");

                        ima.setId(id);

                        String nombre = object.getString("NOMBRE");

                        ima.setNombre(nombre);

                        String descripcion = object.getString("DESCRIPCION");

                        ima.setDescripcion(descripcion);

                        String precio = object.getString("PRECIO");

                        ima.setPrecio(precio);

                        String calificacion = object.getString("CALIFICACION");

                        ima.setCalificacion(calificacion);



                        datos.add(ima);



                    }

                    imagenAdapter = new imagenAdapter(getContext(), datos, images);
                    // Setear adaptador a la lista
                    gridView.setAdapter(imagenAdapter);
                    imagenAdapter.notifyDataSetChanged();

                    break;
                case "2": // FALLIDO

                    imagen.setVisibility(getView().VISIBLE);
                    texto.setVisibility(getView().VISIBLE);



                    break;
            }
            swipeRefreshLayout.setRefreshing(false);

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}

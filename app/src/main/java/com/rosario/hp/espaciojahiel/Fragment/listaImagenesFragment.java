package com.rosario.hp.espaciojahiel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class listaImagenesFragment extends Fragment {

    private imagenAdapter imagenAdapter;
    public static final String ARG_ARTICLES_NUMBER = "imagenes";
    private static final String TAG = listaImagenesFragment.class.getSimpleName();
    private RecyclerView lista;
    private GridView gridView;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private TextView texto;
    private ImageView imagen;
    private FirebaseAuth mAuth;
    private ArrayList<imagen> datos;
    public listaImagenesFragment(){}
    public Context context;
    ArrayList<Bitmap> images;

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

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);
        images = new ArrayList<>();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("mi_imagen", "0");
        editor.apply();

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

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }


}
package com.rosario.hp.espaciojahiel.Fragment;

import android.content.Context;

import android.graphics.Bitmap;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.espaciojahiel.Adaptadores.espacioAdapter;
import com.rosario.hp.espaciojahiel.Entidades.espacioAmigo;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class espacioFragment extends Fragment {
    private static final String TAG = eventosFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "espaciosAmigos";
    private Context context;
    private TextView texto;
    private ImageView imagen;
    private ArrayList<espacioAmigo> datos;
    public espacioFragment(){}
    private GridView gridView;
    private espacioAdapter espacioAdapter;
    private Bitmap loadedImage;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
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
        View v = inflater.inflate(R.layout.espacios_amigos, container, false);
        gridView = v.findViewById(R.id.grid);

        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout);

        mAuth = FirebaseAuth.getInstance();

        context = getContext();

        datos= new ArrayList<>();
        texto =  v.findViewById(R.id.TwEmpty);
        imagen = v.findViewById(R.id.ImEmpty);

        imagen.setVisibility(v.INVISIBLE);
        texto.setVisibility(v.INVISIBLE);
        images = new ArrayList<>();

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

        String newURL = Constantes.GET_ESPACIOS ;
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

                    JSONArray mensaje = response.getJSONArray("espacio_amigo");

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        espacioAmigo ea = new espacioAmigo("","","","","","","","","");

                        String id = object.getString("ID");

                        ea.setId(id);

                        String mail = object.getString("MAIL");

                        ea.setMail(mail);

                        String nombre = object.getString("NOMBRE");

                        ea.setNombre(nombre);

                        String lugar = object.getString("CONTACTO");

                        ea.setContacto(lugar);

                        String destacado = object.getString("DESTACADO");

                        ea.setDestacado(destacado);

                        String face = object.getString("FACE");

                        ea.setFace(face);

                        String pagina = object.getString("PAGINA");

                        ea.setPagina(pagina);

                        String instagram = object.getString("INSTAGRAM");

                        ea.setInstagram(instagram);

                        String telefono = object.getString("TELEFONO");

                        ea.setTelefono(telefono);

                        datos.add(ea);

                    }

                    espacioAdapter = new espacioAdapter(getContext(), datos, images);
                    // Setear adaptador a la lista
                    gridView.setAdapter(espacioAdapter);
                    espacioAdapter.notifyDataSetChanged();


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

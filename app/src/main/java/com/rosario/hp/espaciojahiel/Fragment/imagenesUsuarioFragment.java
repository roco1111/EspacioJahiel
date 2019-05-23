package com.rosario.hp.espaciojahiel.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rosario.hp.espaciojahiel.Adaptadores.imagenAdapter;
import com.rosario.hp.espaciojahiel.Entidades.imagen;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class imagenesUsuarioFragment extends Fragment {

    private imagenAdapter imagenAdapter;
    public static final String ARG_ARTICLES_NUMBER = "imagenes";
    private static final String TAG = imagenesUsuarioFragment.class.getSimpleName();
    private RecyclerView lista;
    private GridView gridView;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private TextView texto;
    private ImageView imagen;
    private FirebaseAuth mAuth;
    private ArrayList<imagen> datos;
    public imagenesUsuarioFragment(){}
    public Context context;
    ArrayList<Bitmap> images;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private Bitmap loadedImage;
    GetImageToURL GetImageToURL;
    private String ls_cod_usuario;

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
        ls_cod_usuario     = settings.getString("cod_usuario","");
        editor.putString("mi_imagen", "1");
        editor.apply();

        editor.commit();

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

        String newURL = Constantes.GET_IMAGENES_USUARIO + "?id_usuario=" + ls_cod_usuario ;
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

                    datos.clear();

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        imagen ima = new imagen("","","","","","");

                        String id = object.getString("id_imagen");

                        ima.setId(id);

                        String nombre = object.getString("nombre");

                        ima.setNombre(nombre);

                        String descripcion = object.getString("descripcion");

                        ima.setDescripcion(descripcion);

                        datos.add(ima);

                    }

                    for(int i = 0;i < datos.size(); i++) {

                        actualizar_foto(datos.get(i).getId());
                    }

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

    public void actualizar_foto(String imagenid){
        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();


        String mChild = "imagenes/" + imagenid  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        final long ONE_MEGABYTE = 1024 * 1024;
        filepath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                loadedImage = null;
                loadedImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                GetImageToURL = new GetImageToURL();

                GetImageToURL.execute(loadedImage);

                images.add(loadedImage);

                if(images.size() == datos.size()) {
                    imagenAdapter = new imagenAdapter(getContext(), datos, images);
                    // Setear adaptador a la lista
                    gridView.setAdapter(imagenAdapter);
                    imagenAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
            }
        });


    }
}

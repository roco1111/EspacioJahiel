package com.rosario.hp.espaciojahiel.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.rosario.hp.espaciojahiel.Adaptadores.mensajeCanalizadoAdapter;
import com.rosario.hp.espaciojahiel.BuildConfig;
import com.rosario.hp.espaciojahiel.Entidades.imagen;

import com.rosario.hp.espaciojahiel.GlideApp;
import com.rosario.hp.espaciojahiel.MainActivity;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class imagenFragment extends Fragment {
    private static final String TAG = imagenFragment.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "imagen";

    private ArrayList<imagen> datos;
    private ImageButton meGusta;
    private ImageButton descargar;
    private ImageButton fondo;
    private JsonObjectRequest myRequest;
    private String ls_imagen;
    ImageView imagen;
    String ls_cod_usuario;
    String ls_mi_imagen;
    private Context context;
    private Activity activity;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public imagenFragment(){}

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.imagenes_principal, container, false);

        activity = getActivity();
        context = getContext();
        mAuth = FirebaseAuth.getInstance();

        meGusta = v.findViewById(R.id.buttonFavorito);
        fondo = v.findViewById(R.id.buttonFondo);
        descargar = v.findViewById(R.id.buttonDescarga);
        imagen = v.findViewById(R.id.imageFondo);

        datos = new ArrayList<>();

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_cod_usuario     = settings.getString("cod_usuario","");
        ls_imagen     = settings.getString("id","");
        ls_mi_imagen = settings.getString("mi_imagen","");

        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String mChild = "imagenes/" + ls_imagen  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        GlideApp.with(context)
                .load(filepath)
                .into(imagen);

        if(ls_mi_imagen.equals("1")){
            meGusta.setVisibility(View.INVISIBLE);

        }else{
            meGusta.setVisibility(View.VISIBLE);
        }

        meGusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardarMensaje();
            }});

        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();
                SaveImage(context,bitmap);
            }});

        fondo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean whichSystem = true, whichLock = true;
                boolean statesApply[] = new boolean[]{whichSystem, whichLock};
                boolean fixed = false;
                Bitmap bitmap = ((BitmapDrawable)imagen.getDrawable()).getBitmap();

                setWallaper(activity, bitmap, statesApply, fixed);
            }});

        return v;
    }



    private void guardarMensaje() {
        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id_usuario", ls_cod_usuario);
        map.put("id_imagen", ls_imagen);

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

        String newURL = Constantes.INSERT_USUARIO_IMAGEN + "?" + encodedParams;
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
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
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

    public void SaveImage(Context context, Bitmap ImageToSave) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())  {
            Log.e(TAG, "Storage not available or read only");
            return;
        }

        verifyStoragePermissions(getActivity());

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "Imagen" + ls_imagen + ".jpg");

        try {
            FileOutputStream fOut = new FileOutputStream(file);

            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }


    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(context,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage", "Scanned " + path + ":");
                        Log.e("ExternalStorage", "-> uri=" + uri);
                    }
                });
    }


    private void UnableToSave() {
        Toast.makeText(getActivity(), "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(getActivity(), "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }

    private boolean setWallaper(Activity activity, Bitmap bitmap, final @Nullable boolean[] statesApply, boolean fixed) {
        boolean success = false;
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int height = metrics.heightPixels;
        final int width = metrics.widthPixels;

        if (bitmap != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    boolean whichSystem = true;
                    boolean whichLock = true;
                    if (statesApply != null) {
                        whichSystem = statesApply[0];
                        whichLock = statesApply[1];
                    }
                    int which = 0;
                    if (whichSystem) which |= WallpaperManager.FLAG_SYSTEM;
                    if (whichLock) which |= WallpaperManager.FLAG_LOCK;

                    //wallpaperManager.clear();
                    if (fixed) {
                        wallpaperManager.setWallpaperOffsetSteps(1, 1);
                        wallpaperManager.suggestDesiredDimensions(width, height);
                    }
                    wallpaperManager.setBitmap(bitmap, null, true, which);
                } else {
                    //wallpaperManager.clear();
                    if (fixed) {
                        wallpaperManager.setWallpaperOffsetSteps(1, 1);
                        wallpaperManager.suggestDesiredDimensions(width, height);
                    }
                    wallpaperManager.setBitmap(bitmap);
                }
                success = true;
            } catch (OutOfMemoryError | IOException | NullPointerException e) {
                if (BuildConfig.DEBUG)
                    e.printStackTrace();
            }
        }
        return success;
    }

}

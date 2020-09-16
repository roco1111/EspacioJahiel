package com.rosario.hp.espaciojahiel.Fragment;

import android.Manifest;
import android.app.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rosario.hp.espaciojahiel.Entidades.usuario;
import com.rosario.hp.espaciojahiel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.NothingSelectedSpinnerAdapter;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class datosUsuarios extends Fragment {
    private static final String TAG = datosUsuarios.class.getSimpleName();
    String id_firebase= null;
    private FirebaseAuth mAuth = null;
    NothingSelectedSpinnerAdapter nothing;
    private JsonObjectRequest myRequest;
    private TextView tvNombre = null;
    private TextView tvMail = null;
    private TextView tvClave = null;
    private TextView tvConfirmar = null;
    private TextView tvFecha = null;
    private Button btn_guardar = null;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    public static final String ARG_ARTICLES_NUMBER = "datos_usuario";
    private static int SELECT_PICTURE = 2;
    private ArrayList<usuario> usuarios;
    String ls_cod_usuario;
    private Button editar_foto;
    private ImageButton button_fecha;
    private CircleImageView imagen;
    GetImageToURL GetImageToURL;
    Uri imageUri;
    Context context;
    StorageReference storageRef;
    private FirebaseStorage storage;
    private Bitmap loadedImage;
    DatePickerDialog datePickerDialog;
    String posicion_string;
    private Activity activity;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

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
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("restore", true);
        outState.putInt("nAndroids", 2);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void  onResume(){
        super.onResume();

        setRetainInstance(true);
        context = getContext();
    }

    @Override
    public void  onPause(){
        super.onPause();

    }

    public datosUsuarios() {
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        if (context instanceof Activity){
            activity=(Activity) context;
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        usuarios = new ArrayList<>();
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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
        ls_cod_usuario     = settings.getString("cod_usuario","");



    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflando layout del fragmento

        View v;
        v = inflater.inflate(R.layout.activity_datos_usuario, container, false);
        this.tvNombre = v.findViewById(R.id.editTextNombre);
        this.tvMail =  v.findViewById(R.id.editTextMail);
        this.tvClave = v.findViewById(R.id.editTextClave);
        this.tvConfirmar = v.findViewById(R.id.editTextConfirmar);
        this.tvFecha = v.findViewById(R.id.editTextFechaNac);
        this.btn_guardar = v.findViewById(R.id.buttonRegistro);
        this.editar_foto = v.findViewById(R.id.buttonFoto);
        this.button_fecha = v.findViewById(R.id.buttonFecha);
        imagen =  v.findViewById(R.id.imageViewfoto);
        activity = getActivity();

        this.button_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ls_fecha = tvFecha.getText().toString();
                Date datetxt1 = null;

                if(ls_fecha.equals( "null")){
                    ls_fecha = "30/11/0002";
                }

                if(ls_fecha.equals("30/11/0002")) {
                    datetxt1 = Calendar.getInstance().getTime();
                }else {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    try {
                        datetxt1 = sdf.parse(ls_fecha);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                final Calendar c = Calendar.getInstance();
                c.setTime(datetxt1);
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day



                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                tvFecha.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                                modificarUsuario();

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        this.btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validar()) {

                    if (compara_clave()) {
                        if(ls_cod_usuario.equals("")){

                            guardarUsuario();

                        } else {
                            modificarUsuario();
                        }
                    } else {
                        Toast.makeText(
                                activity,
                                "La clave ingresada no coincide con su confirmación",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        setHasOptionsMenu(true);

        editar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria(v);

            }
        });

        cargarAdaptador();

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    private void actualizar_foto(){
        storage = FirebaseStorage.getInstance();

        if (storageRef == null)
            storageRef = storage.getReference();

        String mChild = "usuarios/" + ls_cod_usuario  + ".jpg";
        final StorageReference filepath = storageRef.child(mChild);

        clearGlideCache();

        Glide.with(activity.getApplicationContext())
                .load(filepath)
                .error(R.drawable.ic_account_circle)
                .fallback(R.drawable.ic_account_circle)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .centerCrop ()
                .into(imagen);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {


            imageUri = data.getData();

            context = getContext();

            try {
                loadedImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

            } catch (Exception e) {
                e.printStackTrace();
            }

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            bmOptions.inSampleSize = 1;
            bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bmOptions.inJustDecodeBounds = false;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            loadedImage.compress(Bitmap.CompressFormat.JPEG, 10, baos);

            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), loadedImage, "Title", null);
            imageUri = Uri.parse(path);

            switch (requestCode) {
                case 2:

                    if (requestCode == SELECT_PICTURE) {


                        storage = FirebaseStorage.getInstance();

                        if (storageRef == null)
                            storageRef = storage.getReference();

                        String mChild = "usuarios/" + ls_cod_usuario + ".jpg";
                        final StorageReference filepath = storageRef.child(mChild);

                        filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Toast.makeText(activity, "Se ha actualizado la foto", Toast.LENGTH_SHORT).show();

                                GetImageToURL = new GetImageToURL();

                                GetImageToURL.execute(loadedImage);

                            }
                        });
                    }


                    break;
            }

        }
    }

    private class GetImageToURL extends AsyncTask<Bitmap, Bitmap, Bitmap> {

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected Bitmap doInBackground(Bitmap... params) {


            return params[0];
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            imagen.setImageBitmap(result);
            imagen.postInvalidate();

        }
    }



    private void  abrirGaleria(View v) {
        verifyStoragePermissions(activity);
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccione una imagen"),
                SELECT_PICTURE);

    }

    private Void carga_datos(){
        String ls_fecha;
        Date date ;

        ls_fecha = usuarios.get(0).getFecha_nac();
        SimpleDateFormat inDateFmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outDateFmt = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = inDateFmt.parse(ls_fecha);
            ls_fecha= outDateFmt.format(date);
        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }

        tvNombre.setText(usuarios.get(0).getNombre());
        tvMail.setText(usuarios.get(0).getMail());
        tvFecha.setText(ls_fecha);


        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_guardar:// CONFIRMAR
                if (!validar()) {

                    if (compara_clave()) {
                        if(ls_cod_usuario.equals("")){

                            guardarUsuario();

                        } else {
                            modificarUsuario();
                        }
                    } else {
                        Toast.makeText(
                                activity,
                                "La clave ingresada no coincide con su confirmación",
                                Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    public boolean validar(){
        if(camposVacios()){
            Toast.makeText(
                    activity,
                    "Completa los campos",
                    Toast.LENGTH_LONG).show();
            return true;}
        if(ls_cod_usuario.equals("")) {
            if (claves()) {
                return true;
            }
        }
        return false;

    }
    public boolean claves(){

        String clave = tvClave.getText().toString();
        String confirmacion = tvConfirmar.getText().toString();

        if(clave.isEmpty())
        {
            tvClave.requestFocus();
            Toast.makeText(
                    activity,
                    "Debe ingresar su clave",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        if(confirmacion.isEmpty())
        {
            tvConfirmar.requestFocus();
            Toast.makeText(
                    activity,
                    "Debe ingresar la confirmación de su clave",
                    Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    //funcion MD5

    public static void main(String[] args) {

        String s = "SecretKey20111013000";
        String  res = md5(s);
        System.out.println(res);

    }

    private static String md5(String s) { try {

        // Create MD5 Hash
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        // Create Hex String
        StringBuffer hexString = new StringBuffer();
        for (int i=0; i<messageDigest.length; i++)
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
        return "";

    }

    public void modificarUsuario() {
        final String nombre = tvNombre.getText().toString();
        final String mail = tvMail.getText().toString();
        final String clave = md5(tvClave.getText().toString());
        String fecha_nac = tvFecha.getText().toString();

        SimpleDateFormat inDateFmt = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outDateFmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = inDateFmt.parse(fecha_nac);
            fecha_nac= outDateFmt.format(date);
        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("nombre", nombre);
        map.put("mail", mail);
        map.put("fecha_nac", fecha_nac);
        map.put("clave", clave);
        map.put("id", ls_cod_usuario);


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

        String newURL = Constantes.UPDATE_USUARIO + "?" + encodedParams;

        Log.d(TAG,newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(activity).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuesta(response);
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

    public void guardarUsuario() {

        // Obtener valores actuales de los controles

        final String nombre = tvNombre.getText().toString();;
        final String mail = tvMail.getText().toString();
        final String clave = md5(tvClave.getText().toString());
        final String clave_original = tvClave.getText().toString();
        final String fecha_nac = tvFecha.getText().toString();


        //firebase
        mAuth.addAuthStateListener(mAuthListener);
        mAuth.createUserWithEmailAndPassword(mail, clave_original)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");


                            id_firebase =  mAuth.getCurrentUser().getIdToken(true).toString();

                            HashMap<String, String> map = new HashMap<>();// Mapeo previo

                            map.put("nombre", nombre);
                            map.put("mail", mail);
                            map.put("id_firebase", id_firebase);
                            map.put("fecha_nac", fecha_nac);
                            map.put("clave", clave);

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

                            String newURL = Constantes.INSERT_USUARIO + "?" + encodedParams;

                            // Actualizar datos en el servidor
                            VolleySingleton.getInstance(activity).addToRequestQueue(
                                    myRequest = new JsonObjectRequest(
                                            Request.Method.GET,
                                            newURL,
                                            //jobject,
                                            null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Procesar la respuesta del servidor

                                                    procesarRespuesta(response);
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
                        } else {
                            // If sign in fails, display a message to the user.
                            String ls_error;
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                ls_error = getString(R.string.error_weak_password);
                                tvClave.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                ls_error = getString(R.string.error_invalid_email);
                                tvMail.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                                ls_error = e.getMessage();
                            }

                            Toast.makeText(
                                    activity,
                                    ls_error,
                                    Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }

    private void procesarRespuesta(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            activity,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    activity.setResult(Activity.RESULT_OK);

                    posicion_string = String.valueOf(R.id.nav_home);

                    SharedPreferences settings1 = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = settings1.edit();

                    editor.putString("posicion",posicion_string);
                    editor.apply();

                    editor.commit();

                    Bundle args1 = new Bundle();

                    Fragment fragment = new principalFragment();
                    args1.putInt(principalFragment.ARG_ARTICLES_NUMBER, R.id.nav_home);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, fragment)
                            .commit();


                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            activity,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    activity.setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    activity.finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public boolean compara_clave(){
        String clave ;
        String confirma;

        clave = tvClave.getText().toString();
        confirma = tvConfirmar.getText().toString();

        if (clave.equals(confirma)) {
            return true;
        }else{
            tvClave.requestFocus();
            return  false;
        }

    }

    public boolean camposVacios() {

        String nombre = null;

        nombre = tvNombre.getText().toString();

        if(nombre.isEmpty())
        {
            tvNombre.setHintTextColor(Color.RED);
            tvNombre.requestFocus();
            return true;
        }else {
            tvNombre.setHintTextColor(Color.GRAY);
        }

        String mail = tvMail.getText().toString();

        if(mail.isEmpty())
        {
            tvMail.setHintTextColor(Color.RED);
            tvMail.requestFocus();
            return true;
        }else {
            tvMail.setHintTextColor(Color.GRAY);
        }


        return false;
    }

    public void cargarAdaptador() {
        // Petición GET

        String newURL = Constantes.GET_BY_ID_USUARIO + "?id=" + ls_cod_usuario;
        VolleySingleton.
                getInstance(activity).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                newURL,
                                null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Procesar la respuesta Json
                                        procesarRespuestaCarga(response);
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
    private void procesarRespuestaCarga(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO

                    JSONArray mensaje = response.getJSONArray("usuario");

                    for(int i = 0; i < mensaje.length(); i++)
                    {JSONObject object = mensaje.getJSONObject(i);
                        usuario us = new usuario("","","","","","","");

                        String id = object.getString("id");

                        us.setId(id);

                        String nombre = object.getString("nombre");

                        us.setNombre(nombre);

                        String mail = object.getString("mail");

                        us.setMail(mail);

                        String estadoUsuario = object.getString("estado");

                        us.setEstado(estadoUsuario);

                        String fecha_nac = object.getString("fecha_nac");

                        us.setFecha_nac(fecha_nac);

                        usuarios.add(us);

                    }

                    carga_datos();
                    actualizar_foto();

                    break;
                case "2": // FALLIDO



                    break;
            }

        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }

    }

    void clearGlideCache()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                Glide.get(activity.getApplicationContext()).clearDiskCache();
            }
        }.start();

        Glide.get(activity.getApplicationContext()).clearMemory();
    }
}


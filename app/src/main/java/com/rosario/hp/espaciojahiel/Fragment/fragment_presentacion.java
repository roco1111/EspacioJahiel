package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.rosario.hp.espaciojahiel.Entidades.usuario;
import com.rosario.hp.espaciojahiel.MainActivity;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.DialogUtils;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;
import com.rosario.hp.espaciojahiel.notificaciones.LoginInteractor;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.DefaultRetryPolicy;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_presentacion extends Fragment implements LoginInteractor.Callback{
    private LoginButton face;
    private Button ingreso;
    private Button registro;
    private TextView tvNombre = null;
    private TextView tvClave = null;
    private TextView tvConfirmacion = null;
    private TextView tvMail = null;
    private Button editar_foto;
    private ImageButton button_fecha;
    private TextView tvFecha = null;
    private static final String TAG = fragment_presentacion.class.getSimpleName();
    public static final String ARG_ARTICLES_NUMBER = "fragment_presentacion";
    private CallbackManager callbackManager;
    private ProgressDialog m_Dialog_face = null;
    AccessToken accessToken;
    private AuthCredential credential;
    private JsonObjectRequest myRequest;
    private Gson gson = new Gson();
    private String ls_cod_usuario;
    private ProgressDialog m_Dialog = null;
    private String ls_contrasena;
    private String ls_nombre;
    private String ls_mail;
    private TextView olvidaste;
    private String ls_confirmacion;
    private String ls_fecha;
    private Dialog alerta;
    Toast toast1;
    private LoginInteractor.Callback mCallback;
    private TextInputLayout mEmailError;
    private TextInputLayout mPasswordError;
    private Dialog mIngreso;
    private Dialog mRegistro;
    DatePickerDialog datePickerDialog;
    private FirebaseAuth mFirebaseAuth;
    String id_firebase;
    private FirebaseAuth mAuth = null;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    Activity act;
    private CircleImageView imagen;


    public fragment_presentacion() {}

    @Override
    public void onAuthFailed(String msg) {
        this.showLoginError(msg);
    }
    @Override
    public void onBeUserResolvableError(int errorCode) {
    }
    @Override
    public void onEmailError(String msg) {
        this.setEmailError(msg);
    }

    @Override
    public void onPasswordError(String msg) {
        this.setPasswordError(msg);
    }

    @Override
    public void onAuthSuccess() {
    }
    @Override
    public void onGooglePlayServicesFailed() {
        this.showGooglePlayServicesError();
    }
    @Override
    public void onNetworkConnectFailed() {
    }

    public void setEmailError(String error) {
        mEmailError.setError(error);
    }


    public void showLoginError(String msg) {
        Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
    }

    public void setPasswordError(String error) {
        mPasswordError.setError(error);
    }


    public void showGooglePlayServicesError() {
        Toast.makeText(act,
                "Se requiere Google Play Services para usar la app", Toast.LENGTH_LONG)
                .show();
    }

    public void showNetworkError() {
        Toast.makeText(act,
                "La red no está disponible. Conéctese y vuelva a intentarlo", Toast.LENGTH_LONG)
                .show();
    }


    public interface Callback {
        void onInvokeGooglePlayServices(int codeError);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.act = getActivity();
        if (context instanceof Activity){
            act=(Activity) context;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }


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
                             final Bundle savedInstanceState) {

        //FACE////

        final Context context = this.getContext();

        FacebookSdk.sdkInitialize(this.getContext());
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));


        View v = inflater.inflate(R.layout.activity_presentacion, container, false);
        this.face =  v.findViewById(R.id.buttonFace) ;
        this.ingreso =  v.findViewById(R.id.buttonIngreso) ;
        this.registro =  v.findViewById(R.id.buttonRegistro);


        this.face.setFragment(this);

        List<String> permissionNeeds = Arrays.asList( "email", "public_profile");
        face.setReadPermissions(permissionNeeds);

        this.face.setVisibility(View.INVISIBLE);

        this.face.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override

            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken(), context);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:cancel:" );

            }

            @Override
            public void onError(FacebookException e) {
                Log.d(TAG, "facebook:error:" + e.toString());

            }
        });

        ///FIREBASE////
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        this.registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRegistro = createRegistroDialogo();

                mRegistro.show();

            }
        });

        this.ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIngreso = createLoginDialogo();

                mIngreso.show();
            }
        });


        return v;
    }

    private void handleFacebookAccessToken(AccessToken token, final Context context) {

        Log.d(TAG, "handleFacebookAccessToken:" + token);



        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.v("LoginActivity", response.toString());

                        try {
                            /* successfully output email address from graph request here */

                            try {
                                ls_mail = response.getJSONObject().getString("email");
                            } catch (Exception e) {
                                ls_mail = "";
                                e.printStackTrace();
                            }

                            try {
                                ls_nombre = response.getJSONObject().getString("name");
                            } catch (Exception e) {
                                ls_nombre = "";
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Log.e("MomInvoice", "Error in parsing json fb graph", e);
                        }
                    }
                });


        m_Dialog_face = DialogUtils.showProgressDialog(act,"Iniciando sesión..");
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

        accessToken = token;

        credential = FacebookAuthProvider.getCredential(token.getToken());
        id_firebase = token.getToken();

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            obtenerCodigo(context);

                            //verificar si existe usuario, ir a ventana de login de face
                            m_Dialog_face.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            m_Dialog_face.dismiss();
                            Toast.makeText(act,"Error en login",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
    public void cargarDatos(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_CLAVE + "?mail=" + ls_mail;
        Log.d(TAG,newURL);

        // Realizar petición GET_BY_ID
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuesta(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                )
        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuesta(JSONObject response, Context context) {

        try {
            // Obtener atributo "mensaje"
            String estado = response.getString("estado");


            switch (estado) {
                case "1":
                    // Obtener objeto "usuario"
                    JSONArray mensaje1 = response.getJSONArray("usuario");
                    for(int i = 0; i < mensaje1.length(); i++) {
                        Log.d(TAG, "usuario");
                        //Parsear objeto
                        JSONObject object = mensaje1.getJSONObject(i);

                        // Seteando valores en los views
                        Log.d(TAG, "usuario2");
                        ls_cod_usuario = object.getString("id");
                        ls_nombre = object.getString("nombre");
                        ls_fecha = object.getString("fecha_nac");

                        Intent intent2 = new Intent(act, MainActivity.class);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(act);
                        SharedPreferences.Editor editor = settings.edit();

                        editor.putString("cod_usuario", ls_cod_usuario);
                        editor.putString("mail", ls_mail);
                        editor.putString("nombre", ls_nombre);
                        editor.putString("fecha_nac", ls_fecha);

                        editor.apply();
                        Log.d("Usuario", ls_cod_usuario);
                        actualizar_token(id_firebase);
                        act.startActivity(intent2);
                        //getActivity().finish();
                        editor.commit();
                    }
                    break;

                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            act,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            act,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public AlertDialog createRegistroDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        LayoutInflater inflater = act.getLayoutInflater();

        View v = inflater.inflate(R.layout.activity_datos_usuario, null);


        final Context context = getContext();

        builder.setView(v);

        tvNombre = v.findViewById(R.id.editTextNombre);
        tvClave = v.findViewById(R.id.editTextClave);
        tvConfirmacion = v.findViewById(R.id.editTextConfirmar);
        tvMail = v.findViewById(R.id.editTextMail);
        tvFecha = v.findViewById(R.id.editTextFechaNac);
        editar_foto = v.findViewById(R.id.buttonFoto);
        button_fecha = v.findViewById(R.id.buttonFecha);
        imagen =  v.findViewById(R.id.imageViewfoto);

        button_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ls_fecha = tvFecha.getText().toString();
                Date datetxt1 = null;

                if(ls_fecha.equals("")) {
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

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        editar_foto.setVisibility(View.INVISIBLE);
        imagen.setVisibility(View.INVISIBLE);
        Button signup = v.findViewById(R.id.buttonRegistro);

        String rec = act.getResources().getString(R.string.menu_guardar);
        signup.setText(rec);

        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ls_contrasena = tvClave.getText().toString();
                        ls_confirmacion = tvConfirmacion.getText().toString();
                        ls_nombre = tvNombre.getText().toString();
                        ls_mail = tvMail.getText().toString();
                        ls_fecha = tvFecha.getText().toString();
                        Boolean lb_compara;

                        if( ls_nombre.equals(""))
                        {
                            tvNombre.setHintTextColor(act.getResources().getColor(R.color.menu_mensaje));
                            tvNombre.requestFocus();
                            toast1 = Toast.makeText(act, "Debe ingresar su nombre... " , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;

                        }else {
                            tvNombre.setHintTextColor(act.getResources().getColor(R.color.colorRippleMini));
                        }

                        if( ls_mail.equals(""))
                        {
                            tvMail.setHintTextColor(act.getResources().getColor(R.color.menu_mensaje));
                            tvMail.requestFocus();
                            toast1 = Toast.makeText(act, "Debe ingresar su mail... " , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;

                        }else {
                            tvMail.setHintTextColor(act.getResources().getColor(R.color.colorRippleMini));
                        }

                        if( ls_fecha.equals(""))
                        {
                            tvFecha.setHintTextColor(act.getResources().getColor(R.color.menu_mensaje));
                            tvFecha.requestFocus();
                            toast1 = Toast.makeText(act, "Debe ingresar su fecha de nacimiento... " , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;

                        }else {
                            tvFecha.setHintTextColor(act.getResources().getColor(R.color.colorRippleMini));
                        }

                        if( ls_contrasena.equals("")){
                            tvClave.setHintTextColor(Color.RED);
                            tvClave.requestFocus();
                            toast1 = Toast.makeText(act, "Debe ingresar contraseña... " , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;
                        }else {
                            tvClave.setHintTextColor(act.getResources().getColor(R.color.colorRippleMini));
                        }

                        if( ls_confirmacion.equals("")){
                            tvConfirmacion.setHintTextColor(act.getResources().getColor(R.color.menu_mensaje));
                            tvConfirmacion.requestFocus();
                            toast1 = Toast.makeText(act, "Debe ingresar la confirmación de su clave... " , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;
                        }else {
                            tvConfirmacion.setHintTextColor(act.getResources().getColor(R.color.colorRippleMini));
                        }
                        lb_compara = compara_clave(ls_contrasena, ls_confirmacion);
                        if(!lb_compara){
                            toast1 = Toast.makeText(act, "La clave ingresada no coincide con su confirmación" , Toast.LENGTH_LONG);
                            toast1.setGravity(19, 0, 0);
                            toast1.show();
                            return;
                        }

                        final String clave = md5(tvClave.getText().toString());
                        final String clave_original = tvClave.getText().toString();
                        final String mail = tvMail.getText().toString();
                        //firebase
                        mAuth.addAuthStateListener(mAuthListener);
                        mAuth.createUserWithEmailAndPassword(mail, clave_original)
                                .addOnCompleteListener(act, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            guardarUsuario( context);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            String ls_error = "";
                                            try {

                                                throw Objects.requireNonNull(task.getException());

                                            } catch(FirebaseAuthWeakPasswordException e) {
                                                ls_error = getString(R.string.error_weak_password);
                                                tvClave.requestFocus();
                                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                                ls_error = getString(R.string.error_invalid_email);
                                                tvMail.requestFocus();
                                            } catch(Exception e) {
                                                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                                ls_error = e.getMessage();
                                            }

                                            Toast.makeText(
                                                    act,
                                                    ls_error,
                                                    Toast.LENGTH_LONG).show();

                                        }
                                    }


                                });


                    }

                }
        );



        return builder.create();
    }


    public void guardarUsuarioFace(final Context context) {


        // Sign in success, update UI with the signed-in user's information
        Log.d(TAG, "createUserWithFace:success");

        Log.d("nombre",ls_nombre);
        Log.d("mail",ls_mail);
        Log.d("id_firebase",id_firebase);


        id_firebase =  mAuth.getCurrentUser().getIdToken(true).toString();


        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("nombre", ls_nombre);
        map.put("mail", ls_mail);
        map.put("id_firebase", id_firebase);
        map.put("fecha_nac", "");
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

        voley(newURL,getContext());


    }

    public void guardarUsuario(final Context context) {


        // Sign in success, update UI with the signed-in user's information
        Log.d(TAG, "createUserWithEmail:success");

        id_firebase =  mAuth.getCurrentUser().getIdToken(true).toString();


        SimpleDateFormat inDateFmt = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat outDateFmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = inDateFmt.parse(ls_fecha);
            ls_fecha= outDateFmt.format(date);
        } catch (ParseException ex) {
            System.out.println(ex.toString());
        }


        HashMap<String, String> map = new HashMap<>();// Mapeo previo
        map.put("nombre", ls_nombre);
        map.put("mail", ls_mail);
        map.put("id_firebase", id_firebase);
        map.put("fecha_nac", ls_fecha);
        map.put("clave", ls_contrasena);
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

        voley(newURL,context);

    }

    private void voley(String newURL,final Context context){
        // Actualizar datos en el servidor
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        //jobject,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor

                                procesarRespuestaGuardar(response, context);
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
                100000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuestaGuardar(JSONObject response, Context context) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {
                case "1":

                    cargarDatos(context);

                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            act,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    act.setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public boolean compara_clave(String clave, String confirma){


        if (clave.equals(confirma)) {
            return true;
        }else{
            return  false;
        }

    }

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
    public AlertDialog createLoginDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        LayoutInflater inflater = act.getLayoutInflater();

        View v = inflater.inflate(R.layout.activity_login, null);


        builder.setView(v);

        tvMail =  v.findViewById(R.id.editTextUsuario);
        tvClave =  v.findViewById(R.id.editTextClave);
        olvidaste =  v.findViewById(R.id.olvidaste);

        Button signin =  v.findViewById(R.id.buttonIngreso);

        olvidaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"olvidaste en onClick");
                alerta = onCreateDialog(tvMail.getText().toString());

                alerta.show();
            }
        });



        signin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"en onClick");
                        ls_contrasena = tvClave.getText().toString();
                        ls_mail = tvMail.getText().toString();
                        m_Dialog = DialogUtils.showProgressDialog(act,"Iniciando sesión..");

                        if( ls_mail.equals("")){
                            m_Dialog.dismiss();
                            Log.d(TAG ,"email_vacio");
                            fragment_presentacion.this.toast1 = Toast.makeText(act, "Debe ingresar usuario... " , Toast.LENGTH_LONG);
                            fragment_presentacion.this.toast1.setGravity(19, 0, 0);
                            fragment_presentacion.this.toast1.show();
                            return;
                        }

                        if( ls_contrasena.equals("")){
                            m_Dialog.dismiss();
                            Log.d(TAG ,"pass_vacia");
                            fragment_presentacion.this.toast1 = Toast.makeText(act, "Debe ingresar contraseña... " , Toast.LENGTH_LONG);
                            fragment_presentacion.this.toast1.setGravity(19, 0, 0);
                            fragment_presentacion.this.toast1.show();
                            return;
                        }

                        mFirebaseAuth.signInWithEmailAndPassword(ls_mail, ls_contrasena)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (!task.isSuccessful()) {

                                            try {
                                                Log.d(TAG,"error login");
                                                throw Objects.requireNonNull(task.getException());

                                            } catch (FirebaseAuthInvalidUserException e) {
                                                Log.d(TAG, "Invalid Emaild Id - email :" + ls_mail);
                                                fragment_presentacion.this.toast1 = Toast.makeText(act, "Error al ingresar mail... ", Toast.LENGTH_LONG);
                                                fragment_presentacion.this.toast1.setGravity(19, 0, 0);
                                                fragment_presentacion.this.toast1.show();
                                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                                Log.d(TAG, "Invalid Password - email :" + ls_mail);
                                                fragment_presentacion.this.toast1 = Toast.makeText(act, "Error al ingresar clave... ", Toast.LENGTH_LONG);
                                                fragment_presentacion.this.toast1.setGravity(19, 0, 0);
                                                fragment_presentacion.this.toast1.show();
                                            } catch (FirebaseNetworkException e) {
                                                Log.d(TAG, "error_message_failed_sign_in_no_network");
                                                fragment_presentacion.this.toast1 = Toast.makeText(act, "Error en la red... ", Toast.LENGTH_LONG);
                                                fragment_presentacion.this.toast1.setGravity(19, 0, 0);
                                                fragment_presentacion.this.toast1.show();
                                            } catch (Exception e) {
                                                Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                                            }
                                            m_Dialog.dismiss();
                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                        } else {
                                            String ls_clave;
                                            Log.d(TAG,"successfull login");
                                            id_firebase = FirebaseInstanceId.getInstance().getToken();
                                            credential = EmailAuthProvider.getCredential(ls_mail, ls_contrasena);
                                            cargarDatos(getContext());

                                        }

                                    }
                                });

                        //dismiss();
                    }
                }
        );


        return builder.create();
    }

    public Dialog onCreateDialog( String ls_usuario_ing ) {

        final EditText input = new EditText(getContext());
        if(ls_usuario_ing != null) {
            input.setText(ls_usuario_ing);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        final AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(R.string.dialogo_contrasenia)
                .setView(input)
                .setMessage(R.string.mensaje_contrasenia)
                .setPositiveButton(R.string.dialog_aceptar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int wichButton) {
                                String mail = input.getText().toString();

                                if(!mail.equals("")) {

                                    FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "Email sent.");
                                                        Toast.makeText(act, "Se han enviado instrucciones para resetear su clave", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(act, "Fallo al resetear su clave", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }else{
                                    Toast.makeText(act, "Debe ingresar una dirección de mail para reestablecer la contraseña", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                        });
        return builder.create();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void actualizar_token(String token){
        // TODO: Send any registration to your app's servers.

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id", ls_cod_usuario);
        map.put("id_firebase", token);

        JSONObject jobject = new JSONObject(map);

        // Depurando objeto Json...
        Log.d(TAG, jobject.toString());

        StringBuilder encodedParams = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), "utf-8"));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue(), "utf-8")).toString();
                encodedParams.append('&');
            }
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
        }

        encodedParams.setLength(Math.max(encodedParams.length() - 1, 0));

        String newURL = Constantes.UPDATE_TOKEN + "?" + encodedParams;

        // Actualizar datos en el servidor
        VolleySingleton.getInstance(act).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaActualizar(response);
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
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }
    private void procesarRespuestaActualizar(JSONObject response) {

        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            String mensaje = response.getString("mensaje");

            switch (estado) {

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            act,
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void obtenerCodigo(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_CLAVE + "?mail=" + ls_mail;


        // Realizar petición GET_BY_ID
        VolleySingleton.getInstance(context).addToRequestQueue(
                myRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar respuesta Json
                                procesarRespuesta_datos(response, context);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.getMessage());
                            }
                        }
                )
        );
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                5,//DefaultRetryPolicy.DEFAULT_MAX_RETRIES
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void procesarRespuesta_datos(JSONObject response, Context context) {

        try {
            // Obtener atributo "mensaje"
            String mensaje = response.getString("estado");

            switch (mensaje) {
                case "1":
                    // Obtener objeto "usuario"
                    JSONObject object = response.getJSONObject("usuario");

                    //Parsear objeto
                    usuario datosUsuario = gson.fromJson(object.toString(), usuario.class);

                    // Seteando valores en los views
                    ls_cod_usuario = datosUsuario.getId();
                    ls_fecha = datosUsuario.getFecha_nac();

                    Intent intent2 = new Intent(act,MainActivity.class);
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(act);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("mail", ls_mail);
                    editor.putString("cod_usuario", ls_cod_usuario);
                    editor.putString("nombre", ls_nombre);
                    editor.putString("fecha_nac", ls_fecha);

                    editor.apply();
                    actualizar_token(id_firebase);
                    act.startActivity(intent2);
                    // getActivity().finish();
                    editor.commit();
                    break;

                case "2":
                    guardarUsuarioFace(getContext());
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            act,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

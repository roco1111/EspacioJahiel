package com.rosario.hp.espaciojahiel.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.rosario.hp.espaciojahiel.Entidades.usuario;
import com.rosario.hp.espaciojahiel.MainActivity;
import com.rosario.hp.espaciojahiel.R;
import com.rosario.hp.espaciojahiel.include.DialogUtils;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import com.rosario.hp.espaciojahiel.insertUsuario;
import com.rosario.hp.espaciojahiel.notificaciones.LoginInteractor;


public class login extends Fragment implements LoginInteractor.Callback{
    public static final String ARG_ARTICLES_NUMBER = "login";
    private static final String TAG = login.class.getSimpleName();
    private Button btnIngresar = null;
    private Button ivRegistro = null;
    private TextView tvUsuario = null;
    private TextView tvClave = null;
    private Gson gson = new Gson();
    String ls_usuario_ing;
    String ls_id_usuario;
    String ls_nombre;
    String id_firebase;
    String ls_fecha_nac;
    Toast toast1;
    private JsonObjectRequest myRequest;
    private TextInputLayout mEmailError;
    private TextInputLayout mPasswordError;
    private Callback mCallback;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog m_Dialog = null;
    private ProgressDialog m_Dialog_face = null;
    private TextView olvidaste;
    private Dialog alerta;

    private CallbackManager callbackManager;
    private AuthCredential credential;
    AccessToken accessToken;

    Activity act;
    public login() {}


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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        //FACE////

        final Context context = this.getContext();

        callbackManager = CallbackManager.Factory.create();

        View v = inflater.inflate(R.layout.activity_login, container, false);
        this.btnIngresar =  v.findViewById(R.id.buttonIngreso);
        this.ivRegistro =  v.findViewById(R.id.buttonRegistro);
        this.tvUsuario =  v.findViewById(R.id.editTextUsuario);
        this.tvClave =  v.findViewById(R.id.editTextClave);
        this.mEmailError =  v.findViewById(R.id.login_usuario_container);
        this.mPasswordError = v.findViewById(R.id.login_password_container);
        this.olvidaste =  v.findViewById(R.id.olvidaste);


        ///FIREBASE////
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        //mLoginInteractor = new LoginInteractor(getContext(),mFirebaseAuth);

        //extra = getArguments().getString(Constantes.EXTRA_ID);

        this.olvidaste.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"olvidaste en onClick");
                alerta = onCreateDialog();

                alerta.show();
            }
        });


        this.btnIngresar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                Log.d(TAG,"en onClick");
                m_Dialog = DialogUtils.showProgressDialog(getActivity(),"Iniciando sesión..");
                String ls_contrasena = tvClave.getText().toString();
                String ls_usuario = tvUsuario.getText().toString();

                if( ls_usuario.equals("")){
                    m_Dialog.dismiss();
                    Log.d(TAG ,"email_vacio");
                    login.this.toast1 = Toast.makeText(login.this.getContext(), "Debe ingresar usuario... " , Toast.LENGTH_LONG);
                    login.this.toast1.setGravity(19, 0, 0);
                    login.this.toast1.show();
                    return;
                }
                if( ls_contrasena.equals("")){
                    m_Dialog.dismiss();
                    Log.d(TAG ,"pass_vacia");
                    login.this.toast1 = Toast.makeText(login.this.getContext(), "Debe ingresar contraseña... " , Toast.LENGTH_LONG);
                    login.this.toast1.setGravity(19, 0, 0);
                    login.this.toast1.show();
                    return;
                }

                    mFirebaseAuth.signInWithEmailAndPassword(ls_usuario, ls_contrasena)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()) {

                                        try {
                                            throw task.getException();

                                        } catch (FirebaseAuthInvalidUserException e) {
                                            Log.d(TAG, "Invalid Emaild Id - email :" + tvUsuario.getText().toString());
                                            login.this.toast1 = Toast.makeText(login.this.getContext(), "Error al ingresar mail... ", Toast.LENGTH_LONG);
                                            login.this.toast1.setGravity(19, 0, 0);
                                            login.this.toast1.show();
                                        } catch (FirebaseAuthInvalidCredentialsException e) {
                                            Log.d(TAG, "Invalid Password - email :" + tvUsuario.getText().toString());
                                            login.this.toast1 = Toast.makeText(login.this.getContext(), "Error al ingresar clave... ", Toast.LENGTH_LONG);
                                            login.this.toast1.setGravity(19, 0, 0);
                                            login.this.toast1.show();
                                        } catch (FirebaseNetworkException e) {
                                            Log.d(TAG, "error_message_failed_sign_in_no_network");
                                            login.this.toast1 = Toast.makeText(login.this.getContext(), "Error en la red... ", Toast.LENGTH_LONG);
                                            login.this.toast1.setGravity(19, 0, 0);
                                            login.this.toast1.show();
                                        } catch (Exception e) {
                                            Log.e(TAG, e.getMessage());
                                        }
                                        m_Dialog.dismiss();
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    } else {
                                        String ls_clave;

                                        ls_clave = tvClave.getText().toString();
                                        ls_usuario_ing = tvUsuario.getText().toString();
                                        id_firebase = FirebaseInstanceId.getInstance().getToken();
                                        credential = EmailAuthProvider.getCredential(ls_usuario_ing, ls_clave);
                                        cargarDatos(context);

                                    }

                                }
                            });

            }
        });

        this.ivRegistro.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),insertUsuario.class);

                (getActivity()).startActivity(intent);

            }
        });
        return v;

    }

    @Override
    public  void onPause(){
        super.onPause();
        if(( m_Dialog != null) && m_Dialog.isShowing())
            m_Dialog.dismiss();
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

    public void cargarDatos(final Context context) {

        // Añadir parámetro a la URL del web service
        String newURL = Constantes.GET_BY_CLAVE + "?mail=" + ls_usuario_ing;

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
            String mensaje = response.getString("estado");

            switch (mensaje) {
                case "1":
                    // Obtener objeto "cliente"
                    JSONObject object = response.getJSONObject("usuario");

                    //Parsear objeto
                    usuario codigoUsuario = gson.fromJson(object.toString(), usuario.class);

                    // Seteando valores en los views
                    ls_id_usuario = codigoUsuario.getId();
                    ls_nombre = codigoUsuario.getNombre();
                    ls_fecha_nac = codigoUsuario.getFecha_nac();

                    Intent intent2 = new Intent(context,MainActivity.class);
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = settings.edit();

                    editor.putString("id", ls_id_usuario);
                    editor.putString("nombre", ls_nombre);
                    editor.putString("mail", ls_usuario_ing);
                    editor.putString("fec_nac", ls_fecha_nac);
                    editor.apply();
                    actualizar_token(id_firebase);
                    context.startActivity(intent2);
                    editor.commit();



                    break;

                case "2":
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;

                case "3":
                    String mensaje3 = response.getString("mensaje");
                    Toast.makeText(
                            context,
                            mensaje3,
                            Toast.LENGTH_LONG).show();
                    break;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            act=(Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void setEmailError(String error) {
        mEmailError.setError(error);
    }


    public void showLoginError(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    public void setPasswordError(String error) {
        mPasswordError.setError(error);
    }


    public void showGooglePlayServicesError() {
        Toast.makeText(getActivity(),
                "Se requiere Google Play Services para usar la app", Toast.LENGTH_LONG)
                .show();
    }

    public void showNetworkError() {
        Toast.makeText(getActivity(),
                "La red no está disponible. Conéctese y vuelva a intentarlo", Toast.LENGTH_LONG)
                .show();
    }


    public interface Callback {
        void onInvokeGooglePlayServices(int codeError);
    }
    private void actualizar_token(String token){
        // TODO: Send any registration to your app's servers.

        HashMap<String, String> map = new HashMap<>();// Mapeo previo

        map.put("id", ls_id_usuario);
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
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(
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
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Dialog onCreateDialog() {

        final EditText input = new EditText(getContext());
        ls_usuario_ing = tvUsuario.getText().toString();
        if(ls_usuario_ing != null) {
            input.setText(ls_usuario_ing);
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialogo_contrasenia)
                .setView(input)
                .setMessage(R.string.mensaje_contrasenia)
                .setPositiveButton(R.string.dialog_aceptar,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int wichButton) {
                                String mail = input.getText().toString();

                                FirebaseAuth.getInstance().sendPasswordResetEmail(mail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "Email sent.");
                                                    Toast.makeText(getContext(),"Se han enviado instrucciones para resetear su clave",Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(getContext(),"Fallo al resetear su clave",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });


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
                            ls_usuario_ing = response.getJSONObject().getString("email");


                        } catch (Exception e) {
                            Log.e("MomInvoice", "Error in parsing json fb graph", e);
                        }
                    }
                });


        m_Dialog_face = DialogUtils.showProgressDialog(getActivity(),"Iniciando sesión..");
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();

        accessToken = token;



        credential = FacebookAuthProvider.getCredential(token.getToken());
        id_firebase = token.getToken();

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();

                            cargarDatos(context);
                            m_Dialog_face.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            m_Dialog_face.dismiss();
                            Toast.makeText(context,"Error en login",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

}


package com.rosario.hp.espaciojahiel;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FCMInstanceIdService extends FirebaseMessagingService {

  private static final String TAG = "MyFirebaseIIDService";
  private String codUsuario;


  public void onTokenRefresh() {
    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    Log.d(TAG, "Refreshed token: " + refreshedToken);

    sendRegistrationToServer(refreshedToken);
  }

  private void sendRegistrationToServer(String token) {
    // TODO: Send any registration to your app's servers.

    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    SharedPreferences.Editor editor = settings.edit();

    codUsuario = settings.getString("cod_usuario","0");
    HashMap<String, String> map = new HashMap<>();// Mapeo previo

    map.put("id", codUsuario);
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
    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
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
                Map<String, String> headers = new HashMap<String, String>();
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
        case "1":
          // Mostrar mensaje
          Toast.makeText(
                  getApplicationContext(),
                  mensaje,
                  Toast.LENGTH_LONG).show();
          break;

        case "2":
          // Mostrar mensaje
          Toast.makeText(
                  getApplicationContext(),
                  mensaje,
                  Toast.LENGTH_LONG).show();
          // Enviar código de falla
          break;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }
}
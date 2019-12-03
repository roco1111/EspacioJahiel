package com.rosario.hp.espaciojahiel;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import com.rosario.hp.espaciojahiel.include.Constantes;
import com.rosario.hp.espaciojahiel.include.VolleySingleton;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FCMService extends  FirebaseMessagingService {

  private static final String TAG = "MyFirebaseMsgService";
  private String codUsuario;
  private String titulo;
  private String texto;
  private String ls_fecha;
  private String ls_hora;
  Calendar c1 = Calendar.getInstance();


  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Create and show notification



    sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"));
    sendNewPromoBroadcast(remoteMessage);
  }

  private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
    Intent intent = new Intent(MainActivity.ACTION_NOTIFY_NEW_PROMO);
    titulo = remoteMessage.getData().get("title");
    texto = remoteMessage.getData().get("body");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    ls_fecha = sdf.format(Calendar.getInstance().getTime());
    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm");
    ls_hora = sdf2.format(Calendar.getInstance().getTime());
    intent.putExtra("title", titulo);
    intent.putExtra("description", texto);
    intent.putExtra("fecha", ls_fecha);
    intent.putExtra("hora", ls_hora);


    //guardar_notificacion();

    LocalBroadcastManager.getInstance(getApplicationContext())
            .sendBroadcast(intent);
  }

  private void sendNotification(String messageBody, String title) {

    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.icono_toolbar)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setColor(getResources().getColor(R.color.colorPrimary))
            .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
  }
  public void guardar_notificacion(){
    HashMap<String, String> map1 = new HashMap<>();

    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    codUsuario = settings.getString("cod_usuario","0");

    map1.put("titulo", titulo);
    map1.put("texto", texto);
    map1.put("fecha", ls_fecha);
    map1.put("hora", ls_hora);
    map1.put("id_usuario", codUsuario);

    // Crear nuevo objeto Json basado en el mapa
    JSONObject jobject = new JSONObject(map1);

    StringBuilder encodedParams1 = new StringBuilder();
    try {
      for (Map.Entry<String, String> entry : map1.entrySet()) {
        encodedParams1.append(URLEncoder.encode(entry.getKey(), "utf-8"));
        encodedParams1.append('=');
        encodedParams1.append(URLEncoder.encode(entry.getValue(), "utf-8"));
        encodedParams1.append('&');
      }
    } catch (UnsupportedEncodingException uee) {
      throw new RuntimeException("Encoding not supported: " + "utf-8", uee);
    }

    encodedParams1.setLength(Math.max(encodedParams1.length() - 1, 0));

    Log.d(TAG, jobject.toString());

    String newURL = Constantes.INSERT_NOTIFICACION + "?" + encodedParams1;

    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
            new JsonObjectRequest(
                    Request.Method.GET,
                    newURL,
                    null,
                    new Response.Listener<JSONObject>() {
                      @Override
                      public void onResponse(JSONObject response) {
                        // Procesar la respuesta del servidor
                        procesarRespuestaNotificacion(response);
                      }
                    },
                    new Response.ErrorListener() {
                      @Override
                      public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error Volley: " + error.getMessage());
                        Toast.makeText(
                                getApplicationContext(),
                                "Error Volley: " + error.getMessage(),
                                Toast.LENGTH_LONG).show();

                      }
                    }

            )
    );
  }
  private void procesarRespuestaNotificacion(JSONObject response) {

    try {
      // Obtener estado
      String estado = response.getString("estado");
      // Obtener mensaje
      String mensaje = response.getString("mensaje");

      switch (estado) {

        case "1":
          Toast.makeText(
                  getApplicationContext(),
                  "Notificación guardada",
                  Toast.LENGTH_LONG).show();
          break;
        case "2":
          // Mostrar mensaje
          Toast.makeText(
                  getApplicationContext(),
                  mensaje,
                  Toast.LENGTH_LONG).show();
          break;

      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }





}
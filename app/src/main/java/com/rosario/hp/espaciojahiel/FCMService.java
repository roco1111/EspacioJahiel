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






}
package com.rosario.hp.espaciojahiel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;

import android.util.Log;
import android.widget.Switch;
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
  private String tipo;
  private String ls_fecha;
  private String ls_hora;
  Calendar c1 = Calendar.getInstance();


  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    // Create and show notification

    sendNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData().get("tipo"));
    sendNewPromoBroadcast(remoteMessage);
  }

  private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {

    titulo = remoteMessage.getData().get("title");
    texto = remoteMessage.getData().get("body");
    tipo = remoteMessage.getData().get("tipo");

    Intent intent = null;

    switch (tipo){
      case "Imagen":
        intent = new Intent(activity_imagen.ACTION_NOTIFY_NEW_PROMO);
        break;
      case "evento":
        intent = new Intent(activity_evento.ACTION_NOTIFY_NEW_PROMO);
        break;
      case "notificacion":
        intent = new Intent(activity_videos_programa.ACTION_NOTIFY_NEW_PROMO);
        break;
    }
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

  private void sendNotification(String messageBody, String title, String tipo) {

    Intent intent = null;

    switch (tipo){
      case "Imagen":
        intent = new Intent(this, activity_imagen.class);
        break;
      case "evento":
        intent = new Intent(this, activity_evento.class);
        break;
      case "notificacion":
        intent = new Intent(this, activity_videos_programa.class);
        break;
    }

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder builder = null;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      int importance = NotificationManager.IMPORTANCE_DEFAULT;
      NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
      NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
      notificationManager.createNotificationChannel(notificationChannel);

      builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
    } else {
      builder = new NotificationCompat.Builder(getApplicationContext());
    }

    builder = builder
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

    notificationManager.notify(0 /* ID of notification */, builder.build());
  }

}
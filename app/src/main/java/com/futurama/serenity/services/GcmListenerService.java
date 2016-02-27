package com.futurama.serenity.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.futurama.serenity.R;
import com.futurama.serenity.activities.MainActivity;
import com.futurama.serenity.utils.Session;

import java.util.Date;

/**
 * Created by wilfried on 25/09/2015.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    private String TAG = "Serenity Notification";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        //String title = data.getString("title");
        String title = "Serenity";
        String message = data.getString("message");
        String collapseKey = data.getString("collapse_key");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        Log.i(TAG, "Notification data\n" + data.toString());

        if (from.startsWith("/topics/")) {
            // message received from some topic.
            //sendNotification(title, message);
        } else {
            Intent intent;
            // normal downstream message.
            switch (collapseKey){
                case "SYNCHRO_PARAMS":
                    Session session = new Session();
                    session.getEditor().putBoolean("synchTypeFacture", true);
                    session.getEditor().putBoolean("synchModeReglement", true);
                    session.getEditor().putBoolean("synchPrestattion", true);
                    session.getEditor().commit();
                    intent = new Intent(this, MainActivity.class);
                    break;
                default:
                    intent = new Intent(this, MainActivity.class);
                    break;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendNotification(intent, title, message);
        }
    }

    private void sendNotification(Intent intent, String title, String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_plus_signin_btn_icon_light)
                .setContentTitle(title)
                .setContentText(message)
                .setSubText("Details")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) new Date().getTime(), notificationBuilder.build());
    }
}

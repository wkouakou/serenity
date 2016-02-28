package com.futurama.serenity.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.futurama.serenity.MainApplication;
import com.futurama.serenity.R;
import com.futurama.serenity.activities.MainActivity;
import com.futurama.serenity.activities.SmsActivity;
import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.models.WhiteList;
import com.futurama.serenity.utils.Config;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.sql.Date;

/**
 * Created by wilfried on 27/02//2015.
 */
public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    Session session;
    Context mContext;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = MainApplication.getInstance().getApplicationContext();
        // Get the data (SMS data) bound to intent
        Bundle bundle = intent.getExtras();
        FlowManager.init(context);
        session = new Session();

        SmsMessage[] msgs = null;

        if (bundle != null) {
            // Retrieve the SMS Messages received
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            StringBuilder stringBuilder = new StringBuilder();
            Long receiveTime = 0L;
            String senderAddress = "";
            // For every SMS message received
            for (int i=0; i < msgs.length; i++) {
                // Convert Object array
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                //Timestamp
                if(receiveTime == 0){
                    receiveTime = msgs[i].getTimestampMillis();
                }
                //Sender address
                if(senderAddress.isEmpty()){
                    senderAddress = msgs[i].getOriginatingAddress();
                }
                stringBuilder.append(msgs[i].getMessageBody());
            }
            try {//Vérification du numéro de l'expéditeur
                verifySms(senderAddress, receiveTime, stringBuilder.toString());
            }
            catch(Exception e){
                Log.e("SMS receive error ", "Msg : "+e.getMessage());
            }
        }
    }

    private void verifySms(String numeroSuspect, final long dateReception, final String msg){
        if(!WhiteList.isClean(numeroSuspect) && !Utils.isClean(msg)){//Numero et contenu du message suspects
            SmsSuspect smsSuspect = new SmsSuspect();
            smsSuspect.setDatereception(new Date(dateReception));
            smsSuspect.setMsg(msg);
            smsSuspect.setNumerosuspect(numeroSuspect);
            smsSuspect.save();

            //Notify user
            Intent intentSms = new Intent(mContext, SmsActivity.class);
            sendNotification(intentSms, "Serenity", "Vous avez réçu un SMS suspect");
        }
    }

    private void sendNotification(Intent intent, String title, String message) {
        //Context mContext = MainApplication.getInstance().getApplicationContext();
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext)
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
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) new java.util.Date().getTime(), notificationBuilder.build());
    }
}
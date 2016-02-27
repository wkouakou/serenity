package com.futurama.serenity.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.futurama.serenity.utils.Session;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by wilfried on 27/02//2015.
 */
public class SmsReceiver extends BroadcastReceiver {
    private String TAG = SmsReceiver.class.getSimpleName();
    Session session;

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
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
                postSms(senderAddress, receiveTime, stringBuilder.toString());
            }
            catch(Exception e){
                Log.e("SMS receive error ", "Msg : "+e.getMessage());
            }
        }
    }

    private void postSms(final String sender, final long date, final String msg){

    }
}
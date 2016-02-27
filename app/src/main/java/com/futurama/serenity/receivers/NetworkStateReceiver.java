package com.futurama.serenity.receivers;

/**
 * Created by wilfried on 23/02/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;


import com.futurama.serenity.utils.Session;
import com.raizlabs.android.dbflow.config.FlowManager;

public class NetworkStateReceiver extends BroadcastReceiver
{
    private String TAG = NetworkStateReceiver.class.getSimpleName();
    Session session;

    // post event if there is no Internet connection
    public void onReceive(Context context, Intent intent)
    {
        session = new Session();
        //super.onReceive(context, intent);
        if(intent.getExtras()!=null)
        {
            FlowManager.init(context);
            NetworkInfo ni=(NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(ni!=null && ni.getState()==NetworkInfo.State.CONNECTED)
            {
                //TODO Envoi des sms enregistrés au serveur
                Toast.makeText(context, "Connexion établie", Toast.LENGTH_LONG).show();
                Log.e("INTERNET", "there is Internet connection");
                //Toast.makeText(context, "Si hay",1).show();
                //MyApp.getBus().post(new NetworkStateChanged(true) );
                // there is Internet connection
            } else{
                Toast.makeText(context, "Connexion perdue", Toast.LENGTH_LONG).show();
                Log.e("Internet receiver ", "Connexion perdue");
            }
                //MyApp.getBus().post(new NetworkStateChanged(false) );
            if(intent .getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE))
            {
                //Toast.makeText(context, "Connexion échouée", Toast.LENGTH_LONG).show();
                Log.e("Internet notice ", "Connexion échouée");
                // no Internet connection, send network state changed
               // MyApp.getBus().post(new NetworkStateChanged(false) );
                //EventBus.getDefault().post(new NetworkStateChanged(false));
            }
        }
    }
}

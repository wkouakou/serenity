package com.futurama.serenity.receivers;

/**
 * Created by wilfried on 23/02/2016.
 */
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import com.futurama.serenity.MainApplication;
import com.futurama.serenity.models.SmsSuspect;
import com.futurama.serenity.services.ClientService;
import com.futurama.serenity.utils.Config;
import com.futurama.serenity.utils.GenericObjectResult;
import com.futurama.serenity.utils.Session;
import com.futurama.serenity.utils.Utils;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
                //Envoi des sms enregistrés au serveur
                List<SmsSuspect> smsSuspectList = SmsSuspect.getSms();
                for(SmsSuspect smsSuspect : smsSuspectList){
                    sendSms(smsSuspect);
                }
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

    private void sendSms(SmsSuspect smsSuspect){
        Retrofit client = MainApplication.getRetrofit();
        ClientService service = client.create(ClientService.class);
        Call<GenericObjectResult<SmsSuspect>> call = service.transfererMessage(session.getSharedPref().getString("token", "*******************"), session.getSharedPref().getString("uid",""),
                session.getSharedPref().getFloat("latitude", 0), session.getSharedPref().getFloat("longitude", 0), Utils.dateToString(smsSuspect.getDatereception(), Config.DATE_TIME_PATTERN),
                smsSuspect.getNumerosuspect(), session.getSharedPref().getString("numero", ""), smsSuspect.getMsg(), false);
        call.enqueue(new Callback<GenericObjectResult<SmsSuspect>>() {
            @Override
            public void onResponse(Response<GenericObjectResult<SmsSuspect>> response) {
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    GenericObjectResult<SmsSuspect> result = response.body();
                    if (result.getTotal() > 0) {
                        //smsSuspect.
                        Log.e("Synchro", result.getRow().toString());
                    }
                    Log.i("RegistrationID", result.toString());
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                    Log.i("Response code", String.valueOf(response.code()));
                    Log.i("Response message", response.message());
                    Log.i("Response header", response.headers().toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

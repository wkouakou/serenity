package com.futurama.serenity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.futurama.serenity.utils.Config;
import com.futurama.serenity.utils.DateDeserializer;
import com.futurama.serenity.utils.FontAwesomeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.joanzapata.iconify.Iconify;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.otto.Bus;

import java.util.Date;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by wilfried on 06/01/2016.
 */
public class MainApplication extends Application {

    public static String BASE_URL;
    private static MainApplication sInstance;

    private static Retrofit retrofit;
    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        bus = new Bus();
        Iconify.with(new FontAwesomeModule());
        FlowManager.init(this);
        if(Config.MODE.equals("PROD")){
            BASE_URL = Config.PROD_BASE_URL;
        }
        else{
            BASE_URL = Config.DEV_BASE_URL;
        }
        bus.register(this);
    }

    public synchronized static MainApplication getInstance() {
        return sInstance;
    }

    public synchronized static Bus getBus() {
        return bus;
    }

    public synchronized static Retrofit getRetrofit() {
        if(retrofit == null){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Date.class, new DateDeserializer())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static boolean isNetworkAvailable()
    {
        if(!Config.MODE.equals("PROD")){
            return true;
        }
        Context context = MainApplication.getInstance().getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;
        else
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
            {
                for (int i = 0; i <info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}

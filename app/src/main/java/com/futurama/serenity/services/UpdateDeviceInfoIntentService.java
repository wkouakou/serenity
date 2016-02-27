package com.futurama.serenity.services;

import android.app.IntentService;
import android.content.Intent;

import com.futurama.serenity.utils.Session;


/**
 * Created by Wilfried on 18/05/2015.
 */
public class UpdateDeviceInfoIntentService extends IntentService {
    private final String TAG = UpdateDeviceInfoIntentService.class.getSimpleName();
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * param name Used to name the worker thread, important only for debugging.
     */
    public UpdateDeviceInfoIntentService() {
        super("UpdateDeviceInfoIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Session session = new Session();
        //TODO

    }
}

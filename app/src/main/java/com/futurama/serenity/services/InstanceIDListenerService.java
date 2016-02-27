package com.futurama.serenity.services;

import android.content.Intent;

/**
 * Created by wilfried on 25/09/2015.
 */
public class InstanceIDListenerService extends com.google.android.gms.iid.InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}

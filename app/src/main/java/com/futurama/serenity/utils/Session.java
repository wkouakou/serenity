package com.futurama.serenity.utils;

import android.content.SharedPreferences;

import com.futurama.serenity.MainApplication;

/**
 * Created by wilfried on 14/10/2015.
 */
public class Session {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public Session() {
        this.sharedPref = MainApplication.getInstance().getApplicationContext().getSharedPreferences(Config.SESSION_NAME, MainApplication.getInstance().MODE_PRIVATE);
        this.editor = this.sharedPref.edit();
    }

    public Session(SharedPreferences sharedPreferences) {
        this.sharedPref = sharedPreferences;
        this.editor = this.sharedPref.edit();
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }
}

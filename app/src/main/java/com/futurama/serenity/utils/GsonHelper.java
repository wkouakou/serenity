package com.futurama.serenity.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Wilfried on 19/05/2015.
 */
public class GsonHelper {

    public GsonHelper() {
        super();
    }

    public <T> T fromGson(String string, Class<T> type){
        Gson gson = new GsonBuilder()
                .setDateFormat(Config.DATE_TIME_PATTERN)
                .create();
        return gson.fromJson(string, type);
    }

    public static String toJsonString(Object obj){
        Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm:ss").serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(obj);
    }
}

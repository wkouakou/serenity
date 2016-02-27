package com.futurama.serenity.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by wilfried on 13/01/2016.
 */
public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement element, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String date = element.getAsString();
        if(date == "" || date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(Config.DATE_PATTERN);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }
}

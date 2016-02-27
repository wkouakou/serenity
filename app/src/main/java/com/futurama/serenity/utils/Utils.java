package com.futurama.serenity.utils;

import android.widget.Toast;


import com.futurama.serenity.MainApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by Wilfried on 16/05/2015.
 */
public class Utils {
    public static String TAG = "Serenity";

    public static void toast(String msg){
        Toast.makeText(MainApplication.getInstance().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    public static Date stringToDate(String dateString, String format) {
        try {
            return  new SimpleDateFormat(format).parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String dateToString(Date date, String format) {
        try {
            return  new SimpleDateFormat(format).format(date);
        } catch (Exception ex){
            return "";
        }

    }

    public static String cleanNumber(String phoneNumber) {
        return phoneNumber.replaceAll("\\s+", "").replace("+","").replace("$00","");
    }

    public static boolean isClean(String msg) {
        int j = 0;
        for (int i = 0; i < Config.LISTMOTSUSPECT.size(); i++){
            if(msg.contains(Config.LISTMOTSUSPECT.get(i))){
                j++;
            }
        }
        if(j > Config.niveauAlertSms){
            return false;
        }
        else{
            return true;
        }
    }
}

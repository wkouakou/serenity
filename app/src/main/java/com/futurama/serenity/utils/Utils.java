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
    public static String TAG = "NewStyle";

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
}

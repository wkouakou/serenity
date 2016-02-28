package com.futurama.serenity.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Wilfried on 14/10/2015.
 */
public class Config {

    public static final List<String> LISTMOTSUSPECT =  Arrays.asList(
            "contact",
            "heritage",
            "transfert",
            "huissier",
            "gagne",
            "tombola",
            "concours",
            "annonce",
            "donation",
            "lotterie",
            "remboursement",
            "colis",
            "indemnise",
            "retirer",
            "interpol",
            "fbi",
            "cia",
            "appel",
            "impot",
            "cheque",
            "maitre",
            "maladie",
            "concessionnaire",
            "fortune",
            "interesse"
    );
    public static final String MODE = "PROD"; //DEV | PROD
    public static final String PROD_BASE_URL = "http:192.168.11.100/"; //http://52.3.214.184:8080/rkimmo/
    public static final String DEV_BASE_URL = "http://192.168.11.100";
    public static final int niveauAlertSms = 2; //

    public static final String CONTENT_APPLICATION_JSON = "application/json";
    public static final String CONTENT_APPLICATION_STANDARD = "application/x-www-form-urlencoded";

    public static final String SESSION_NAME = "SERENITY_SESS";
    public static final String SENDER_ID = "1067911358612";

    //GOOGLE PLAY SERVICES
    public static final String PROPERTY_APP_VERSION = "1.0.0";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int UPDATE_INTERVAL = 1000*60*2; // 2 min
    public static final int FATEST_INTERVAL = 5000; // 5 sec
    public static final int DISPLACEMENT = 500; // 500 meters

    public static final String DATE_PATTERN = "dd-MM-yyyy";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";
}

package com.futurama.serenity.models;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by wilfried on 27/02/2016.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    public static final String NAME = "Serenity";

    public static final int VERSION = 1;
}

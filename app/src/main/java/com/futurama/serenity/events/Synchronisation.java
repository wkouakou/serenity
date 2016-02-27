package com.futurama.serenity.events;

/**
 * Created by wilfried on 25/01/2016.
 */
public class Synchronisation {

    private String type;

    public Synchronisation() {
    }

    public Synchronisation(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Synchronisation{" +
                "type='" + type + '\'' +
                '}';
    }
}

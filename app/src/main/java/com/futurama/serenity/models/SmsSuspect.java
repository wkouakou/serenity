package com.futurama.serenity.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;

/**
 * Created by wilfried on 06/01/2016.
 */
@ModelContainer
@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.IGNORE)
public class SmsSuspect extends BaseModel {

    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    private Date datereception;

    @Column
    private String numerosuspect;

    @Column
    private String numeroexpediteur;

    @Column
    private String msg;

    @Column
    private float latitude;

    @Column
    private float longitude;

    @Column
    boolean envoyerposition;

    public SmsSuspect() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDatereception() {
        return datereception;
    }

    public void setDatereception(Date datereception) {
        this.datereception = datereception;
    }

    public String getNumerosuspect() {
        return numerosuspect;
    }

    public void setNumerosuspect(String numerosuspect) {
        this.numerosuspect = numerosuspect;
    }

    public String getNumeroexpediteur() {
        return numeroexpediteur;
    }

    public void setNumeroexpediteur(String numeroexpediteur) {
        this.numeroexpediteur = numeroexpediteur;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public boolean isEnvoyerposition() {
        return envoyerposition;
    }

    public void setEnvoyerposition(boolean envoyerposition) {
        this.envoyerposition = envoyerposition;
    }

    @Override
    public String toString() {
        return "SmsSuspect{" +
                "id=" + id +
                ", datereception=" + datereception +
                ", numerosuspect='" + numerosuspect + '\'' +
                ", numeroexpediteur='" + numeroexpediteur + '\'' +
                ", msg='" + msg + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", envoyerposition=" + envoyerposition +
                '}';
    }
}

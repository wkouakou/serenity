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
public class User extends BaseModel {

    @PrimaryKey(autoincrement = false)
    int id;

    @Column
    private String numero;

    @Column
    private String uid;

    @Column
    private String email;

    public User() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

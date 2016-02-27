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
public class Message extends BaseModel {

    @PrimaryKey(autoincrement = false)
    int id;

    @Column
    private Date dateenvoi;

    @Column
    private String msg;

    @Column
    private int type;

    public Message() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateenvoi() {
        return dateenvoi;
    }

    public void setDateenvoi(Date dateenvoi) {
        this.dateenvoi = dateenvoi;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", dateenvoi=" + dateenvoi +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}

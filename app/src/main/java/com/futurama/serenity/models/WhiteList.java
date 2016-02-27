package com.futurama.serenity.models;

import com.futurama.serenity.utils.Utils;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Date;

/**
 * Created by wilfried on 06/01/2016.
 */
@ModelContainer
@Table(database = AppDatabase.class, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.IGNORE)
public class WhiteList extends BaseModel {

    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    @Unique
    private String numero;

    public WhiteList() {
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

    @Override
    public String toString() {
        return "WhiteList{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                '}';
    }

    public static boolean isClean(String numero){
        WhiteList whiteList = SQLite.select().from(WhiteList.class)
                .where(WhiteList_Table.numero.eq(Utils.cleanNumber(numero)))
                .querySingle();

        return whiteList == null ? false : true;
    }
}

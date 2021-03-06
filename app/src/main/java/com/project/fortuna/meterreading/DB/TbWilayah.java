package com.project.fortuna.meterreading.DB;

import android.content.Context;
import android.database.Cursor;

import com.project.fortuna.meterreading.annotation.Column;
import com.project.fortuna.meterreading.annotation.PrimaryKey;
import com.project.fortuna.meterreading.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akautsar Efendi on 2/28/2018.
 */

@Table(name = "tabelwilayah")
public class TbWilayah extends BaseDAO {
    public TbWilayah(Context context) {
        super(context);
    }

    @PrimaryKey
    @Column
    public String kdWilayah;

    @Column
    public String nmWilayah;

    public String getKdWilayah() {
        return kdWilayah;
    }

    public void setKdWilayah(String kdWilayah) {
        this.kdWilayah = kdWilayah;
    }

    public String getNmWilayah() {
        return nmWilayah;
    }

    public void setNmWilayah(String nmWilayah) {
        this.nmWilayah = nmWilayah;
    }

    @Override
    public List<TbWilayah> retrieveAll() {
        List<TbWilayah> w = new ArrayList<TbWilayah>();

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                TbWilayah m = new TbWilayah(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(m, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                w.add(m);
            }while(cursor.moveToNext());
        }
        db.close();
        return w;
    }



    @Override
    public TbWilayah retrieveByID() {
        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
        if(cursor != null) cursor.moveToFirst();
        String[] columnNames = cursor.getColumnNames();
        for (String string : columnNames) {
            try {
                Field declaredField = this.getClass().getDeclaredField(string);
                declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
            } catch (NoSuchFieldException e) {
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }

        }
        db.close();
        return this;
    }
}

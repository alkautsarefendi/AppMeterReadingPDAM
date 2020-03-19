package com.project.fortuna.meterreading.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.fortuna.meterreading.annotation.Column;
import com.project.fortuna.meterreading.annotation.PrimaryKey;
import com.project.fortuna.meterreading.annotation.Table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akautsar Efendi on 2/28/2018.
 */

@Table(name = "tabeltarif")
public class TbTarif extends BaseDAO {

    @PrimaryKey
    @Column
    public String kdTarif;

    @PrimaryKey
    @Column
    public String maxM3;

    @Column
    public String hargaM3;

    @Column
    public String golTarif;

    @Column
    public String biayaAdmin;

    @Column
    public String biayaDenda;


    public String getKdTarif() {
        return kdTarif;
    }


    public void setKdTarif(String kdTarif) {
        this.kdTarif = kdTarif;
    }


    public String getMaxM3() {
        return maxM3;
    }


    public void setMaxM3(String maxM3) {
        this.maxM3 = maxM3;
    }


    public String getHargaM3() {
        return hargaM3;
    }


    public void setHargaM3(String hargaM3) {
        this.hargaM3 = hargaM3;
    }


    public String getGolTarif() {
        return golTarif;
    }


    public void setGolTarif(String golTarif) {
        this.golTarif = golTarif;
    }


    public String getBiayaAdmin() {
        return biayaAdmin;
    }


    public void setBiayaAdmin(String biayaAdmin) {
        this.biayaAdmin = biayaAdmin;
    }


    public String getBiayaDenda() {
        return biayaDenda;
    }


    public void setBiayaDenda(String biayaDenda) {
        this.biayaDenda = biayaDenda;
    }


    public TbTarif(Context context) {
        super(context);
    }


    /* Generate Tagihan */
    public List<TbTarif> retrieveForTagihan(String kodeTarif) {
        SQLiteDatabase readableDatabase = new DBHelper(context).getReadableDatabase();
        List<TbTarif> w = new ArrayList<TbTarif>();
        String sql = "select "+ JoniUtils.getCloumns(this)+" from tabeltarif where kdTarif='"+kodeTarif+"' order by maxM3 asc ";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{}
        );
        if(cursor.moveToFirst()){
            do{
                TbTarif m = new TbTarif(context);
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
        readableDatabase.close();
        return w;
    }


    @Override
    public List<TbTarif> retrieveAll() {
        List<TbTarif> w = new ArrayList<TbTarif>();
        DBHelper db = new DBHelper(context);

        try {
            Cursor cursor = db.getAllData(this);
            if(cursor.moveToFirst()){
                do{
                    TbTarif m = new TbTarif(context);
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

            return w;
        } finally {
            db.close();
        }
    }

    @Override
    public TbTarif retrieveByID() {
        DBHelper db = new DBHelper(context);

        try {
            Cursor cursor = db.getDataByPrimaryKey(this);
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
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
                return this;
            }else{
                return null;
            }
        } finally {
            db.close();
        }
    }

}

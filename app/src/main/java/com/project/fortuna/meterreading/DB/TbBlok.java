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

@Table(name = "tabelblok")
public class TbBlok extends BaseDAO {

    public TbBlok(Context context) {
        super(context);
    }


    @PrimaryKey
    @Column
    public String kdWilayah;

    @Column
    public String kdJalan;

    @Column
    public String nmJalan;


    public String getKdWilayah() {
        return kdWilayah;
    }

    public void setKdWilayah(String kdWilayah) {
        this.kdWilayah = kdWilayah;
    }

    public String getKdJalan() {
        return kdJalan;
    }

    public void setKdJalan(String kdJalan) {
        this.kdJalan = kdJalan;
    }

    public String getNmJalan() {
        return nmJalan;
    }

    public void setNmJalan(String nmJalan) {
        this.nmJalan = nmJalan;
    }



    @Override
    public List<TbBlok> retrieveAll() {
        List<TbBlok> j = new ArrayList<TbBlok>();

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                TbBlok mjal = new TbBlok(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mjal, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                j.add(mjal);
            }while(cursor.moveToNext());
        }
        db.close();
        return j;
    }

    public List<TbBlok> retrieveByWilayah(String kdWilayah) {
        List<TbBlok> j = new ArrayList<TbBlok>();

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getReadDB().rawQuery("SELECT * FROM tabelblok where kdWilayah=?", new String[]{kdWilayah});
        if(cursor.moveToFirst()){
            do{
                TbBlok mjal = new TbBlok(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mjal, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                j.add(mjal);
            }while(cursor.moveToNext());
        }
        db.close();
        return j;
    }

    @Override
    public TbBlok retrieveByID() {
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


    /* Sort By Blok */
    public List<TbBlok> retrieveForSortByBlok(String user, String sTglMeter, boolean isBelum, boolean isSukses) {
        List<TbBlok> w = new ArrayList<TbBlok>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select "+ JoniUtils.getCloumns(this)+" from tabelblok where USER_ID=?";
        if(isBelum) sql=sql+" and ( STATUS_BACA < 0 ) ORDER BY nmJalan ASC";
        else if(isSukses) sql=sql+" and ( TGL_WAKTU_BACA LIKE '"+sTglMeter+"%' ) and ( STATUS_BACA between 0 and 17 ) ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                TbBlok m = new TbBlok(context);
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
}

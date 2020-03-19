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

@Table(name = "tabelbpma")
public class TbBPMA extends BaseDAO {

    public TbBPMA(Context context) {
        super(context);
    }


    @PrimaryKey
    @Column
    public String kdDiameter;

    @Column
    public String diameterFrom;

    @Column
    public String diameterTo;

    @Column
    public String biaya;




    public String getKdDiameter() {
        return kdDiameter;
    }



    public void setKdDiameter(String kdDiameter) {
        this.kdDiameter = kdDiameter;
    }



    public String getDiameterFrom() {
        return diameterFrom;
    }



    public void setDiameterFrom(String diameterFrom) {
        this.diameterFrom = diameterFrom;
    }



    public String getDiameterTo() {
        return diameterTo;
    }



    public void setDiameterTo(String diameterTo) {
        this.diameterTo = diameterTo;
    }



    public String getBiaya() {
        return biaya;
    }



    public void setBiaya(String biaya) {
        this.biaya = biaya;
    }




    @Override
    public List<TbBPMA> retrieveAll() {
        List<TbBPMA> j = new ArrayList<TbBPMA>();

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getAllData(this);
        if(cursor.moveToFirst()){
            do{
                TbBPMA mbpma = new TbBPMA(context);
                String[] columnNames = cursor.getColumnNames();
                for (String string : columnNames) {
                    try {
                        Field declaredField = this.getClass().getDeclaredField(string);
                        declaredField.set(mbpma, cursor.getString(cursor.getColumnIndex(string)));
                    } catch (NoSuchFieldException e) {
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
                j.add(mbpma);
            }while(cursor.moveToNext());
        }
        db.close();
        return j;
    }



    /* Generate BPMA */
    public List<TbBPMA> retrieveForBPMA(String diameter) {
        SQLiteDatabase readableDatabase = new DBHelper(context).getReadableDatabase();
        List<TbBPMA> w = new ArrayList<TbBPMA>();
        String sql = "select "+ JoniUtils.getCloumns(this)+" from tabelbpma where diameterFrom='"+diameter+"'";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{}
        );
        if(cursor.moveToFirst()){
            do{
                TbBPMA m = new TbBPMA(context);
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
    public TbBPMA retrieveByID() {
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

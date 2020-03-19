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
@Table(name="tabeluser")
public class TbUser extends BaseDAO{

    public TbUser(Context context) {
        super(context);
    }

    @Column
    @PrimaryKey
    public String userid;

    @Column
    public String username;

    @Column
    public String password;

    @Column
    public String nmPegawai;

    @Column
    public String nmJabatan;

    @Column
    public String nip;

    @Column
    public String kdWilayah;

    @Column
    public String nmWilayah;

    public String imei;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNmPegawai() {
        return nmPegawai;
    }

    public void setNmPegawai(String nmPegawai) {
        this.nmPegawai = nmPegawai;
    }

    public String getNmJabatan() {
        return nmJabatan;
    }

    public void setNmJabatan(String nmJabatan) {
        this.nmJabatan = nmJabatan;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

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

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }


    public TbUser getUser(String username){
        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getReadableDatabase().query(
                JoniUtils.getTableName(this), JoniUtils.getCloumns(this).split(","),
                "username=?", new String[]{username}, null, null, null);
        if(cursor.getCount() > 0){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                }

            }
            return this;
        }

        return null;
    }

    @Override
    public List<?> retrieveAll() {

        return null;
    }

    @Override
    public TbUser retrieveByID() {
        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
        if(cursor.getCount() > 0){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                }

            }
            return this;
        }
        db.close();
        return null;
    }



    /* Retrieve Review Data */
    public List<TbUser> retrieveForUser(String user) {
        SQLiteDatabase readableDatabase = new DBHelper(context).getReadableDatabase();
        List<TbUser> w = new ArrayList<TbUser>();
        String sql = "select "+JoniUtils.getCloumns(this)+" from tabeluser where userid=? ";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if(cursor.moveToFirst()){
            do{
                TbUser m = new TbUser(context);
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



    /* Retrieve User */
    public TbUser retrieveForUserHome(String user) {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from tabeluser where userid=?",
                new String[]{user}
        );
        if(cursor.getCount() > 0 ){
            if(cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    System.out.println("test "+string+"="+cursor.getString(cursor.getColumnIndex(string)));
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

            }
            return this;
        }
        readableDatabase.close();
        return null;
    }
}

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
@Table(name = "tabelkonfigurasiws")
public class TbKonfigurasi extends BaseDAO {
    public TbKonfigurasi(Context context) {
        super(context);
    }

    @Column
    public String kdKonfigurasi;

    @Column
    public String userFTP;

    @Column
    public String passwordFTP;

    @Column
    public String urlFTP;

    @Column
    @PrimaryKey
    public String userURL;

    @Column
    public String wilayahURL;

    @Column
    public String wilayahBacaURL;

    @Column
    public String tarifURL;

    @Column
    public String bpmaURL;

    @Column
    public String jalanURL;

    @Column
    public String downloadURL;

    @Column
    public String uploadURL;

    @Column
    public String realtimeURL;

    @Column
    public String pingURL;


    public String getUserFTP() {
        return userFTP;
    }

    public void setUserFTP(String userFTP) {
        this.userFTP = userFTP;
    }


    public String getPasswordFTP() {
        return passwordFTP;
    }

    public void setPasswordFTP(String passwordFTP) {
        this.passwordFTP = passwordFTP;
    }

    public String getUrlFTP() {
        return urlFTP;
    }

    public void setUrlFTP(String urlFTP) {
        this.urlFTP = urlFTP;
    }


    public String getUserURL() {
        return userURL;
    }

    public void setUserURL(String userURL) {
        this.userURL = userURL;
    }


    public String getWilayahURL() {
        return wilayahURL;
    }

    public void setWilayahURL(String wilayahURL) {
        this.wilayahURL = wilayahURL;
    }

    public String getWilayahBacaURL() {
        return wilayahBacaURL;
    }

    public void setWilayahBacaURL(String wilayahBacaURL) {
        this.wilayahBacaURL = wilayahBacaURL;
    }

    public String getTarifURL() {
        return tarifURL;
    }

    public void setTarifURL(String tarifURL) {
        this.tarifURL = tarifURL;
    }


    public String getBpmaURL() {
        return bpmaURL;
    }

    public void setBpmaURL(String bpmaURL) {
        this.bpmaURL = bpmaURL;
    }

    public String getJalanURL() {
        return jalanURL;
    }

    public void setJalanURL(String jalanURL) {
        this.jalanURL = jalanURL;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getUploadURL() {
        return uploadURL;
    }

    public void setUploadURL(String uploadURL) {
        this.uploadURL = uploadURL;
    }


    public String getRealtimeURL() {
        return realtimeURL;
    }

    public void setRealtimeURL(String realtimeURL) {
        this.realtimeURL = realtimeURL;
    }


    public String getPingURL() {
        return pingURL;
    }

    public void setPingURL(String pingURL) {
        this.pingURL = pingURL;
    }

    @Override
    public List<TbKonfigurasi> retrieveAll() {
        List<TbKonfigurasi> k = new ArrayList<TbKonfigurasi>();
        DBHelper db = new DBHelper(context);

        try {
            Cursor cursor = db.getAllData(this);
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    TbKonfigurasi mk = new TbKonfigurasi(context);
                    String[] columnNames = cursor.getColumnNames();
                    for (String string : columnNames) {
                        try {
                            Field declaredField = this.getClass().getDeclaredField(string);
                            declaredField.set(mk, cursor.getString(cursor.getColumnIndex(string)));
                        } catch (NoSuchFieldException e) {
                        } catch (IllegalArgumentException e) {
                        } catch (IllegalAccessException e) {
                        }
                    }
                    k.add(mk);
                } while (cursor.moveToNext());
            }
            return k;
        } finally {
            db.close();
        }
    }

    @Override
    public TbKonfigurasi retrieveByID() {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readabledatabase = db.getReadableDatabase();
        String sql = "SELECT downloadURL FROM tabelkonfigurasiws WHERE kdKonfigurasi=1";
        try {
            Cursor cursor = readabledatabase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor != null) cursor.moveToFirst();
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
            }
            return null;
        } finally {
            db.close();
        }

    }

    public TbKonfigurasi retrieveUploadByID() {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readabledatabase = db.getReadableDatabase();
        String sql = "SELECT uploadURL FROM tabelkonfigurasiws WHERE kdKonfigurasi=1";
        try {
            Cursor cursor = readabledatabase.rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor != null) cursor.moveToFirst();
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
            }
            return null;
        } finally {
            db.close();
        }

    }
}

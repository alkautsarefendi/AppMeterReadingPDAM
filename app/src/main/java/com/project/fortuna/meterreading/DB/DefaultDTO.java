package com.project.fortuna.meterreading.DB;

import android.content.Context;

import java.util.List;

/**
 * Created by Akautsar Efendi on 3/1/2018.
 */

public class DefaultDTO extends BaseDAO {

    public String USERID;
    public String KD_WILAYAH;
    public String PERIODE;
    public String TGL_MULAI;
    public String TGL_SAMPAI;
    public String WIL_BACA;
    public String STATUS_;

    public DefaultDTO(Context context) {
        super(context);
    }

    @Override
    public List<?> retrieveAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object retrieveByID() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getKD_WILAYAH() {
        return KD_WILAYAH;
    }

    public void setKD_WILAYAH(String KD_WILAYAH) {
        this.KD_WILAYAH = KD_WILAYAH;
    }

    public String getPERIODE() {
        return PERIODE;
    }

    public void setPERIODE(String PERIODE) {
        this.PERIODE = PERIODE;
    }

    public String getTGL_MULAI() {
        return TGL_MULAI;
    }

    public void setTGL_MULAI(String TGL_MULAI) {
        this.TGL_MULAI = TGL_MULAI;
    }

    public String getTGL_SAMPAI() {
        return TGL_SAMPAI;
    }

    public void setTGL_SAMPAI(String TGL_SAMPAI) {
        this.TGL_SAMPAI = TGL_SAMPAI;
    }

    public String getWIL_BACA() {
        return WIL_BACA;
    }

    public void setWIL_BACA(String WIL_BACA) {
        this.WIL_BACA = WIL_BACA;
    }

    public String getSTATUS_() {
        return STATUS_;
    }

    public void setSTATUS_(String STATUS_) {
        this.STATUS_ = STATUS_;
    }
}

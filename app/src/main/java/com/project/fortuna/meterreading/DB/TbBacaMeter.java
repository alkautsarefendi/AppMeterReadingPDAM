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

@Table(name = "tbbacameter")
public class TbBacaMeter extends BaseDAO {

    public TbBacaMeter(Context context) {
        super(context);
    }

    @Column
    @PrimaryKey
    public String KD_PELANGGAN;

    @Column
    public String PELANGGAN;

    @Column
    public String ALMT_PASANG;

    @Column
    public String KD_TARIF;

    @Column
    public String GOL_TARIF;

    @Column
    public String DIAMETER;

    @Column
    public String PERIODE;

    @Column
    public String NO_METER;

    @Column
    public String M3_AWAL;

    @Column
    public String M3_AKHIR;

    @Column
    public String STAND_HITUNG;

    @Column
    public String STATUS_BACA;

    @Column
    public String TAGIHAN_BULAN_INI;

    @Column
    public String PREDIKSI_TAGIHAN;

    @Column
    public String KUBIKASI;

    @Column
    public String FOTO;

    @Column
    public String LATITUDE;

    @Column
    public String LONGITUDE;

    @Column
    public String GPS_LAT;

    @Column
    public String GPS_LONG;

    @Column
    public String TGL_WAKTU_BACA;

    @Column
    public String NM_JALAN;

    @Column
    public String KD_WILAYAH;

    @Column
    public String USER_ID;

    @Column
    public String S_KIRIM;

    @Column
    public String M_KIRIM;

    public String getSTAND_HITUNG() {
        return STAND_HITUNG;
    }

    public void setSTAND_HITUNG(String STAND_HITUNG) {
        this.STAND_HITUNG = STAND_HITUNG;
    }

    public String getKD_PELANGGAN() {
        return KD_PELANGGAN;
    }


    public void setKD_PELANGGAN(String kD_PELANGGAN) {
        KD_PELANGGAN = kD_PELANGGAN;
    }


    public String getPELANGGAN() {
        return PELANGGAN;
    }


    public void setPELANGGAN(String pELANGGAN) {
        PELANGGAN = pELANGGAN;
    }


    public String getALMT_PASANG() {
        return ALMT_PASANG;
    }


    public void setALMT_PASANG(String aLMT_PASANG) {
        ALMT_PASANG = aLMT_PASANG;
    }


    public String getKD_TARIF() {
        return KD_TARIF;
    }


    public void setKD_TARIF(String kD_TARIF) {
        KD_TARIF = kD_TARIF;
    }


    public String getGOL_TARIF() {
        return GOL_TARIF;
    }


    public void setGOL_TARIF(String gOL_TARIF) {
        GOL_TARIF = gOL_TARIF;
    }


    public String getDIAMETER() {
        return DIAMETER;
    }


    public void setDIAMETER(String dIAMETER) {
        DIAMETER = dIAMETER;
    }


    public String getPERIODE() {
        return PERIODE;
    }


    public void setPERIODE(String pERIODE) {
        PERIODE = pERIODE;
    }


    public String getNO_METER() {
        return NO_METER;
    }


    public void setNO_METER(String nO_METER) {
        NO_METER = nO_METER;
    }


    public String getM3_AWAL() {
        return M3_AWAL;
    }


    public void setM3_AWAL(String m3_AWAL) {
        M3_AWAL = m3_AWAL;
    }


    public String getM3_AKHIR() {
        return M3_AKHIR;
    }


    public void setM3_AKHIR(String m3_AKHIR) {
        M3_AKHIR = m3_AKHIR;
    }


    public String getSTATUS_BACA() {
        return STATUS_BACA;
    }


    public void setSTATUS_BACA(String sTATUS_BACA) {
        STATUS_BACA = sTATUS_BACA;
    }


    public String getTAGIHAN_BULAN_INI() {
        return TAGIHAN_BULAN_INI;
    }


    public void setTAGIHAN_BULAN_INI(String tAGIHAN_BULAN_INI) {
        TAGIHAN_BULAN_INI = tAGIHAN_BULAN_INI;
    }


    public String getPREDIKSI_TAGIHAN() {
        return PREDIKSI_TAGIHAN;
    }


    public void setPREDIKSI_TAGIHAN(String pREDIKSI_TAGIHAN) {
        PREDIKSI_TAGIHAN = pREDIKSI_TAGIHAN;
    }


    public String getKUBIKASI() {
        return KUBIKASI;
    }


    public void setKUBIKASI(String kUBIKASI) {
        KUBIKASI = kUBIKASI;
    }


    public String getFOTO() {
        return FOTO;
    }


    public void setFOTO(String fOTO) {
        FOTO = fOTO;
    }


    public String getLATITUDE() {
        return LATITUDE;
    }


    public void setLATITUDE(String lATITUDE) {
        LATITUDE = lATITUDE;
    }


    public String getLONGITUDE() {
        return LONGITUDE;
    }


    public void setLONGITUDE(String lONGITUDE) {
        LONGITUDE = lONGITUDE;
    }


    public String getGPS_LAT() {
        return GPS_LAT;
    }

    public void setGPS_LAT(String GPS_LAT) {
        this.GPS_LAT = GPS_LAT;
    }

    public String getGPS_LONG() {
        return GPS_LONG;
    }

    public void setGPS_LONG(String GPS_LONG) {
        this.GPS_LONG = GPS_LONG;
    }

    public String getS_KIRIM() {
        return S_KIRIM;
    }

    public void setS_KIRIM(String s_KIRIM) {
        S_KIRIM = s_KIRIM;
    }

    public String getM_KIRIM() {
        return M_KIRIM;
    }

    public void setM_KIRIM(String m_KIRIM) {
        M_KIRIM = m_KIRIM;
    }

    public String getTGL_WAKTU_BACA() {
        return TGL_WAKTU_BACA;
    }


    public void setTGL_WAKTU_BACA(String tGL_WAKTU_BACA) {
        TGL_WAKTU_BACA = tGL_WAKTU_BACA;
    }


    public String getNM_JALAN() {
        return NM_JALAN;
    }

    public void setNM_JALAN(String NM_JALAN) {
        this.NM_JALAN = NM_JALAN;
    }


    public String getKD_WILAYAH() {
        return KD_WILAYAH;
    }


    public void setKD_WILAYAH(String kD_WILAYAH) {
        KD_WILAYAH = kD_WILAYAH;
    }


    public String getUSER_ID() {
        return USER_ID;
    }


    public void setUSER_ID(String uSER_ID) {
        USER_ID = uSER_ID;
    }


    /* Retrieve All */
    @Override
    public List<TbBacaMeter> retrieveAll() {
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();

        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getAllData(this);
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }


    /* Retrieve by ID */
    @Override
    public TbBacaMeter retrieveByID() {
        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getDataByPrimaryKey(this);
        if (cursor.getCount() > 0) {
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
        db.close();
        return null;
    }


    /* Retrieve Baca Meter */
    public TbBacaMeter retrieveForMeter(String user, String kd_pel) {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select * from tbbacameter where USER_ID=? and KD_PELANGGAN=?",
                new String[]{user, kd_pel}
        );
        if (cursor.getCount() > 0) {
            if (cursor != null) cursor.moveToFirst();
            String[] columnNames = cursor.getColumnNames();
            for (String string : columnNames) {
                try {
                    System.out.println("test " + string + "=" + cursor.getString(cursor.getColumnIndex(string)));
                    Field declaredField = this.getClass().getDeclaredField(string);
                    declaredField.set(this, cursor.getString(cursor.getColumnIndex(string)));
                } catch (NoSuchFieldException e) {
                } catch (IllegalArgumentException e) {
                } catch (IllegalAccessException e) {
                }

            }
            return this;
        }
        db.close();
        return null;
    }


    /* Retrieve Kode Blok Belum */
    public String retrieveForKodeBlokBelum(String nama_blok) {

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select count(*) from tbbacameter where NM_JALAN LIKE '" + nama_blok + "%' and ( STATUS_BACA < 0 )", null
        );

        cursor.moveToFirst();
        int count= cursor.getInt(0);

        db.close();

        return String.valueOf(count);
    }


    /* Retrieve Kode Blok Sudah */
    public String retrieveForKodeBlokSudah(String nama_blok, String sTglMeter) {

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("select count(*) from tbbacameter where NM_JALAN LIKE '" + nama_blok + "%' and ( STATUS_BACA between 0 and 17 )" +
                "and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' )", null
        );

        cursor.moveToFirst();
        int count= cursor.getInt(0);

        db.close();

        return String.valueOf(count);
    }



    /* Review Blok */
    public List<TbBacaMeter> retrieveMeterBlok(String user, String sTglMeter, boolean isBelum, boolean isSukses) {
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select distinct NM_JALAN from tbbacameter where USER_ID=?";
        if (isBelum) sql = sql + " and ( STATUS_BACA < 0 ) ORDER BY NM_JALAN ASC";
        else if (isSukses)
            sql = sql + " and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA between 0 and 17) ORDER BY NM_JALAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }


    /* Review */
    public List<TbBacaMeter> retrieveForReview(String user, String sTglMeter, boolean isBelum, boolean isSukses, String sNamaJalan) {
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select " + JoniUtils.getCloumns(this) + " from tbbacameter where USER_ID=?";
        if (isBelum) sql = sql + " and ( STATUS_BACA < 0 ) and NM_JALAN LIKE '" + sNamaJalan + "%' ORDER BY KD_PELANGGAN ASC";
        else if (isSukses)
            sql = sql + " and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA between 0 and 17 ) and NM_JALAN LIKE '" + sNamaJalan + "%' ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }

    /* Review belum*/
    public List<TbBacaMeter> retrieveDataBelum(String user, String sTglMeter, boolean isBelum, boolean isSukses) {
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select " + JoniUtils.getCloumns(this) + " from tbbacameter where USER_ID=?";
        if (isBelum) sql = sql + " and ( STATUS_BACA < 0 ) ORDER BY KD_PELANGGAN ASC";
        else if (isSukses)
            sql = sql + " and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA between 0 and 17 ) ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }

    /* Review belum*/
    public List<TbBacaMeter> retrieveDataSudah(String user, String sTglMeter, boolean isSukses) {
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();

        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        String sql = "select " + JoniUtils.getCloumns(this) + " from tbbacameter where USER_ID=?";
        if (false) sql = sql + " and ( STATUS_BACA < 0 ) ORDER BY KD_PELANGGAN ASC";
        else if (isSukses)
            sql = sql + " and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA > 0 ) ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }


    /* Export to Server */
    public List<TbBacaMeter> retrieveForExportServer(String user, String sTglMeter) {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();
        String sql = "select " + JoniUtils.getCloumns(this) + " from tbbacameter where USER_ID=? and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA between 0 and 17 ) and S_KIRIM = 0 ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }


    /* Retrieve Export Local */
    public List<TbBacaMeter> retrieveForExport(String user, String sTglMeter) {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();
        String sql = "select " + JoniUtils.getCloumns(this) + " from tbbacameter where USER_ID=? and ( TGL_WAKTU_BACA LIKE '" + sTglMeter + "%' ) and ( STATUS_BACA between 0 and 17 ) ORDER BY KD_PELANGGAN ASC";

        Cursor cursor = readableDatabase.rawQuery(sql,
                new String[]{user}
        );
        if (cursor.moveToFirst()) {
            do {
                TbBacaMeter m = new TbBacaMeter(context);
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
            } while (cursor.moveToNext());
        }
        db.close();
        return w;
    }


    /* Hapus Data Belum di Baca */
    public List<TbBacaMeter> doClearDatabaseBacaMeterBelumdiBaca() {
        DBHelper db = new DBHelper(context);
        SQLiteDatabase readableDatabase = db.getWritableDatabase();
        List<TbBacaMeter> w = new ArrayList<TbBacaMeter>();
        String sql = "delete from tbbacameter where STATUS_BACA < 0 ";

		/*Cursor cursor = */
        readableDatabase.execSQL(sql);/*(sql,
					new String[]{}
				);*/
		/*if(cursor.moveToFirst()){
			do{
				TSurvey m = new TSurvey(context);
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
		}*/
        db.close();
        return w;
    }
}

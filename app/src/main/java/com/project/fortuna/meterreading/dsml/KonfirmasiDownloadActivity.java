package com.project.fortuna.meterreading.dsml;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.fortuna.meterreading.config.KonfigurasiActivity;
import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.util.ConnectivityWS;
import com.project.fortuna.meterreading.DB.DefaultDTO;
import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.DB.TbKonfigurasi;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KonfirmasiDownloadActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnImportFile, btnImportCancel;

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/CSV/";
    private ListView listView;
    private ListAdapter adapter;
    private List<TbBacaMeter> listMeter = new ArrayList<TbBacaMeter>();
    private TextView txtTotal;
    private ProgressDialog pDialog;
    private static final String TAG = KonfirmasiDownloadActivity.class.getSimpleName();
    RelativeLayout lay;
    /*private TextView txtTotal;*/

    Handler handler = new Handler();
    String jmlData;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_download);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Konfirmasi Download DSML");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initComponent();
        isInternetOn();
        setDataList();

    }

    private void initComponent() {

        listView = findViewById(R.id.id_import_list_view_server);

        txtTotal = findViewById(R.id.txtViewTotalData);

        lay = findViewById(R.id.lay_fab);

        /* Button Import */
        btnImportFile = findViewById(R.id.btnImportFile);
        btnImportFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new importData().execute();

            }
        });

        /* Button Cancel */
        btnImportCancel = findViewById(R.id.btnCancelDownload);
        btnImportCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*txtTotal = (TextView) findViewById(R.id.txtViewTotal);*/

        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {

                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH
                        + " on sdcard failed");
                return;
            } else {

                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");

            }
        }

    }

    private void setDataList() {

        setListData task = new setListData(KonfirmasiDownloadActivity.this);
        task.execute();

    }



    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            Log.e(TAG, "Connected");
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(KonfirmasiDownloadActivity.this);

            alertDialog.setMessage("No Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("Retry",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            }).setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            KonfirmasiDownloadActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(new Intent(KonfirmasiDownloadActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class importData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KonfirmasiDownloadActivity.this);
            pDialog.setMax(listView.getAdapter().getCount());
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            final int totalProgressTime = listView.getAdapter().getCount();
            final Thread t = new Thread() {
                @Override
                public void run() {
                    int jumpTime = 0;

                    while (jumpTime < totalProgressTime) {
                        try {
                            sleep(200);
                            jumpTime += 5;
                            pDialog.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            };
            t.start();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int count;

            if (!listMeter.isEmpty()) {
                try {
                    for (TbBacaMeter tm : listMeter) {
                        tm.KD_PELANGGAN = "'" + tm.KD_PELANGGAN;
                        if (tm.retrieveByID() != null) {
                            tm.update();
                            /*flag=true;*/

                        } else {
                            tm.save();

                        }
                    }
                } catch (Exception e) {
                    Snackbar.make(lay, "Data Gagal di Import", Snackbar.LENGTH_LONG).show();
                    /*pDialog.dismiss();*/
                    /*Util.showmsg(getApplicationContext(), "Peringatan :", "Data Gagal di Import");*/
                }
                /*btnImportFile.setEnabled(false);*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            KonfirmasiDownloadActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean flag = false;
                    if (!flag) {
                        /*Snackbar.make(lay, listView.getAdapter().getCount() +" Data Berhasil di Import", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                        finish();*/

                        AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiDownloadActivity.this);
                        builder.setTitle("Import From Network")
                                .setMessage("Import " + listView.getAdapter().getCount() + " Data Berhasil")
                                .setCancelable(false)
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiDownloadActivity.this);
                        builder.setTitle("Import From Network")
                                .setMessage("Data Berhasil diperbaharui")
                                .setCancelable(false)
                                .setNegativeButton("Close", new
                                        DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.show();
                    }
                }
            });

        }
    }

    @SuppressLint("StaticFieldLeak")
    private class setListData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        public setListData(KonfirmasiDownloadActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Memuat..."); // Setting Message
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            dialog.show(); // Display Progress Dialog
            dialog.setCancelable(false);

        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected Void doInBackground(Void... strings) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TbKonfigurasi km = new TbKonfigurasi(getApplicationContext());
                    km.kdKonfigurasi = "1";
                    km = km.retrieveByID();
                    System.out.println("url Download = " + km.retrieveByID());
                    if (km != null && !km.getDownloadURL().trim().equals("")) {

                        adapter = new ImportDSMLAdapter(KonfirmasiDownloadActivity.this, listMeter);
                        listView.setAdapter(adapter);



                        String jumlahData = String.valueOf(listMeter.size());
                /*System.out.println("Jumlah data = " + jumlahData);*/

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0); // 0 - for private mode
                        String awal = pref.getString("tglAwal", "");
                        String akhir = pref.getString("tglAkhir", "");
                        String periode = pref.getString("periode", "");
                        String status_ = pref.getString("status_", "");
                        String kd_wilayah = pref.getString("kd_wilayah", "");
                /*String status_kudo = String.valueOf("status_");*/

                        DefaultDTO dto = new DefaultDTO(getApplicationContext());

                        dto.USERID = pref.getString("userid", "");
                        dto.KD_WILAYAH = (kd_wilayah);
                        dto.PERIODE = (periode);
                        dto.TGL_MULAI = (awal);
                        dto.TGL_SAMPAI = (akhir);
                        dto.STATUS_ = (status_);

                        System.out.println("userID = " + pref.getString("userid", ""));
                        System.out.println("periode = " + periode);
                        System.out.println("tglAwal = " + awal);
                        System.out.println("tglAkhir = " + akhir);
                        System.out.println("status data = " + status_);
                        System.out.println("kd_wilayah = " + kd_wilayah);

                        final JSONObject response = ConnectivityWS.postToServer(dto, km.getDownloadURL());
                        System.out.println(response);

                        try {
                            if (response != null && response.has("CODE") && response.get("CODE").equals("00")) {
                                JSONArray jsonArray = response.getJSONArray("DATA");
                                System.out.println("iko jumlah data e  yuang = " + String.valueOf(jsonArray.length()));

                                jmlData = String.valueOf(jsonArray.length());
                                if (jsonArray.length() == 0) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Util.showmsg(KonfirmasiDownloadActivity.this, "Peringatan :", "Data Kosong");
                                        }
                                    });
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        TbBacaMeter tMeter = new TbBacaMeter(getApplicationContext());
                                        tMeter.ALMT_PASANG = jsonArray.getJSONObject(i).getString("ALMT_PASANG");
                                        tMeter.M3_AWAL = jsonArray.getJSONObject(i).getString("M3_AWAL");
                                        tMeter.STAND_HITUNG = jsonArray.getJSONObject(i).getString("STAND_HITUNG");
                                        tMeter.USER_ID = jsonArray.getJSONObject(i).getString("USERID");
                                        tMeter.KD_PELANGGAN = jsonArray.getJSONObject(i).getString("KD_PELANGGAN");
                                        tMeter.PELANGGAN = jsonArray.getJSONObject(i).getString("PEMILIK");
                                        tMeter.PERIODE = jsonArray.getJSONObject(i).getString("PERIODE");
                                        tMeter.KD_TARIF = jsonArray.getJSONObject(i).getString("KD_TARIF");
                                        tMeter.GOL_TARIF = jsonArray.getJSONObject(i).getString("GOL_TARIF");
                                        tMeter.LATITUDE = jsonArray.getJSONObject(i).getString("LATITUDE");
                                        tMeter.LONGITUDE = jsonArray.getJSONObject(i).getString("LONGITUDE");
                                        tMeter.NO_METER = jsonArray.getJSONObject(i).getString("KD_STANDMETER");
                                        tMeter.DIAMETER = jsonArray.getJSONObject(i).getString("DIAMETER_PIPA");
                                        tMeter.NM_JALAN = jsonArray.getJSONObject(i).getString("NM_JALAN");
                                        tMeter.KD_WILAYAH = jsonArray.getJSONObject(i).getString("KD_WILAYAH");
                                        tMeter.STATUS_BACA = "-1";
                                        tMeter.S_KIRIM = "0";
                                        listMeter.add(tMeter);

                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            txtTotal.setText("Total Data Yang di Import " + jmlData);
                                        }
                                    });
                                }
                            } else {
                                if (response == null) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Util.showmsg(KonfirmasiDownloadActivity.this, "Peringatan :", "Permintaan gagal");
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            try {
                                                Util.showmsg(KonfirmasiDownloadActivity.this, "Peringatan :", "Permintaan gagal, response is " + response.getString("CODE"));
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.dismiss();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Util.showmsg(KonfirmasiDownloadActivity.this, "Peringatan :", "Silahkan konfigurasi url download terlebih dahulu");
                            }
                        });
                    }
                }
            });

            return null;
        }
    }
}

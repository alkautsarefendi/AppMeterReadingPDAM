package com.project.fortuna.meterreading.export;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.DB.TbKonfigurasi;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.config.KonfigurasiActivity;
import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.dsml.KonfirmasiDownloadActivity;
import com.project.fortuna.meterreading.util.ConnectivityWS;
import com.project.fortuna.meterreading.util.Util;

import org.json.JSONObject;

import java.util.List;

public class KonfirmasiUploadActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private List<TbBacaMeter> listMeter;
    private TbBacaMeter selectedMeter;
    private Button btnKirimSemua;
    private ListView listExport;
    private static final String TAG = KonfirmasiUploadActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private int flag;
    private TextView txtTotalData;

    protected String userid;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_upload);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export File Network");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        userid = pref.getString("userid", "");

        initComponent();
        isInternetOn();
    }

    private void initComponent() {

        Intent param = getIntent();
        String sTglMeter = String.valueOf(param.getExtras().getString("sTglMeter"));
        System.out.println("sTglMeter = " + sTglMeter);

        listExport = (ListView) findViewById(R.id.id_export_list_view);
        listMeter = new TbBacaMeter(KonfirmasiUploadActivity.this).retrieveForExportServer(userid, sTglMeter);
        String totData = String.valueOf(listMeter.size());
        System.out.println("totalDataUpload = " + totData);

        txtTotalData = (TextView) findViewById(R.id.txtViewTotalDataUpload);
        txtTotalData.setText("Total Data = " + totData);

        if (listMeter.size() > 0) {
            ExportFileMeterAdapter adapter = new ExportFileMeterAdapter(this, listMeter);
            listExport.setAdapter(adapter);
        } else {
            System.out.println("banyak data: " + listMeter.size());
            AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiUploadActivity.this);
            builder.setTitle("Peringatan :")
                    .setMessage("Status " + getString(R.string.msg_no_data))
                    .setCancelable(false)
                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        }

        /* Button Kirim */
        btnKirimSemua = (Button) findViewById(R.id.btnSendChecked);
        btnKirimSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProses run = new uploadProses();
                run.execute();
                /*try {
                    String allnoreg = "";
                    for (int i = 0; i < listExport.getCount(); i++) {
                        View vg = listExport.getChildAt(i);

                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiUploadActivity.this);
                    builder.setTitle("Peringatan : ");
                    builder.setMessage("Apakah Anda Ingin Mengirim Data Dengan Kode Pel : " + allnoreg.replace("'", "") + " ke Server ?").setCancelable(false);
                    builder.setPositiveButton("Kirim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            new uploadProses().execute();
                        }
                    });

                    builder.setNegativeButton("Batal", null).show();
                } catch (
                        Exception e)

                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiUploadActivity.this);
                    builder.setTitle("Peringatan : ");
                    builder.setMessage(e.getMessage()).setCancelable(false);
                    builder.setNegativeButton("Batal", null).show();
                }*/
            }
        });

    }

    public class uploadProses extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(KonfirmasiUploadActivity.this);
            progressDialog.setMax(listMeter.size());
            progressDialog.setMessage("Mengupload...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (progressDialog.getProgress() <= progressDialog
                                .getMax()) {
                            Thread.sleep(200);
                            handle.sendMessage(handle.obtainMessage());
                            if (progressDialog.getProgress() == progressDialog
                                    .getMax()) {

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDialog.incrementProgressBy(1);
            }
        };


        @Override
        protected Void doInBackground(Void... voids) {
            TbKonfigurasi km = new TbKonfigurasi(getApplicationContext());
            km.kdKonfigurasi = "1";
            km = km.retrieveUploadByID();
            System.out.println("uploadURL = " + km.uploadURL);
            flag = 0;
            if (km != null && km.uploadURL != null && !km.uploadURL.trim().equals("")) {
                for (int i = 0; i < listExport.getCount(); i++) {
                    View vg = listExport.getChildAt(i);

                    selectedMeter = listMeter.get(i);
                    selectedMeter.KD_PELANGGAN = selectedMeter.KD_PELANGGAN.replace("'", "");
                    /*progressDialog.dismiss();*/
                    try {
                        JSONObject response = ConnectivityWS.postBacaMeter(selectedMeter, km.uploadURL);
                        if (response.getString("CODE").equals("00")) {
                            System.out.println("respon upload = "+response);
                            selectedMeter.S_KIRIM = "1";
                            selectedMeter.KD_PELANGGAN = "'" + selectedMeter.KD_PELANGGAN;
                            selectedMeter.update();
                            flag++;
                        } else throw new Exception("Gagal terkirim");
                    } catch (Exception e) {
                        KonfirmasiUploadActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiUploadActivity.this);
                                builder.setTitle("Export To Network")
                                        .setMessage("Gagal terkirim")
                                        .setCancelable(false)
                                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.setCancelable(false);
                                alert.show();
                            }
                        });

                    }

                }
            } else {
                KonfirmasiUploadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showmsg(KonfirmasiUploadActivity.this, "Export ke server", "Konfigurasi url belum tersedia.");
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            KonfirmasiUploadActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (flag > 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(KonfirmasiUploadActivity.this);
                        builder.setTitle("Export To Network")
                                .setMessage(flag + " Data Sukses dikirim")
                                .setCancelable(false)
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        startActivity(getIntent());
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

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            Log.e(TAG, "Connected");
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(KonfirmasiUploadActivity.this);

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
                            KonfirmasiUploadActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(new Intent(KonfirmasiUploadActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }
}

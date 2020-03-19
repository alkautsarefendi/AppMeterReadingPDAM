package com.project.fortuna.meterreading.config;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.fortuna.meterreading.util.ConnectivityWS;
import com.project.fortuna.meterreading.DB.JoniUtils;
import com.project.fortuna.meterreading.DB.TbBPMA;
import com.project.fortuna.meterreading.DB.TbBlok;
import com.project.fortuna.meterreading.DB.TbKonfigurasi;
import com.project.fortuna.meterreading.DB.TbTarif;
import com.project.fortuna.meterreading.DB.TbUser;
import com.project.fortuna.meterreading.DB.TbWilayah;
import com.project.fortuna.meterreading.DB.TbWilayahBaca;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class KonfigurasiActivity extends AppCompatActivity {

    private Toolbar toolbar;
    protected EditText txtUser;
    protected Button btnSyncUser, btnSyncWilayah, btnSyncWilayahBaca, btnSyncBlok, btnSimpanConfig, btnSyncTarif, btnSyncBPMA, btnCancelConfig;
    String urlUser, urlWilayah, urlWilayahBaca, urlBlock, urlTarif, urlBPMA, urlDownloadNew, urlUpload;
    private static final String TAG = KonfigurasiActivity.class.getSimpleName();
    Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_konfigurasi);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Konfigurasi");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        initComponent();
        setUrl();
        initDataURL();
        isInternetOn();


    }

    private void setUrl() {

        urlUser = getString(R.string.ip_config);
        urlWilayah = getString(R.string.ip_config);
        urlWilayahBaca = getString(R.string.ip_config);
        urlBlock = getString(R.string.ip_config);
        urlBPMA = getString(R.string.ip_config);
        urlTarif = getString(R.string.ip_config);
        urlDownloadNew = getString(R.string.ip_config);
        urlUpload = getString(R.string.ip_config);
    }

    private void initDataURL() {
        TbKonfigurasi mk = new TbKonfigurasi(getApplicationContext());
        mk.retrieveByID();

        if (mk.userURL != null) {
            txtUser.setText(mk.userURL);
            txtUser.setText(mk.wilayahURL);
            txtUser.setText(mk.wilayahBacaURL);
            txtUser.setText(mk.jalanURL);
            txtUser.setText(mk.bpmaURL);
            txtUser.setText(mk.tarifURL);
            txtUser.setText(mk.downloadURL);
            txtUser.setText(mk.uploadURL);
        } else {
            txtUser.setText(urlUser);
            txtUser.setText(urlWilayah);
            txtUser.setText(urlWilayahBaca);
            txtUser.setText(urlBlock);
            txtUser.setText(urlTarif);
            txtUser.setText(urlBPMA);
            txtUser.setText(urlDownloadNew);
            txtUser.setText(urlUpload);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initComponent() {

        /*//progress button
        final String text = "Sinkronisasi. . .";
        lt = new LoadToast(this)
                .setProgressColor(Color.RED)
                .setText(text)
                .setTranslationY(100)
                .setBorderColor(Color.LTGRAY);
        //lt.success();
        final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View v = new View(this);
        v.setBackgroundColor(Color.RED);*/

        txtUser = (EditText) findViewById(R.id.txtUserURL);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutKonfig);

        /* Button Sync User */
        btnSyncUser = (Button) findViewById(R.id.btnSyncUser);
        btnSyncUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncUser syncUser = new SyncUser();
                    syncUser.execute();
                }

            }
        });

        /* Button Sync Wilayah */
        btnSyncWilayah = (Button) findViewById(R.id.btnSyncWilayah);
        btnSyncWilayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncWilayah syncWilayah = new SyncWilayah();
                    syncWilayah.execute();
                }
            }
        });

        /* Button Sync Wilayah Baca */
        btnSyncWilayahBaca = (Button) findViewById(R.id.btnSyncWilayahBaca);
        btnSyncWilayahBaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncWilayahBaca syncWilayahBaca = new SyncWilayahBaca();
                    syncWilayahBaca.execute();
                }
            }
        });

        /* Button Sync Blok */
        btnSyncBlok = (Button) findViewById(R.id.btnSyncBlok);
        btnSyncBlok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncBlok syncBlok = new SyncBlok();
                    syncBlok.execute();
                }
            }
        });


        /* Button Simpan */
        btnSimpanConfig = (Button) findViewById(R.id.btnSimpanConfig);
        btnSimpanConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + txtUser.getText().toString());
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Mohon melakukan konfigurasi dulu", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });

                } else {
                    inputData();
                    clearText();
                    txtUser.requestFocus();
                }
            }
        });


        /* Button Sync Tarif */
        btnSyncTarif = (Button) findViewById(R.id.btnSyncTarif);
        btnSyncTarif.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncTarif syncTarif = new SyncTarif();
                    syncTarif.execute();
                }
            }
        });


        /* Button Sync BPMA */
        btnSyncBPMA = (Button) findViewById(R.id.btnSyncBPMA);
        btnSyncBPMA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ipPort = txtUser.getText().toString();
                System.out.println("tes = " + ipPort);
                if (ipPort.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "IP dan Port tidak boleh kosong", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                } else {
                    SyncBPMA syncBPMA = new SyncBPMA();
                    syncBPMA.execute();
                }
            }
        });


        /* Button Cancel */
        btnCancelConfig = (Button) findViewById(R.id.btnBackConfig);
        btnCancelConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*initData();*/
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncUser extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String userURL = txtUser.getText().toString();
            String url = "http://" + userURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncUser&PDAMID=PDAMCoreV2";
            TbUser domain = new TbUser(getApplicationContext());
            domain.setImei(JoniUtils.getDeviceID(KonfigurasiActivity.this));
            JSONObject object = ConnectivityWS.postToServer(domain, url);
            System.out.print("Respon User = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                domain.delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbUser muser = domain;
                        JSONObject juser = jsonArray.getJSONObject(i);
                        System.out.print("Respon User2 = " + object);
                        Iterator keys = juser.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("USER_ID")) {
                                    muser.setUserid(String.valueOf(juser.get(key)));
                                } else {
                                    muser.getClass().getDeclaredField(JoniUtils.formatField(key)).set(muser, String.valueOf(juser.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        muser.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncWilayah extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String wilayahURL = String.valueOf(txtUser.getText());
            String url = "http://" + wilayahURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayah&PDAMID=PDAMCoreV2";
            JSONObject object = ConnectivityWS.postToServer(new TbWilayah(getApplicationContext()), url);
            System.out.print("Respon wilayah = " + object);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new TbWilayah(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbWilayah mwilayah = new TbWilayah(getApplicationContext());
                        JSONObject jwilayah = jsonArray.getJSONObject(i);
                        System.out.print("kudo = " + jwilayah);
                        Iterator keys = jwilayah.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("KD_WILAYAH")) {
                                    mwilayah.setKdWilayah(String.valueOf(jwilayah.get(key)));
                                } else {
                                    mwilayah.getClass().getDeclaredField(JoniUtils.formatField(key)).set(mwilayah, String.valueOf(jwilayah.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mwilayah.save();
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class SyncWilayahBaca extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String wilayahBacaURL = String.valueOf(txtUser.getText());
            String url = "http://" + wilayahBacaURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayahBaca&PDAMID=PDAMCoreV2";
            JSONObject object = ConnectivityWS.postToServer(new TbWilayahBaca(getApplicationContext()), url);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new TbWilayahBaca(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbWilayahBaca mwilayah = new TbWilayahBaca(getApplicationContext());
                        JSONObject jwilayah = jsonArray.getJSONObject(i);
                        System.out.print("kudo = " + jwilayah);
                        Iterator keys = jwilayah.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("KD_WILAYAHBACA")) {
                                    mwilayah.setKdWilayah(String.valueOf(jwilayah.get(key)));
                                } else {
                                    mwilayah.getClass().getDeclaredField(JoniUtils.formatField(key)).set(mwilayah, String.valueOf(jwilayah.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mwilayah.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncBlok extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String blockURL = String.valueOf(txtUser.getText());
            String url = "http://" + blockURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncJalan&PDAMID=PDAMCoreV2";
            JSONObject object = ConnectivityWS.postToServer(new TbBlok(getApplicationContext()), url);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new TbBlok(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbBlok mjalan = new TbBlok(getApplicationContext());
                        JSONObject jjalan = jsonArray.getJSONObject(i);
                        Iterator keys = jjalan.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                if (key.equals("KD_JALAN")) {
                                    mjalan.setKdJalan(String.valueOf(jjalan.get(key)));
                                } else {
                                    mjalan.getClass().getDeclaredField(JoniUtils.formatField(key)).set(mjalan, String.valueOf(jjalan.get(key)));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mjalan.save();
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncTarif extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String blockURL = String.valueOf(txtUser.getText());
            String url = "http://" + blockURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncTarif&PDAMID=PDAMCoreV2";
            JSONObject object = ConnectivityWS.postToServer(new TbTarif(getApplicationContext()), url);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new TbTarif(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbTarif mtarif = new TbTarif(getApplicationContext());
                        JSONObject jtarif = jsonArray.getJSONObject(i);
                        System.out.print("karambia = " + jtarif);
                        Iterator keys = jtarif.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                System.out.println(key + "=" + JoniUtils.formatField(key));
                                mtarif.getClass().getDeclaredField(JoniUtils.formatField(key)).set(mtarif, String.valueOf(jtarif.get(key)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mtarif.save();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SyncBPMA extends AsyncTask<String, String, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(KonfigurasiActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setCancelable(false);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String blockURL = String.valueOf(txtUser.getText());
            String url = "http://" + blockURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncBpma&PDAMID=PDAMCoreV2";
            JSONObject object = ConnectivityWS.postToServer(new TbBPMA(getApplicationContext()), url);
            if (object != null && object.has("DATA") || (object.has("CODE"))) {
                new TbBPMA(getApplicationContext()).delete();
                try {
                    JSONArray jsonArray = object.getJSONArray("DATA");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TbBPMA mbpma = new TbBPMA(getApplicationContext());
                        JSONObject jbpma = jsonArray.getJSONObject(i);
                        Iterator keys = jbpma.keys();
                        while (keys.hasNext()) {
                            try {
                                String key = ((String) keys.next()).trim();
                                mbpma.getClass().getDeclaredField(JoniUtils.formatField(key)).set(mbpma, String.valueOf(jbpma.get(key)));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mbpma.save();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    });
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi gagal", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing()) {
                dialog.dismiss();
                snackbar = Snackbar.make(coordinatorLayout, "Konfigurasi Berhasil", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }
    }

    /* Input Data */
    private void inputData() {

        try {
            TbKonfigurasi mk = new TbKonfigurasi(KonfigurasiActivity.this);

            String userURL = String.valueOf(txtUser.getText());
            urlUser = "http://" + userURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncUser&PDAMID=PDAMCoreV2";
            String wilayahURL = String.valueOf(txtUser.getText());
            urlWilayah = "http://" + wilayahURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayah&PDAMID=PDAMCoreV2";
            String wilayahBacaURL = String.valueOf(txtUser.getText());
            urlWilayahBaca = "http://" + wilayahBacaURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncWilayahBaca&PDAMID=PDAMCoreV2";
            String blockURL = String.valueOf(txtUser.getText());
            urlBlock = "http://" + blockURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncJalan&PDAMID=PDAMCoreV2";
            String tarifURL = String.valueOf(txtUser.getText());
            urlTarif = "http://" + tarifURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncTarif&PDAMID=PDAMCoreV2";
            String bpmaURL = String.valueOf(txtUser.getText());
            urlBPMA = "http://" + bpmaURL + "/pdamws/pdam/corev2?USERID=123&WEBCMD=SyncBpma&PDAMID=PDAMCoreV2";
            String downloadURL = String.valueOf(txtUser.getText());
            urlDownloadNew = "http://" + downloadURL + "/pdamws/pdam/corev2?WEBCMD=DownloadDsmlNew&PDAMID=PDAMCoreV2";
            String uploadURL = String.valueOf(txtUser.getText());
            urlUpload = "http://" + uploadURL + "/pdamws/pdam/corev2?WEBCMD=MeterReading&PDAMID=PDAMCoreV2";

            /*http://10.12.1.79:8080/pdamws/pdam/corev2*/

            mk.kdKonfigurasi = "1";
            mk.wilayahURL = urlWilayah;
            mk.userURL = urlUser;
            mk.wilayahBacaURL = urlWilayahBaca;
            mk.jalanURL = urlBlock;
            mk.tarifURL = urlTarif;
            mk.bpmaURL = urlBPMA;
            mk.downloadURL = urlDownloadNew;
            mk.uploadURL = urlUpload;

            mk.update();

            /*if (mk.retrieveByID() != null) {
                mk.update();
                System.out.println("dataupdate = ");
            } else {
                mk.save();
                System.out.println("datasave = ");
            }*/

            if (mk.retrieveByID() != null) {
                mk.update();
                System.out.println("dataupdate = ");
            } else if (mk.userURL.equals("") || mk.userURL == null) {
                mk.userURL = txtUser.getText().toString();
            } else {
                mk.save();
                System.out.println("datasave = ");
            }


            new AlertDialog.Builder(this)
                    .setTitle("Simpan Data")
                    .setMessage("Data konfigurasi telah disimpan")
                    //.setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(KonfigurasiActivity.this, LoginActivity.class));
                        }
                    }).show();
        } catch (
                Exception e)

        {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("Simpan Data")
                    .setMessage("Simpan data gagal karena : " + e.getMessage())
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setNeutralButton("Ok ", null)
                    .show();
        }
    }


    /* Clear */
    private void clearText() {
        txtUser.setText("");
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

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(KonfigurasiActivity.this);

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
                            KonfigurasiActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(new Intent(KonfigurasiActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }
}

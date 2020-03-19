package com.project.fortuna.meterreading.dsml;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.fortuna.meterreading.CalendarAdapter;
import com.project.fortuna.meterreading.CircularProgressBar;
import com.project.fortuna.meterreading.DB.DefaultDTO;
import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.DB.TbKonfigurasi;
import com.project.fortuna.meterreading.DB.TbWilayahBaca;
import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.util.AndroidVersion;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.ConnectivityWS;
import com.project.fortuna.meterreading.util.RecyclerItemClickListener;
import com.project.fortuna.meterreading.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ImportDSMLFromNetworkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinWilayah, spinTglAwal, spinTglAkhir, spinPilihanData;
    private Button btnDownloadDSML;
    private SimpleDateFormat dateFormatter;
    private EditText txtPeriode;

    private TextView dialogMonthYearOnlyYear;
    private ImageButton leftClik, rightClick;
    private String status_, kd_wilayah_baca, totalData;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String TAG = ImportDSMLFromNetworkActivity.class.getSimpleName();
    private SharedPreferences pref;

    private List<TbBacaMeter> listMeter = new ArrayList<TbBacaMeter>();
    private ProgressDialog pDialog;

    public static int year, month, constantYear;
    private String bulan;
    public String[] Listmonth = {
            "Jan ",
            "Feb",
            "Mar",
            "April",
            "Mei",
            "Juni",
            "Juli ",
            "Agus",
            "Sept",
            "Okto",
            "Nov",
            "Des"
    };

    String[] statusData = {
            "Semua Data",
            "Data Sudah Dibaca",
            "Data Belum Dibaca"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_dsmlfrom_network);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Import DSML From Network");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtPeriode = findViewById(R.id.txtPeriode);
        txtPeriode.setInputType(InputType.TYPE_NULL);

        isInternetOn();

        year = Calendar.getInstance().get(Calendar.YEAR);
        constantYear = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        dateFormatter = new SimpleDateFormat("yyyyMM", Locale.US);
        txtPeriode = findViewById(R.id.txtPeriode);
        txtPeriode.setInputType(InputType.TYPE_NULL);
        txtPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog builder = new Dialog(ImportDSMLFromNetworkActivity.this);
                builder.setContentView(R.layout.date_picker_dialog);
                builder.setTitle("Pilih Periode");

                year = Calendar.getInstance().get(Calendar.YEAR);
                constantYear = Calendar.getInstance().get(Calendar.YEAR);
                month = Calendar.getInstance().get(Calendar.MONTH) + 1;

                dialogMonthYearOnlyYear = builder.findViewById(R.id.dialogMonthYearOnlyYear);
                dialogMonthYearOnlyYear.setText(String.valueOf(year));
                leftClik = builder.findViewById(R.id.dialogMonthYearLeftButton);
                rightClick = builder.findViewById(R.id.dialogMonthYearRightButton);

                leftClik.setOnClickListener(this);
                rightClick.setOnClickListener(this);

                leftClik.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (2000 < year) {
                            --year;
                        }
                        dialogMonthYearOnlyYear.setText(String.valueOf(year));
                    }
                });

                rightClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (constantYear > year) {
                            ++year;
                        }
                        dialogMonthYearOnlyYear.setText(String.valueOf(year));
                    }
                });

                mRecyclerView = builder.findViewById(R.id.gridBulan);
                mLayoutManager = new GridLayoutManager(ImportDSMLFromNetworkActivity.this, 3);
                mRecyclerView.setLayoutManager(mLayoutManager);

                ArrayList<AndroidVersion> av = prepareData();
                CalendarAdapter mAdapter = new CalendarAdapter(ImportDSMLFromNetworkActivity.this, av);

                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(ImportDSMLFromNetworkActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int i) {

                                switch (i) {
                                    case 0:
                                        bulan = String.valueOf("01");
                                        if (bulan.equals(month)) {
                                        }
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 1:
                                        bulan = String.valueOf("02");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 2:
                                        bulan = String.valueOf("03");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 3:
                                        bulan = String.valueOf("04");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 4:
                                        bulan = String.valueOf("05");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 5:
                                        bulan = String.valueOf("06");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 6:
                                        bulan = String.valueOf("07");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 7:
                                        bulan = String.valueOf("08");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 8:
                                        bulan = String.valueOf("09");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 9:
                                        bulan = String.valueOf("10");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 10:
                                        bulan = String.valueOf("11");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                    case 11:
                                        bulan = String.valueOf("12");
                                        builder.dismiss();
                                        periodeTahunBulan();
                                        break;
                                }
                            }
                        })
                );
                builder.show();

            }
        });

        initComponent();
        loadSpinnerData();
    }

    private void periodeTahunBulan() {

        System.out.println("joni = " + bulan);
        String tahun = dialogMonthYearOnlyYear.getText().toString();
        String tahunBulan = (tahun + "" + bulan);
        System.out.println("tahun joni = " + tahunBulan);
        txtPeriode.setText(tahunBulan);

    }

    private void initComponent() {
        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0); // 0 - for private mode
        spinTglAwal = findViewById(R.id.spinTglAwal);
        spinTglAkhir = findViewById(R.id.spinTglAkhir);

        spinPilihanData = findViewById(R.id.spinStatusData);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusData);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPilihanData.setAdapter(aa);
        spinPilihanData.post(new Runnable() {
            @Override
            public void run() {
                spinPilihanData.setSelection(0);
            }
        });
        spinPilihanData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    status_ = "status_";
                } else if (position == 1) {
                    status_ = "status_sudah";
                } else {
                    status_ = "status_belum";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnDownloadDSML = findViewById(R.id.btnDownloadDSML);
        btnDownloadDSML.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baruak();

            }
        });

        spinWilayah = findViewById(R.id.spinWilayah);
        spinWilayah.setOnItemSelectedListener(new SpinWilayahClick());
    }

    private void baruak() {

        if (spinTglAwal.getSelectedItem().toString().equals("")) {

            Util.showmsg(ImportDSMLFromNetworkActivity.this, null, "Silahkan pilih tanggal baca awal");

        } else if (spinTglAkhir.getSelectedItem().toString().equals("")) {

            Util.showmsg(ImportDSMLFromNetworkActivity.this, null, "Silahkan pilih tanggal baca akhir");

        } else {

            setListData task = new setListData(ImportDSMLFromNetworkActivity.this);
            task.execute();

            /*Intent i = new Intent(ImportDSMLFromNetworkActivity.this, KonfirmasiDownloadActivity.class);
            i.putExtra("tglAwal", tglAwal);
            i.putExtra("tglAkhir", tglAkhir);
            i.putExtra("periode", periode);
            i.putExtra("status_", status_);
            i.putExtra("kd_wilayah", kd_wilayah_baca);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);*/

        }


    }

    private void loadSpinnerData() {

        TbWilayahBaca db = new TbWilayahBaca(getApplicationContext());
        List<TbWilayahBaca> lables = db.retrieveAll();
        List<String> wilBaca = new ArrayList<>();

        for (int i = 0; i < lables.size(); i++) {
            wilBaca.add(lables.get(i).getNmWilayah());
        }


        System.out.println("DATA SPINNER " + wilBaca.toString());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, wilBaca);
        spinWilayah.setAdapter(dataAdapter);
    }

    private class SpinWilayahClick implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (position == 0) {
                kd_wilayah_baca = String.valueOf(1);
            }
            if (position == 1) {
                kd_wilayah_baca = String.valueOf(2);
            }
            if (position == 2) {
                kd_wilayah_baca = String.valueOf(3);
            }
            if (position == 3) {
                kd_wilayah_baca = String.valueOf(4);
            }
            if (position == 4) {
                kd_wilayah_baca = String.valueOf(5);
            }
            if (position == 5) {
                kd_wilayah_baca = String.valueOf(6);
            }
            if (position == 6) {
                kd_wilayah_baca = String.valueOf(7);
            }
            if (position == 7) {
                kd_wilayah_baca = String.valueOf(8);
            }
            if (position == 8) {
                kd_wilayah_baca = String.valueOf(10);
            }
            if (position == 9) {
                kd_wilayah_baca = String.valueOf(11);
            }
            if (position == 10) {
                kd_wilayah_baca = String.valueOf(12);
            }
            if (position == 11) {
                kd_wilayah_baca = String.valueOf(13);
            }
            if (position == 12) {
                kd_wilayah_baca = String.valueOf(14);
            }
            if (position == 13) {
                kd_wilayah_baca = String.valueOf(15);
            }
            if (position == 14) {
                kd_wilayah_baca = String.valueOf(16);
            }
            if (position == 15) {
                kd_wilayah_baca = String.valueOf(17);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < Listmonth.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(Listmonth[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }

    public final void isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            Log.e(TAG, "Connected");
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ImportDSMLFromNetworkActivity.this);

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
                            ImportDSMLFromNetworkActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(new Intent(ImportDSMLFromNetworkActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class setListData extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;

        public setListData(ImportDSMLFromNetworkActivity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(ImportDSMLFromNetworkActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Downloading");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                startActivity(new Intent(ImportDSMLFromNetworkActivity.this, KonfirmasiDownloadActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected Void doInBackground(Void... strings) {
            String tglAwal = spinTglAwal.getSelectedItem().toString();
            String tglAkhir = spinTglAkhir.getSelectedItem().toString();
            String periode = txtPeriode.getText().toString();

            System.out.println("status data = " + status_);
            System.out.println("tglAwal = " + tglAwal);
            System.out.println("tglAkhir = " + tglAkhir);
            System.out.println("kd_wilayah = " + kd_wilayah_baca);
            System.out.println("periode = " + periode);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
            SharedPreferences.Editor i = pref.edit();
            i.putString("tglAwal", tglAwal);
            i.putString("tglAkhir", tglAkhir);
            i.putString("periode", periode);
            i.putString("status_", status_);
            i.putString("kd_wilayah", kd_wilayah_baca);
            i.commit();
            try {
                Thread.sleep(12000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


}




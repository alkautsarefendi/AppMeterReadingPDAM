package com.project.fortuna.meterreading.bacameter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InputMeterOneStepKonfirmasiActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView tvKodePel, tvNamaPel, tvNoMeter, tvGolTarif, tvDiameter, tvAlamat, tvStatus, tvStandMeter, tvTotalPakai, tvTagihan;
    private Button btnSimpanKonfirmasi;
    private SharedPreferences pref;
    private String kodePelPref, namaPelPref, noMeterPref, golTarifPref, diameterPref, alamatPref, statusPref, namaFilePref,
            kodeStatusPref, standMeterPref, totalPakaiPref, tagihanPref, latitudePref, longitudePref, newLatPref, newLongPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meter_one_step_konfirmasi);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Meter Konfirmasi ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);

        initComponent();
        getDataFromOneStep();
    }

    private void initComponent() {

        tvKodePel = (TextView) findViewById(R.id.tvKodePel);
        tvNamaPel = (TextView) findViewById(R.id.tvNamaPel);
        tvNoMeter = (TextView) findViewById(R.id.tvNoMeter);
        tvGolTarif = (TextView) findViewById(R.id.tvGolTarif);
        tvDiameter = (TextView) findViewById(R.id.tvDiameter);
        tvAlamat = (TextView) findViewById(R.id.tvAlamat);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvStandMeter = (TextView) findViewById(R.id.tvStandMeter);
        tvTotalPakai = (TextView) findViewById(R.id.tvTotalPakai);
        tvTagihan = (TextView) findViewById(R.id.tvTagihan);

        btnSimpanKonfirmasi = (Button) findViewById(R.id.btnSimpanKonfirmasi);
        btnSimpanKonfirmasi.setOnClickListener(new ButtonSimpanKonfirmasiClickHandler());

    }

    private void getDataFromOneStep() {

        SharedPreferences sharedpreferences = getSharedPreferences(InputMeterOneStepActivity.OneStepPreferences, Context.MODE_PRIVATE);
        kodePelPref = sharedpreferences.getString("kodePelKey", "");
        namaPelPref= sharedpreferences.getString("namaPelKey", "");
        noMeterPref = sharedpreferences.getString("nomorMeterKey", "");
        golTarifPref = sharedpreferences.getString("golTarifKey", "");
        diameterPref = sharedpreferences.getString("diameterKey", "");
        alamatPref = sharedpreferences.getString("alamatKey", "");
        statusPref = sharedpreferences.getString("statusKey", "");
        kodeStatusPref = sharedpreferences.getString("kodeStatusKey", "");
        namaFilePref = sharedpreferences.getString("namaFileKey", "");
        standMeterPref = sharedpreferences.getString("standMeterKey", "");
        totalPakaiPref = sharedpreferences.getString("totalPakaiKey", "");
        tagihanPref = sharedpreferences.getString("totalTagihanKey", "");
        latitudePref = sharedpreferences.getString("latitudeKey", "");
        longitudePref = sharedpreferences.getString("longitudeKey", "");
        newLatPref = sharedpreferences.getString("newLatKey", "");
        newLongPref = sharedpreferences.getString("newLongKey", "");

        tvKodePel.setText(kodePelPref);
        tvNamaPel.setText(namaPelPref);
        tvNoMeter.setText(noMeterPref);
        tvGolTarif.setText(golTarifPref);
        tvDiameter.setText(diameterPref);
        tvAlamat.setText(alamatPref);
        tvStatus.setText(kodeStatusPref);
        tvStandMeter.setText(standMeterPref);
        tvTotalPakai.setText(totalPakaiPref);
        tvTagihan.setText(tagihanPref);

    }


    private class ButtonSimpanKonfirmasiClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String mesgErr = "";

            if (mesgErr.equals("")) {

                TbBacaMeter meter = new TbBacaMeter(getApplicationContext());
                meter.KD_PELANGGAN = "'" + tvKodePel.getText().toString();
                meter.PELANGGAN = tvNamaPel.getText().toString();
                meter.NO_METER = tvNoMeter.getText().toString();
                meter.ALMT_PASANG = tvAlamat.getText().toString();
                meter.M3_AKHIR = tvStandMeter.getText().toString();
                meter.STATUS_BACA = "0";
                meter.FOTO = namaFilePref;
                meter.LATITUDE = "" + latitudePref;
                meter.LONGITUDE = "" + longitudePref;
                meter.GPS_LAT = "" + newLatPref;
                meter.GPS_LONG = "" + newLongPref;
                meter.USER_ID = pref.getString("userid", "");
                meter.S_KIRIM = "0";
                meter.TGL_WAKTU_BACA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                meter.PERIODE = new SimpleDateFormat("yyyyMM").format(new Date());
                meter.PREDIKSI_TAGIHAN = tvTagihan.getText().toString().replace("Rp. ", "");
                meter.KUBIKASI = tvTotalPakai.getText().toString().replace(" M3", "");

                meter.update();
                AlertDialog.Builder builder = new AlertDialog.Builder(InputMeterOneStepKonfirmasiActivity.this);
                builder.setTitle(getString(R.string.action_input))
                        .setMessage("Input Data Sukses")
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.project.fortuna.meterreading.export;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.project.fortuna.meterreading.config.KonfigurasiActivity;
import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.dsml.ImportDSMLFromNetworkActivity;
import com.project.fortuna.meterreading.dsml.KonfirmasiDownloadActivity;
import com.project.fortuna.meterreading.util.ConnectivityWS;
import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.DB.TbKonfigurasi;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportDariJaringanActivity extends AppCompatActivity {

    private Toolbar toolbar;
    protected Button btnExport, btnExportCancel;
    private CustomCalendarView calendarView;
    private String tgl, bulan, tahun;
    protected String userid;
    private String sTglMeter;
    private static final String TAG = ExportDariJaringanActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_dari_jaringan);

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

        initComponent();
        isInternetOn();

    }

    private void initComponent() {

        btnExportCancel = (Button) findViewById(R.id.btnExportCanceltoLocal);
        btnExportCancel.setOnClickListener(new ExportDariJaringanActivity.ButtonExportCancelClickHandler());
        btnExport = (Button) findViewById(R.id.btnExporttoLocal);
        btnExport.setOnClickListener(new ExportDariJaringanActivity.ButtonExportClickHandler());

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(new CalendarListener() {

            @Override
            public void onDateSelected(Date date) {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat tf = new SimpleDateFormat("dd");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat mf = new SimpleDateFormat("MM");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat yf = new SimpleDateFormat("yyyy");
                tgl = tf.format(date);
                bulan = mf.format(date);
                tahun = yf.format(date);
                sTglMeter = String.valueOf(tahun + "-" + bulan + "-" + tgl);
                System.out.println("sTglMeter = "+sTglMeter);
                Toast.makeText(ExportDariJaringanActivity.this, String.valueOf(tgl + "-" + bulan + "-" + tahun), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
                Toast.makeText(ExportDariJaringanActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

        /*//Setting custom font
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Arch_Rival_Bold.ttf");
        if (null != typeface) {
            calendarView.setCustomTypeface(typeface);
            calendarView.refreshCalendar(currentCalendar);
        }*/

    }

    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate())) {
                int color = Color.parseColor("#a9afb9");
                dayView.setBackgroundColor(color);
            }
        }
    }

    /* Button Cancel */
    public class ButtonExportCancelClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            finish();
        }
    }


    /* Button Export */
    public class ButtonExportClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            exportData();
        }
    }

    private void exportData() {

        /*Intent param = getIntent();
        String periode = param.getExtras().getString("periode");
        if (periode!=null){
            startActivity(new Intent(ExportDariJaringanActivity.this, KonfirmasiUploadActivity.class));
        } else {
            Toast.makeText(ExportDariJaringanActivity.this, "Data tidak ditemukan", Toast.LENGTH_LONG).show();
        }*/
        Intent param = new Intent(ExportDariJaringanActivity.this, KonfirmasiUploadActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sTglMeter", sTglMeter);
        param.putExtras(bundle);
        startActivity(param);

        /*startActivity(new Intent(ExportDariJaringanActivity.this, KonfirmasiUploadActivity.class));*/

    }


    /* Clear */

    public final boolean isInternetOn() {

        ConnectivityManager connec = (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED||
                connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING||
                connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED){
            Log.e(TAG,"Connected");
        }else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExportDariJaringanActivity.this);

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
                            ExportDariJaringanActivity.super.onBackPressed();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                            startActivity(new Intent(ExportDariJaringanActivity.this, LoginActivity.class));
                        }
                    });
            AlertDialog alert = alertDialog.create();
            alert.show();
            return false;
        }
        return false;
    }

}

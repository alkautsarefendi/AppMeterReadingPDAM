package com.project.fortuna.meterreading.export;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExportDariKomputerActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CustomCalendarView calendarView;
    protected EditText mEditText;
    protected Button btnExport, btnExportCancel;
    protected String userid;
    protected String username;
    private String tgl, bulan, tahun;
    protected SharedPreferences pref;
    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/CSV/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_dari_komputer);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export File Local");
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
        username = pref.getString("username", "");

        initComponent();
    }
    private void initComponent() {

        btnExportCancel = (Button) findViewById(R.id.btnExportCanceltoLocal);
        btnExportCancel.setOnClickListener(new ButtonExportCancelClickHandler());
        btnExport = (Button) findViewById(R.id.btnExporttoLocal);
        btnExport.setOnClickListener(new ButtonExportClickHandler());

        calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);
        mEditText = (EditText) findViewById(R.id.txtExportFile);


        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH
                        + " on sdcard failed");
            } else {
                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");
            }
        }

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
                Toast.makeText(ExportDariKomputerActivity.this, String.valueOf(tgl + "-" + bulan + "-" + tahun), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MMM yyyy");
                Toast.makeText(ExportDariKomputerActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });


        //adding calendar day decorators
        List<DayDecorator> decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        calendarView.setDecorators(decorators);
        calendarView.refreshCalendar(currentCalendar);

       /* //Setting custom font
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


    /*
     * AGS 02072013
	 */
    private void exportData() {
        /*String sTglMeter = tahun + "-" + (bulan + 1 < 10 ? "0" + (bulan + 1) : "" + (bulan + 1)) + "-" + (tgl < 10 ? "0" + tgl : "" + tgl);*/
        String sTglMeter = tahun + "-" + bulan + "-" + tgl;
        System.out.println(sTglMeter);
        String tglExport = sTglMeter.replaceAll("-", "");
        String sFileName = DATA_PATH + "M" + "_" + username + "_" + tglExport + ".csv";
        String editTextField = "M" + "_" + username + "_" + tglExport + ".csv";
        String sLine = "";
        System.out.println("sTglMeter = "+sTglMeter);

        TbBacaMeter ds2 = new TbBacaMeter(getApplicationContext());
        List<TbBacaMeter> retrieveForExport = ds2.retrieveForExport(userid, sTglMeter);

        if (retrieveForExport.size() == 0) {
            Util.showmsg(ExportDariKomputerActivity.this, "Peringatan :", getString(R.string.msg_export_notok) + ", Belum ada.");
        } else {
            for (TbBacaMeter bm : retrieveForExport) {
                sLine = sLine + bm.getKD_PELANGGAN().replace("'", "") + ",";
                sLine = sLine + bm.getUSER_ID() + ",";
                sLine = sLine + bm.getPERIODE() + ",";
                sLine = sLine + bm.getM3_AWAL() + ",";
                sLine = sLine + bm.getM3_AKHIR().replace(" ", "") + ",";
                sLine = sLine + bm.getSTATUS_BACA() + ",";
                sLine = sLine + bm.getFOTO() + ",";
                sLine = sLine + bm.getGPS_LAT() + ",";
                sLine = sLine + bm.getGPS_LONG() + ",";
                sLine = sLine + bm.getTGL_WAKTU_BACA() + "\n";
            }

            boolean writeFile = Util.writeFile(sFileName, sLine);
            if (writeFile) {
                Util.showmsg(ExportDariKomputerActivity.this, "Export File", "Sukses export " + retrieveForExport.size() + " Baca Meter Pelanggan.");
                mEditText.setText(editTextField);
                for (TbBacaMeter bm : retrieveForExport) {
                    bm.S_KIRIM = "1";
                    bm.update();
                }
            } else {
                Util.showmsg(ExportDariKomputerActivity.this, "Import File", "Gagal import file to " + sFileName);
            }
        }
    }


    /* Clear */
    public void clearExport() {
        mEditText.setText("");
    }
}

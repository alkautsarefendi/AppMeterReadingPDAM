package com.project.fortuna.meterreading.review;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReviewMeterSudahSortByBlokActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private SharedPreferences pref;
    private String[] menuItems = {""};
    private List<TbBacaMeter> listBlok;
    private List<TbBacaMeter> listMeter;
    private ListView reviewBlokList;

    private Boolean viewData = true;
    private boolean isSukses;
    private TextView txtTotalBlok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_meter_sudah_sort_by_blok);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Sudah dibaca");
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
    }

    private void initComponent() {

        reviewBlokList = (ListView) findViewById(R.id.id_review_blok_sudah);
        reviewBlokList.setOnItemClickListener(new ListBlokMeterSudahClickHandler());
        txtTotalBlok = (TextView) findViewById(R.id.txtViewTotalDataSudah);
        initListData();

    }


    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        initListData();

    }


    private void initListData() {
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        String userid = sharedPreferences.getString("userid", "");
        String sTglMeter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        isSukses = extras.getBoolean("isSukses");

        listBlok = new TbBacaMeter(ReviewMeterSudahSortByBlokActivity.this).retrieveMeterBlok(userid, sTglMeter, false, extras.getBoolean("isSukses"));
        listMeter = new TbBacaMeter(ReviewMeterSudahSortByBlokActivity.this).retrieveDataSudah(userid, sTglMeter, isSukses);
		/* CheckBox Belum */
        if (extras.getBoolean("isSukses")) {
            if (listBlok.size() > 0) {
                ReviewMeterSudahSortByBlokAdapter adapter = new ReviewMeterSudahSortByBlokAdapter(this, listBlok);
                reviewBlokList.setAdapter(adapter);
                String jmlData = String.valueOf(listMeter.size());
                System.out.println("Jumlah Data "+jmlData);
                txtTotalBlok.setText("Total Data = "+listMeter.size());
            } else {
                reviewBlokList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewMeterSudahSortByBlokActivity.this);
                builder.setTitle("Peringatan :")
                        .setMessage("Status " + getString(R.string.msg_no_data_blok_sudah))
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
        }
    }


    private class ListBlokMeterSudahClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                final TbBacaMeter lvm = listBlok.get(position);

                Intent reviewSudah = new Intent(ReviewMeterSudahSortByBlokActivity.this, ViewMeterSudahActivity.class);
                reviewSudah.putExtra("isSukses", viewData);
                reviewSudah.putExtra("namaJalan", lvm.getNM_JALAN());
                startActivity(reviewSudah);

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(ReviewMeterSudahSortByBlokActivity.this, "Peringatan :", e.toString());

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}

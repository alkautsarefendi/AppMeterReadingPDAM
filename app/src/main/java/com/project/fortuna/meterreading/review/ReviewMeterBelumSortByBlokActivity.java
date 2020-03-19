package com.project.fortuna.meterreading.review;

import android.annotation.SuppressLint;
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

public class ReviewMeterBelumSortByBlokActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private Toolbar toolbar;

    private String[] menuItems = {""};
    private List<TbBacaMeter> listBlok;
    private List<TbBacaMeter> listMeter;
    private ListView reviewBlokList;
    private TextView txtTotalData;

    private Boolean viewData = true;
    private boolean isBelum;

    @SuppressLint("SetTextI18n")
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_meter_belum_sort_by_blok);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Belum dibaca");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);

        txtTotalData = (TextView)findViewById(R.id.txtViewTotalDataBelum);

        initComponent();
    }

    private void initComponent() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        reviewBlokList = (ListView) findViewById(R.id.id_review_blok_belum);
        reviewBlokList.setOnItemClickListener(new ListBlokMeterBelumClickHandler());

        initListData();
    }


    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        initListData();
    }


    private void initListData() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        String userid = sharedPreferences.getString("userid", "");
        String sTglMeter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final TbBacaMeter lvm = new TbBacaMeter(getApplicationContext());

        if (extras != null) {
            isBelum = extras.getBoolean("isBelum");

            if (isBelum) {
                System.out.print("jalankan query is = "+ isBelum);
                listBlok = new TbBacaMeter(ReviewMeterBelumSortByBlokActivity.this).retrieveMeterBlok(userid, sTglMeter, isBelum, false);
                listMeter = new TbBacaMeter(ReviewMeterBelumSortByBlokActivity.this).retrieveDataBelum(userid, sTglMeter,isBelum,false);


                if (listBlok.size() > 0) {
                    ReviewMeterBelumSortByBlokAdapter adapter = new ReviewMeterBelumSortByBlokAdapter(this, listBlok);
                    reviewBlokList.setAdapter(adapter);
                    String jmlBlok = String.valueOf(listBlok.size());
                    String jmlData = String.valueOf(listMeter.size());

                    System.out.println("Jumlah Kudo = "+jmlBlok);
                    System.out.println("Jumlah Data = "+jmlData);
                    txtTotalData.setText("Total Data = "+jmlData);
                } else {
                    reviewBlokList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReviewMeterBelumSortByBlokActivity.this);
                    builder.setTitle("Peringatan :")
                            .setMessage("Status " + getString(R.string.msg_no_data_blok_belum))
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

            /*if(isBelum) {
                listMeter = new TbBacaMeter(ReviewMeterBelumSortByBlokActivity.this).retrieveMeterBlok(userid, sTglMeter, isBelum, false);
            }*/
        }

		/* CheckBox Belum */

    }


    private class ListBlokMeterBelumClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                final TbBacaMeter lvm = listBlok.get(position);

                Intent reviewBelum = new Intent(ReviewMeterBelumSortByBlokActivity.this, ViewMeterBelumActivity.class);
                reviewBelum.putExtra("isBelum", viewData);
                reviewBelum.putExtra("namaJalan", lvm.getNM_JALAN());
                startActivity(reviewBelum);

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(ReviewMeterBelumSortByBlokActivity.this, "Peringatan :", e.toString());

            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}

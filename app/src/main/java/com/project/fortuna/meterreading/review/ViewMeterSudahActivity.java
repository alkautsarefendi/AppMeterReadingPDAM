package com.project.fortuna.meterreading.review;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class ViewMeterSudahActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar toolbar;

    private String[] menuItems = {""};
    private List<TbBacaMeter> listMeter;
    private List<TbBacaMeter> tempListMeter;
    private TextView txtTotal;
    private ListView reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meter_sudah);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Sudah Baca Meter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initComponent();
        initListData();

    }

    private void initComponent() {

        txtTotal = (TextView) findViewById(R.id.txtViewTotal);
        reviewList = (ListView) findViewById(R.id.id_review_list_view);
        reviewList.setOnItemClickListener(new ListMeterSudahClickHandler());

    }


    private void initListData() {
        Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        String userid = sharedPreferences.getString("userid", "");
        String sTglMeter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sNamaJalan = extras.getString("namaJalan");
        System.out.println(sTglMeter);

        listMeter = new TbBacaMeter(ViewMeterSudahActivity.this).retrieveForReview(userid, sTglMeter, false, extras.getBoolean("isSukses"), sNamaJalan);
        tempListMeter = listMeter;

		/* CheckBox Sukses */
        if (extras.getBoolean("isSukses"))
            txtTotal.setText("Total Data yang Sukses di Baca = " + listMeter.size());
        {
            if (listMeter.size() > 0) {
                ReviewMeterAdapter adapter = new ReviewMeterAdapter(ViewMeterSudahActivity.this, listMeter);
                reviewList.setAdapter(adapter);
            } else {
                reviewList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMeterSudahActivity.this);
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
        }
    }


    /* Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuviewsudah, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(ViewMeterSudahActivity.this);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onQueryTextSubmit(String newText) {
        // this is your adapter that will be filtered
        if (TextUtils.isEmpty(newText)) {

        } else {
            Bundle extras = getIntent().getExtras();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
            String userid = sharedPreferences.getString("userid", "");
            String sTglMeter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String sNamaJalan = extras.getString("namaJalan");
            tempListMeter = listMeter;
            List<TbBacaMeter> lm = new TbBacaMeter(ViewMeterSudahActivity.this).retrieveForReview(userid, sTglMeter, false, extras.getBoolean("isSukses"), sNamaJalan);
            lm.clear();
            for (TbBacaMeter tb : listMeter) {
                if (tb.getALMT_PASANG().toLowerCase().contains(newText.toLowerCase())) {
                    lm.add(tb);
                }
            }
            ReviewMeterAdapter adapter = new ReviewMeterAdapter(this, lm);
            tempListMeter = lm;
            reviewList.setAdapter(adapter);
        }

        return true;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("") || newText.isEmpty()) {
            initListData();
        }
        return false;

    }


    private class ListMeterSudahClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {
            try {
                final TbBacaMeter lvm = tempListMeter.get(position);

                Util.showmsg(ViewMeterSudahActivity.this, getString(R.string.action_view), "Kode Pelanggan = "
                        + lvm.getKD_PELANGGAN().replace("'", "") + "\n" + "Nama = " + lvm.getPELANGGAN()
                        + "\n" + "Alamat = " + lvm.getALMT_PASANG()
                        + "\n" + "No Meter = " + lvm.getNO_METER()
                        + "\n" + "Meter Akhir = " + lvm.getM3_AKHIR()
                        + "\n" + "Latitude Baru " + lvm.getGPS_LAT()
                        + "\n" + "Longitude Baru " + lvm.getGPS_LONG()
                        + "\n" + "Status Baca = " + lvm.getSTATUS_BACA()
                        + "\n" + "Kubikasi = " + lvm.getKUBIKASI()
                        + "\n" + "Total Tagihan = " + lvm.getPREDIKSI_TAGIHAN()
                        + "\n" + "Tgl Baca = " + lvm.getTGL_WAKTU_BACA());

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(ViewMeterSudahActivity.this, "Peringatan :", e.toString());

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

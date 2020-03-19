package com.project.fortuna.meterreading.review;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.project.fortuna.meterreading.bacameter.InputMeterTwoStepActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewMeterBelumActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Toolbar toolbar;

    private String[] menuItems = {""};
    private List<TbBacaMeter> listMeter;
    private List<TbBacaMeter> tempListMeter;
    private TextView txtTotal;
    private ListView reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meter_belum);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Data Belum Baca Meter");
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
    }

    private void initComponent() {

        txtTotal = (TextView) findViewById(R.id.txtViewTotal);
        reviewList = (ListView) findViewById(R.id.id_review_list_view);
        reviewList.setOnItemClickListener(new ListMeterBelumClickHandler());

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
        String sNamaJalan = extras.getString("namaJalan");
        System.out.println("Nama jalan = "+sNamaJalan);

        listMeter = new TbBacaMeter(ViewMeterBelumActivity.this).retrieveForReview(userid, sTglMeter, extras.getBoolean("isBelum"), false, sNamaJalan);
        tempListMeter = listMeter;

		/* CheckBox Belum */
        if (extras.getBoolean("isBelum"))
            txtTotal.setText("Total Data yang Belum di Baca = " + listMeter.size());
        {
            if (listMeter.size() > 0) {
                ReviewMeterAdapter adapter = new ReviewMeterAdapter(this, listMeter);
                reviewList.setAdapter(adapter);
            } else {
                reviewList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItems));
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMeterBelumActivity.this);
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
        getMenuInflater().inflate(R.menu.menuviewbelum, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(ViewMeterBelumActivity.this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // this is your adapter that will be filtered
        if (query.isEmpty() || query.equals("")) {

        } else {
            Bundle extras = getIntent().getExtras();
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
            String userid = sharedPreferences.getString("userid", "");
            String sTglMeter = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String sNamaJalan = extras.getString("namaJalan");

            tempListMeter = listMeter;
            List<TbBacaMeter> lm = new TbBacaMeter(ViewMeterBelumActivity.this).retrieveForReview(userid, sTglMeter, extras.getBoolean("isBelum"), false, sNamaJalan);
            lm.clear();
            for (TbBacaMeter tb : tempListMeter) {
                if (tb.getALMT_PASANG().toLowerCase().contains(query.toLowerCase())) {
                    lm.add(tb);
                }
            }
            ReviewMeterAdapter adapter = new ReviewMeterAdapter(this, lm);
            tempListMeter = lm;
            reviewList.setAdapter(adapter);
        }

        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.equals("") || newText.isEmpty()) {
            initListData();
        }
        return false;

    }


    private class ListMeterBelumClickHandler implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> l, View v, int position, long id) {

            try {
                final TbBacaMeter lvm = tempListMeter.get(position);

                Intent intent = new Intent(ViewMeterBelumActivity.this, InputMeterTwoStepActivity.class);
                intent.putExtra("key", lvm.getKD_PELANGGAN().replace("'", ""));
                startActivity(intent);

            } catch (Exception e) {

                e.printStackTrace();
                Log.e("getData", "Error: " + e.toString());
                Util.showmsg(ViewMeterBelumActivity.this, "Peringatan :", e.toString());

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

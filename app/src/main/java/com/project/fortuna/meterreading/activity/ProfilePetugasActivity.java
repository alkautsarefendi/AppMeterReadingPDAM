package com.project.fortuna.meterreading.activity;

import android.content.SharedPreferences;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.project.fortuna.meterreading.DB.TbUser;
import com.project.fortuna.meterreading.adapter.ProfileMeterAdapter;
import com.project.fortuna.meterreading.R;

import java.io.File;
import java.util.List;

public class ProfilePetugasActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Icon/";

    private List<TbUser> listUser;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_petugas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile Petugas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);

        // init
        File dir = new File(ICON_DEFAULT);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + ICON_DEFAULT + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + ICON_DEFAULT + " on sdcard");
            }
        }

        initComponent();
    }

    private void initComponent() {

        String userid = pref.getString("userid", "");
        TbUser mu = new TbUser(getApplicationContext());
        mu = mu.retrieveForUserHome(userid);

        initListData();

    }

    private void initListData() {

        //		Bundle extras = getIntent().getExtras();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        String userid = sharedPreferences.getString("userid", "");

        listUser = new TbUser(ProfilePetugasActivity.this).retrieveForUser(userid);

        ProfileMeterAdapter profileadapter = new ProfileMeterAdapter(this, listUser);
        ListView androidListView = (ListView) findViewById(R.id.list_view_profile);
        androidListView.setAdapter(profileadapter);

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

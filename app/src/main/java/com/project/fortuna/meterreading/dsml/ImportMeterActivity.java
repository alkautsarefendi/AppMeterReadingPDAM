package com.project.fortuna.meterreading.dsml;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.project.fortuna.meterreading.R;

public class ImportMeterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnLocal, btnNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_meter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Import DSML");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnLocal = (Button)findViewById(R.id.btnLocal);
        btnNetwork = (Button)findViewById(R.id.btnNetwork);

        btnLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImportMeterActivity.this, ImportDSMLFromLocalActivity.class));
            }
        });

        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImportMeterActivity.this, ImportDSMLFromNetworkActivity.class));
            }
        });
    }
}

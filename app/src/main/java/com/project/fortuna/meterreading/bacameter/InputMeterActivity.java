package com.project.fortuna.meterreading.bacameter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.project.fortuna.meterreading.R;

public class InputMeterActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button btnOneStep, btnTwoStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu Baca Meter");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*btnOneStep = (Button) findViewById(R.id.btnOneStep);*/
        btnTwoStep = (Button) findViewById(R.id.btnTwoStep);

        btnOneStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeterActivity.this, InputMeterOneStepActivity.class));
            }
        });

        btnTwoStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputMeterActivity.this, InputMeterTwoStepActivity.class));
            }
        });
    }
}

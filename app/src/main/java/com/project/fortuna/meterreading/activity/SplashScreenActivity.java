package com.project.fortuna.meterreading.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.R;

public class SplashScreenActivity extends AppCompatActivity {

    TextView tvSplash;
    private ImageView imageSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        tvSplash = (TextView) findViewById(R.id.tvSplash);
        imageSplash = (ImageView) findViewById(R.id.imageSplash);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        }, 3000L); //3000 L = 3 detik
    }
}

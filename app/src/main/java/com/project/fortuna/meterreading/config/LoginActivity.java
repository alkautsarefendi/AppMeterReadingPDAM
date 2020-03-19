package com.project.fortuna.meterreading.config;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.project.fortuna.meterreading.util.Cryptograph;
import com.project.fortuna.meterreading.DB.TbUser;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;
import com.project.fortuna.meterreading.activity.HalamanUtamaActivity;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin, btnKonfigurasi;
    protected EditText txtUser, txtPass;
    private TextView txtTitle;

    protected static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/CSV/";
    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Foto/";
    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Icon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnKonfigurasi = (Button) findViewById(R.id.btnKonfigurasi);

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtUser = (EditText) findViewById(R.id.txtUserLogin);
        txtPass = (EditText) findViewById(R.id.txtUserPassword);


        //txtTitle.setTypeface(FontCache.getTypeface("roboto.light.ttf", LoginActivity.this));

        btnKonfigurasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, KonfigurasiActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                /*startActivity(new Intent(LoginActivity.this, HalamanUtamaActivity.class));*/
            }
        });

        // init
        File dir = new File(DATA_PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + DATA_PATH + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + DATA_PATH + " on sdcard");
            }
        }


        // init2
        File dir2 = new File(PHOTO_PATH);
        if (!dir2.exists()) {
            if (!dir2.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + PHOTO_PATH + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + PHOTO_PATH + " on sdcard");
            }
        }


        // init3
        File dir3 = new File(ICON_DEFAULT);
        if (!dir3.exists()) {
            if (!dir3.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + ICON_DEFAULT + " on sdcard failed");
                return;
            } else {
                Log.v("ISA", "Created directory " + ICON_DEFAULT + " on sdcard");
            }
        }
    }

    private void login() {

        TbUser user = new TbUser(getApplicationContext());
        user = user.getUser(txtUser.getText().toString());
        System.out.println(user);
        if (user != null) {
            String decrypt = Cryptograph.getInstance().decrypt(user.getPassword());
            if (decrypt.equals(txtPass.getText().toString())) {

                //TODO Save data user ke session
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userid", user.userid);
                editor.putString("username", user.username);
                editor.putString("nmPegawai", user.nmPegawai);
                editor.putString("nmJabatan", user.nmJabatan);
                editor.putString("kdWilayah", user.kdWilayah);
                editor.putString("nmWilayah", user.nmWilayah);
                editor.apply();

                Intent menuUtama = new Intent(LoginActivity.this, HalamanUtamaActivity.class);
                startActivity(menuUtama);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            } else {
                Util.showmsg(LoginActivity.this, "Peringatan :", "Password salah");
            }
        } else {
            Util.showmsg(LoginActivity.this, "Peringatan :", "User tidak ditemukan");
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);

        alertDialog.setMessage("Keluar dari Aplikasi?");
        alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                LoginActivity.super.onBackPressed();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                startActivity(intent);
                finish();
                System.exit(0);
            }
        });

        alertDialog.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }
}

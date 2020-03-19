package com.project.fortuna.meterreading.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.fortuna.meterreading.adapter.AndroidDataAdapter;
import com.project.fortuna.meterreading.bacameter.InputMeterTwoStepActivity;
import com.project.fortuna.meterreading.util.AndroidVersion;
import com.project.fortuna.meterreading.config.KonfigurasiActivity;
import com.project.fortuna.meterreading.config.LoginActivity;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.RecyclerItemClickListener;
import com.project.fortuna.meterreading.dsml.ImportMeterActivity;
import com.project.fortuna.meterreading.export.ExportMeterActvity;
import com.project.fortuna.meterreading.review.ReviewMeterActivity;

import java.util.ArrayList;

public class HalamanUtamaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtUserHeader;
    HalamanUtamaActivity halamanUtamaActivity;
    private String TAG = HalamanUtamaActivity.class.getSimpleName();

    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private final String recyclerViewTitleText[] = {
            "PROFILE ",
            "PEMBACAAN METER",
            "DATA BACA METER",
            "MASUKKAN DSML",
            "EXPORT FILE METER",
            "CLEAN DATABASE"

    };
    private final int recyclerViewImages[] = {
            R.mipmap.user,
            R.mipmap.input_ganti_meter,
            R.mipmap.lihat_data_pelanggan,
            R.mipmap.masuk_file_spkp,
            R.mipmap.export_file,
            R.mipmap.clean_db
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_utama);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        halamanUtamaActivity = new HalamanUtamaActivity();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(HalamanUtamaActivity.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        AndroidDataAdapter mAdapter = new AndroidDataAdapter(HalamanUtamaActivity.this, av);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        switch (i) {
                            case 0:
                                Intent intent = new Intent(HalamanUtamaActivity.this, ProfilePetugasActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                break;
                            case 1:
                                Intent intent1 = new Intent(HalamanUtamaActivity.this, InputMeterTwoStepActivity.class);
                                startActivity(intent1);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                break;
                            case 2:
                                Intent intent2 = new Intent(HalamanUtamaActivity.this, ReviewMeterActivity.class);
                                startActivity(intent2);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                break;
                            case 3:
                                Intent intent3 = new Intent(HalamanUtamaActivity.this, ImportMeterActivity.class);
                                startActivity(intent3);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                break;
                            case 4:
                                Intent intent4 = new Intent(HalamanUtamaActivity.this, ExportMeterActvity.class);
                                startActivity(intent4);
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                                break;
                            case 5:
                                final Dialog dialog = new Dialog(HalamanUtamaActivity.this);

                                dialog.setContentView(R.layout.password_dialog);
                                dialog.setTitle("Form Login");

                                final EditText editTextUserName = (EditText) dialog.findViewById(R.id.txtUserLogin);
                                final EditText editTextPassword = (EditText) dialog.findViewById(R.id.txtUserPassword);
                                Button btnSignIn = (Button) dialog.findViewById(R.id.btnLogin);

                                // Set On ClickListener
                                btnSignIn.setOnClickListener(new View.OnClickListener() {

                                    public void onClick(View v) {

                                        // get The User name and Password
                                        String userName = editTextUserName.getText().toString();
                                        String password = editTextPassword.getText().toString();

                                        // fetch the Password form database for respective user name
                                        String storedPassword = "admin";
                                        String storedUsername = "admin";

                                        // check if the Stored password matches with  Password entered by user
                                        if (userName.equals(storedUsername)) {

                                            if (password.equals(storedPassword)) {
                                                Intent cd = new Intent(HalamanUtamaActivity.this, CleanDatabaseActivity.class);
                                                startActivity(cd);
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(HalamanUtamaActivity.this, "Password Salah", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(HalamanUtamaActivity.this, "Username Salah", Toast.LENGTH_LONG).show();
                                        }

                                    }
                                });


                                dialog.show();

                                /*Intent intent5 = new Intent(HalamanUtamaActivity.this, CleanDatabaseActivity.class);
                                startActivity(intent5);*/
                                break;
                        }
                    }
                })
        );


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        txtUserHeader = (TextView) navigationView.findViewById(R.id.txtUserHeader);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

        alertDialog.setMessage("Keluar dari Aplikasi?");
        alertDialog.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HalamanUtamaActivity.super.onBackPressed();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.utama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Apakah anda yakin ingin Logout?");
            alertDialog.setIcon(R.mipmap.logout);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.commit();

                    startActivity(new Intent(HalamanUtamaActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                    finish();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();


            /*finish();
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            //starting login activity
            Intent intent = new Intent(this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile_petugas) {
            Intent intent = new Intent(HalamanUtamaActivity.this, ProfilePetugasActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_input_ganti_meter) {
            Intent intent = new Intent(HalamanUtamaActivity.this, InputMeterTwoStepActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_lihat_data_pelanggan) {
            Intent intent = new Intent(HalamanUtamaActivity.this, ReviewMeterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_input_file_spkp) {
            Intent intent = new Intent(HalamanUtamaActivity.this, ImportMeterActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_export_file_meter) {
            Intent intent = new Intent(HalamanUtamaActivity.this, ExportMeterActvity.class);
            startActivity(intent);
        } else if (id == R.id.nav_pengaturan) {
            Intent intent = new Intent(HalamanUtamaActivity.this, KonfigurasiActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HalamanUtamaActivity.this);

            alertDialog.setTitle("Logout");
            alertDialog.setMessage("Apakah anda yakin ingin Logout?");
            alertDialog.setIcon(R.mipmap.logout);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.clear();
                    e.commit();

                    startActivity(new Intent(HalamanUtamaActivity.this, LoginActivity.class));
                    finish();
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            alertDialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout) findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_android_layout);
    }*/

    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(recyclerViewTitleText[i]);
            mAndroidVersion.setrecyclerViewImage(recyclerViewImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }
}

package com.project.fortuna.meterreading;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.fortuna.meterreading.activity.CleanDatabaseActivity;
import com.project.fortuna.meterreading.activity.HalamanUtamaActivity;
import com.project.fortuna.meterreading.activity.ProfilePetugasActivity;
import com.project.fortuna.meterreading.adapter.AndroidDataAdapter;
import com.project.fortuna.meterreading.bacameter.InputMeterActivity;
import com.project.fortuna.meterreading.dsml.ImportDSMLFromNetworkActivity;
import com.project.fortuna.meterreading.dsml.ImportMeterActivity;
import com.project.fortuna.meterreading.export.ExportMeterActvity;
import com.project.fortuna.meterreading.review.ReviewMeterActivity;
import com.project.fortuna.meterreading.util.AndroidVersion;
import com.project.fortuna.meterreading.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Akautsar Efendi on 3/15/2018.
 */

public class DialogCalendar extends DialogFragment /*implements View.OnClickListener*/{

    /*private TextView dialogMonthYearOnlyYear;
    private ImageButton leftClik, rightClick;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public static int year, month, constantYear;

    public String[] Listmonth = {
            "Jan ",
            "Feb",
            "Mar",
            "April",
            "Mei",
            "Juni",
            "Juli ",
            "Agus",
            "Sept",
            "Okto",
            "Nov",
            "Des"
    };

    private String bulan;

    private static final int MAX_YEAR = 2099;
    private DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NewApi")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        year = Calendar.getInstance().get(Calendar.YEAR);
        constantYear = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH) + 1;

        View dialog = inflater.inflate(R.layout.date_picker_dialog, null);

        dialogMonthYearOnlyYear = (TextView) dialog.findViewById(R.id.dialogMonthYearOnlyYear);
        dialogMonthYearOnlyYear.setText(String.valueOf(year));
        leftClik = (ImageButton) dialog.findViewById(R.id.dialogMonthYearLeftButton);
        rightClick = (ImageButton) dialog.findViewById(R.id.dialogMonthYearRightButton);

        leftClik.setOnClickListener(this);
        rightClick.setOnClickListener(this);


        mRecyclerView = (RecyclerView) dialog.findViewById(R.id.gridBulan);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        CalendarAdapter mAdapter = new CalendarAdapter(getActivity(), av);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {

                        switch (i){
                            case 0:
                                bulan = String.valueOf("01");
                                periodeTahunBulan();
                                break;
                            case 1:
                                bulan = String.valueOf("02");
                                periodeTahunBulan();
                                break;
                            case 2:
                                bulan = String.valueOf("03");
                                periodeTahunBulan();
                                break;
                            case 3:
                                bulan = String.valueOf("04");
                                periodeTahunBulan();
                                break;
                            case 4:
                                bulan = String.valueOf("05");
                                periodeTahunBulan();
                                break;
                            case 5:
                                bulan = String.valueOf("06");
                                periodeTahunBulan();
                                break;
                            case 6:
                                bulan = String.valueOf("07");
                                periodeTahunBulan();
                                break;
                            case 7:
                                bulan = String.valueOf("08");
                                periodeTahunBulan();
                                break;
                            case 8:
                                bulan = String.valueOf("09");
                                periodeTahunBulan();
                                break;
                            case 9:
                                bulan = String.valueOf("10");
                                periodeTahunBulan();
                                break;
                            case 10:
                                bulan = String.valueOf("11");
                                periodeTahunBulan();
                                break;
                            case 11:
                                bulan = String.valueOf("12");
                                periodeTahunBulan();
                                break;

                        }
                    }
                })
        );


        builder.setView(dialog);
        return builder.create();
    }

    private void periodeTahunBulan() {
        System.out.println("joni = "+bulan);
        Intent myIntent = new Intent(getActivity(), ImportDSMLFromNetworkActivity.class);
        String tahun = dialogMonthYearOnlyYear.getText().toString();
        String tahunBulan = (tahun+""+bulan);
        System.out.println("tahun joni = "+tahunBulan);
        Toast.makeText(getActivity(),tahunBulan, Toast.LENGTH_LONG).show();
        myIntent.putExtra("periodeCalendar",tahunBulan);
        startActivity(myIntent);
        getActivity().closeContextMenu();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogMonthYearLeftButton:
                if (1990 < year) {
                    --year;
                }
                dialogMonthYearOnlyYear.setText(String.valueOf(year));
                break;

            case R.id.dialogMonthYearRightButton:
                if (constantYear > year) {
                    ++year;
                }
                dialogMonthYearOnlyYear.setText(String.valueOf(year));
                break;
        }
    }

    private ArrayList<AndroidVersion> prepareData() {

        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < Listmonth.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(Listmonth[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }*/


}

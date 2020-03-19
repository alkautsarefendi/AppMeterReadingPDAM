package com.project.fortuna.meterreading;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fortuna.meterreading.adapter.AndroidDataAdapter;
import com.project.fortuna.meterreading.util.AndroidVersion;

import java.util.ArrayList;

/**
 * Created by Akautsar Efendi on 3/15/2018.
 */

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private ArrayList<AndroidVersion> arrayList;
    private Context mcontext;

    public CalendarAdapter(Context context, ArrayList<AndroidVersion> android) {
        this.arrayList = android;
        this.mcontext = context;
    }

    @Override
    public void onBindViewHolder(CalendarAdapter.ViewHolder holder, int i) {

        holder.textView.setText(arrayList.get(i).getrecyclerViewTitleText());
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.row_calendar, vGroup, false);
        return new CalendarAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View v) {
            super(v);

            textView = (TextView) v.findViewById(R.id.text);
        }
    }
}

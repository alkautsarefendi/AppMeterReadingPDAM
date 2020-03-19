package com.project.fortuna.meterreading.review;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.R;

import java.util.List;

/**
 * Created by Akautsar Efendi on 3/1/2018.
 */

public class ReviewMeterBelumSortByBlokAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<TbBacaMeter> listMeter;

    public ReviewMeterBelumSortByBlokAdapter(Activity activity, List<TbBacaMeter> listMeter) {
        this.activity = activity;
        this.listMeter = listMeter;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_blok_menu, parent, false);

        TextView titleBlok = (TextView) convertView.findViewById(R.id.titleBlok);
        TextView subtitleBlok = (TextView) convertView.findViewById(R.id.subtitleBlok);

        TbBacaMeter m = listMeter.get(position);

        titleBlok.setText(m.getNM_JALAN());
        subtitleBlok.setText("Jumlah Pelanggan : " + new TbBacaMeter(activity).retrieveForKodeBlokBelum(m.getNM_JALAN()));

        return convertView;
    }


    @Override
    public int getCount() {
        return listMeter.size();
    }


    @Override
    public Object getItem(int position) {
        return listMeter.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    public class ThumbnailClickHandler implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

        }

    }

}

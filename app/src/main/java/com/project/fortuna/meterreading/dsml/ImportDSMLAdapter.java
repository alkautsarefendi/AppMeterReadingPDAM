package com.project.fortuna.meterreading.dsml;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.R;

import java.util.List;

/**
 * Created by Akautsar Efendi on 3/1/2018.
 */

public class ImportDSMLAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<TbBacaMeter> listMeter;

    public static final String PHOTO_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Foto/";
    public static final String ICON_DEFAULT = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Icon/";

    public ImportDSMLAdapter(Activity activity, List<TbBacaMeter> listMeter) {
        this.activity = activity;
        this.listMeter = listMeter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.v("Test", "data ke " + position);

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.activity_import_dsml_adapter, parent, false);

        TextView title = convertView.findViewById(R.id.title);
        TextView rating = convertView.findViewById(R.id.rating);
        TextView genre = convertView.findViewById(R.id.genre);
        ImageView thumbnail = convertView.findViewById(R.id.thumbnail);
        thumbnail.setOnClickListener(new ThumbnailExportClickHandler());

        // getting movie data for the row
        TbBacaMeter b = listMeter.get(position);
        title.setText(b.getPELANGGAN());
        rating.setText("Kode Pel : " + b.getKD_PELANGGAN().replace("'", ""));
        genre.setText(b.getALMT_PASANG());

        if (b.FOTO == null || b.FOTO.trim().equals("")) {
            String defIcon = "tidak_ada_foto_meter.png";

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(ICON_DEFAULT + defIcon, options);
            thumbnail.setImageBitmap(bitmap);
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(PHOTO_PATH + b.FOTO, options);
            thumbnail.setImageBitmap(bitmap);
        }

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


    public class ThumbnailExportClickHandler implements android.view.View.OnClickListener {

        @Override
        public void onClick(View v) {

        }

    }

}

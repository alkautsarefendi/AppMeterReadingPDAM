package com.project.fortuna.meterreading.bacameter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.fortuna.meterreading.DB.JoniUtils;
import com.project.fortuna.meterreading.DB.TbBPMA;
import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.DB.TbBlok;
import com.project.fortuna.meterreading.DB.TbTarif;
import com.project.fortuna.meterreading.util.GPSTracker;
import com.project.fortuna.meterreading.R;
import com.project.fortuna.meterreading.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class InputMeterTwoStepActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText txtKDPelTS, txtPelangganTS, txtNoMeterTS, txtStandAwalTS, txtGolTarifTS, txtDiameterTS, txtAlamatTS, txtStandMeterTS, txtTotalPemakaianTS, txtTotalTagihanTS;
    private Button btnQRCodeTS, btnCariTS, btnMapTS, btnFotoMeterTS, btnTotalPemakaianTS, btnInputTS, btnResetTS, btnInputCancelTS;
    private Spinner spinStatusTS;
    private ImageView imageViewMeterTS;

    private String userIDTS, meterAwalTS, kodeTarifTS;

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/";

    public static final String PHOTO_PATH_TS = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Foto/";
    public static final String lang = "eng";
    private static final String TAG = "MeterReadingOCRActivity.java";
    private static final int SCANNER_QRCODE = 1;
    private static final int SCANNER_OCR = 2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    private SharedPreferences pref;
    protected String _path;
    protected boolean _taken;
    private String fileName;
    protected Uri outputFileUri = null;
    Uri output = null;

    @SuppressLint("SimpleDateFormat")
    String tglPeriode = new SimpleDateFormat("yyyyMMdd").format(new Date());

    // GPSTracker class
    GPSTracker gps;
    private double newlat = 0.0;
    private double newlong = 0.0;
    private String latitude = "";
    private String longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meter_two_step);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Meter Two Step");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        getGPSFoto();

        initComponent();
        doInitData();
        doFromReview();
    }

    @SuppressLint("LongLogTag")
    private void initComponent() {


        btnFotoMeterTS = (Button) findViewById(R.id.btnFotoMeterTS);
        btnFotoMeterTS.setOnClickListener(new ButtonFotoMeterTSClickHandler());

		/* QRCode */
        txtKDPelTS = (EditText) findViewById(R.id.txtKDPelTS);
        btnQRCodeTS = (Button) findViewById(R.id.btnQRCodeTS);
        btnQRCodeTS.setOnClickListener(new ButtonQRCodeTSClickHandler());

        btnCariTS = (Button) findViewById(R.id.btnCariTS);
        btnCariTS.setOnClickListener(new ButtonCariTSClickHandler());

        btnInputCancelTS = (Button) findViewById(R.id.btnInputCancelTS);
        btnInputCancelTS.setOnClickListener(new ButtonInputCancelTSClickHandler());

        btnInputTS = (Button) findViewById(R.id.btnInputTS);
        btnInputTS.setOnClickListener(new ButtonInputTSClickHandler());

        btnResetTS = (Button) findViewById(R.id.btnResetTS);
        btnResetTS.setOnClickListener(new ButtonResetTSClickHandler());

        txtPelangganTS = (EditText) findViewById(R.id.txtPelangganTS);
        txtNoMeterTS = (EditText) findViewById(R.id.txtNoMeterTS);
        txtStandAwalTS = (EditText) findViewById(R.id.txtStandAwalTS);
        txtAlamatTS = (EditText) findViewById(R.id.txtAlamatTS);
        txtStandMeterTS = (EditText) findViewById(R.id.txtStandMeterTS);
        //userID = (EditText) findViewById(R.id.txtUserId);
        txtTotalPemakaianTS = (EditText) findViewById(R.id.txtTotalPemakaianTS);
        txtTotalTagihanTS = (EditText) findViewById(R.id.txtTotalTagihanTS);
        //txtAwal = (EditText) findViewById(R.id.txtMeterAwal);
        txtGolTarifTS = (EditText) findViewById(R.id.txtGolTarifTS);
        //txtKodeTarif = (EditText) findViewById(R.id.txtKodeTarif);
        txtDiameterTS = (EditText) findViewById(R.id.txtDiameterTS);

        spinStatusTS = (Spinner) findViewById(R.id.spinStatusTS);
        spinStatusTS.post(new Runnable() {
            @Override
            public void run() {
                spinStatusTS.setSelection(1);
            }
        });
        spinStatusTS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    //parentView.setEnabled(true);
                    txtStandMeterTS.setText("");
                    Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Anda tidak dapat memilih status Scan OCR pada metode Baca Meter Two Step. \nSilahkan pilih status lain untuk dapat memasukkan nilai meter akhir !");
                    txtStandMeterTS.setEnabled(false);
                } else if (position == 1) {
                    //parentView.setEnabled(true);
                    txtStandMeterTS.setText("");
                    txtStandMeterTS.setEnabled(true);
                } else {
                    txtStandMeterTS.setText("0");
                    txtStandMeterTS.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> view) {

            }

        });

        btnMapTS = (Button) findViewById(R.id.btnMapTS);
        btnMapTS.setOnClickListener(new ButtonMapTSClickHandler());

        btnTotalPemakaianTS = (Button) findViewById(R.id.btnTotalPemakaianTS);
        btnTotalPemakaianTS.setOnClickListener(new ButtonTotalPakaiTSClickHandler());

		/* OCR Meter */
        imageViewMeterTS = (ImageView) findViewById(R.id.imageViewMeterTS);

        String[] paths = new String[]{PHOTO_PATH_TS, PHOTO_PATH_TS + "tessdata/"};

        for (String path : paths) {
            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.v(TAG, "ERROR: Creation of directory " + path
                            + " on sdcard failed");
                    return;
                } else {
                    Log.v(TAG, "Created directory " + path + " on sdcard");
                }
            }

        }

        // lang.traineddata file with the app (in assets folder)
        // You can get them at: http://code.google.com/p/tesseract-ocr/downloads/list
        // This area needs work and optimization
        if (!(new File(PHOTO_PATH_TS + "tessdata/" + lang + ".traineddata"))
                .exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.traineddata");
                // GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(PHOTO_PATH_TS
                        + "tessdata/eng.traineddata");

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                // while ((lenf = gin.read(buff)) > 0) {
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                // gin.close();
                out.close();

                Log.v(TAG, "Copied " + lang + " traineddata");
            } catch (IOException e) {
                Log.e(TAG,
                        "Was unable to copy " + lang + " traineddata " + e.toString());
            }
        }


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
        File dir2 = new File(PHOTO_PATH_TS);
        if (!dir2.exists()) {
            if (!dir2.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + PHOTO_PATH_TS + " on sdcard failed");
            } else {
                Log.v("ISA", "Created directory " + PHOTO_PATH_TS + " on sdcard");
            }
        }

    }

    /* Init Data */
    @SuppressWarnings("unused")
    private void doInitData() {
        String kdWilayah = pref.getString("kdWilayah", "");
        List<TbBlok> listJalan = new TbBlok(InputMeterTwoStepActivity.this).retrieveByWilayah(kdWilayah);
        txtKDPelTS.setFocusable(true);
    }

    /* From Review */
    private void doFromReview() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = extras.getString("key");
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAA " + string);
            txtKDPelTS.setText(string);
            getData(string);
        }

    }

    /* QRCode */
    private class ButtonQRCodeTSClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCANNER_QRCODE);

            } catch (ActivityNotFoundException anfe) {
                showDialog(InputMeterTwoStepActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
            }
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException ignored) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    /* Button Cari */
    public class ButtonCariTSClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getData(txtKDPelTS.getText().toString());
        }
    }


    void getData(String sKDPelanggan) {
        try {
            String userid = pref.getString("userid", "");
            TbBacaMeter ds = new TbBacaMeter(this);
            ds = ds.retrieveForMeter(userid, "'" + sKDPelanggan);

            if (ds == null) {
                Util.showmsg(this, "Peringatan :", getString(R.string.msg_no_data));
                txtKDPelTS.setEnabled(true);
                txtKDPelTS.setSelection(txtKDPelTS.getText().length());
            } else {

                txtPelangganTS.setText(ds.getPELANGGAN());
                txtNoMeterTS.setText(ds.getNO_METER());
                txtStandAwalTS.setText(ds.getSTAND_HITUNG());
                txtAlamatTS.setText(ds.getALMT_PASANG());
                txtStandMeterTS.setText(ds.getM3_AKHIR());
                userIDTS = ds.getUSER_ID();
                meterAwalTS = ds.getSTAND_HITUNG();
                txtGolTarifTS.setText(ds.getGOL_TARIF());
                kodeTarifTS = ds.getKD_TARIF();
                txtDiameterTS.setText(ds.getDIAMETER());
                spinStatusTS.setSelection(JoniUtils.getPosition(spinStatusTS.getAdapter(), "" + ds.getSTATUS_BACA()));

                latitude = ds.LATITUDE;
                longitude = ds.LONGITUDE;

                Util.popup(this, "Pelanggan : " + ds.getPELANGGAN());
                txtKDPelTS.setEnabled(false);
                Log.d("JAMBLANG " + String.valueOf(meterAwalTS), null);

            }
        } catch (Exception e) {
            Log.e("getData", "Error: " + e.toString());
        }
    }

    /* Button Meter */
    public class ButtonFotoMeterTSClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (txtPelangganTS.getText().toString().length() == 0)
                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", getString(R.string.msg_scan_pelanggan));
            else {
                Log.v("SCANNER_OCR", "Starting Camera app");
                startCameraActivity(view);
            }
        }
    }

    // Simple android photo capture: http://labs.makemachine.net/2010/03/simple-android-photo-capture/
    protected void startCameraActivity(View v) {

        fileName = composeFileFoto(txtKDPelTS.getText().toString());
        _path = PHOTO_PATH_TS + fileName;

        File file = new File(_path);
        output = Uri.fromFile(file);
        //outputFileUri = Uri.fromFile(file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        startActivityForResult(intent, SCANNER_OCR);

    }


    /* Nama Foto */
    String composeFileFoto(String sKDPelanggan) {
        return "M_" + tglPeriode + "_" + sKDPelanggan + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("SCANNER_OCR", "resultCode: " + resultCode + " On Request: " + requestCode);

        if (resultCode != RESULT_OK) {
            Util.showmsg(this, "Peringatan : ", "Gagal Mengambil Foto");
        } else {
            switch (requestCode) {
                case SCANNER_OCR:

                    outputFileUri = output;
                    onPhotoTaken1();
                    viewFoto();

                    break;

                case SCANNER_QRCODE:

                    String contents = data.getStringExtra("SCAN_RESULT");
                    txtKDPelTS.setText(contents);
                    getData(contents);

                    break;

                default:
                    break;
            }
        }
    }

    Bitmap ShrinkBitmap(String file, int width, int height) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(file, bmpFactoryOptions);

        int actualHeight = bmpFactoryOptions.outHeight;
        int actualWidth = bmpFactoryOptions.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        bmpFactoryOptions.inSampleSize = calculateInSampleSize(bmpFactoryOptions, actualWidth, actualHeight);
        bmpFactoryOptions.inJustDecodeBounds = false;
        bmpFactoryOptions.inDither = false;
        bmpFactoryOptions.inPurgeable = true;
        bmpFactoryOptions.inInputShareable = true;
        bmpFactoryOptions.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) bmpFactoryOptions.outWidth;
        float ratioY = actualHeight / (float) bmpFactoryOptions.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateTime = sdf.format(Calendar.getInstance().getTime());
        String textPhoto = getString(R.string.cr_pdam_name);
        String idPel = txtKDPelTS.getText().toString();
        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);

        Canvas canvas = new Canvas(scaledBitmap);
        Paint tPaint = new Paint();
        tPaint.setTextSize(35);
        tPaint.setAntiAlias(true);
        tPaint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        tPaint.setStyle(Paint.Style.FILL);
        tPaint.setTypeface(typeface);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        float heightNew = tPaint.measureText("yY");
        canvas.drawText(dateTime + " " + idPel, (middleX - bmp.getWidth() / 2) + 15, canvas.getHeight() - 10, tPaint);
        canvas.drawText(textPhoto, 0, 30, tPaint);

        ExifInterface exif;
        try {
            exif = new ExifInterface(file);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp;
    }

    public int calculateInSampleSize(BitmapFactory.Options bmpFactoryOptions, int reqWidth, int reqHeight) {
        final int height = bmpFactoryOptions.outHeight;
        final int width = bmpFactoryOptions.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    protected void onPhotoTaken1() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        options.inJustDecodeBounds = true;

        Bitmap bitmap = ShrinkBitmap(_path, 1280, 720);
        imageViewMeterTS.setImageBitmap(bitmap);

    }


    public void viewFoto() {

        imageViewMeterTS.setImageURI(outputFileUri);

    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(InputMeterOneStepActivity.PHOTO_TAKEN, _taken);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(InputMeterOneStepActivity.PHOTO_TAKEN)) {
            onPhotoTaken1();
        }
    }


    public void getGPSFoto() {
        // create class object
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            newlat = gps.getLatitude();
            newlong = gps.getLongitude();

            Toast.makeText(InputMeterTwoStepActivity.this, "Your Location is - \nLat: " + newlat + "\nLong: " + newlong, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
    }


    /* Button Cancel */
    public class ButtonInputCancelTSClickHandler implements View.OnClickListener {

        public void onClick(View view) {

            cancelDialog();

        }
    }


    private void cancelDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan : ");
        builder.setMessage("Batal Melakukan Input Data ? ").setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int id) {

                finish();

            }
        });

        builder.setNegativeButton("Tidak", null).show();

    }


    /* Button Reset */
    public class ButtonResetTSClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            clearText();
            txtKDPelTS.setEnabled(true);
            txtKDPelTS.requestFocus();

        }
    }

    /* Clear All Text */
    private void clearText() {
        txtKDPelTS.setText("");
        txtPelangganTS.setText("");
        txtNoMeterTS.setText("");
        txtStandAwalTS.setText("");
        txtAlamatTS.setText("");
        txtStandMeterTS.setText("");
        txtGolTarifTS.setText("");
        txtTotalPemakaianTS.setText("");
        txtTotalTagihanTS.setText("");
        txtDiameterTS.setText("");
        spinStatusTS.setSelection(1);
        outputFileUri = null;
        imageViewMeterTS.setImageResource(R.mipmap.tidak_ada_foto_meter);
    }


    /* Button Map */
    public class ButtonMapTSClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent maps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=" + latitude + "," + longitude));
            startActivity(maps);

        }

    }


    /* Button Input */
    public class ButtonInputTSClickHandler implements View.OnClickListener {

        @SuppressLint("SimpleDateFormat")
        public void onClick(View view) {

            String StatusBaca = spinStatusTS.getSelectedItem().toString();
            String tokenStatusBaca = StatusBaca.substring(4);

            if (txtPelangganTS.getText().length() == 0) {
                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Data Pelanggan Belum Ada");
            } else if (txtStandMeterTS.getText().toString().trim().equals("")) {
                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Bacaan Meter Tidak Boleh Kosong");
            } else if (outputFileUri == null) {
                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Foto Water Meter Tidak Boleh Kosong");
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(InputMeterTwoStepActivity.this);
                builder.setTitle(getString(R.string.action_input))
                        .setMessage("Kode Pelanggan  = \t "
                                + txtKDPelTS.getText().toString() + "\n"
                                + "Pelanggan \t\t\t\t = \t " + txtPelangganTS.getText().toString() + "\n"
                                + "Alamat \t\t\t\t\t = \t " + txtAlamatTS.getText().toString() + "\n"
                                + "No Meter \t\t\t\t = \t " + txtNoMeterTS.getText().toString() + "\n"
                                + "Meter Awal \t\t\t\t = \t " + txtStandAwalTS.getText().toString() + "\n"
                                + "Meter Akhir \t\t\t = \t " + txtStandMeterTS.getText().toString() + "\n"
                                + "New Lat \t\t\t\t\t = \t " + newlat + "\n"
                                + "New Long \t\t\t\t = \t " + newlong + "\n"
                                + "Kubikasi \t\t\t\t\t = \t " + txtTotalPemakaianTS.getText().toString() + "\n"
                                + "Total Tagihan \t\t = \t " + txtTotalTagihanTS.getText().toString() + "\n"
                                + "Tgl Baca \t\t\t\t\t = \t " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n"
                                + "Status Baca \t\t\t = \t " + tokenStatusBaca)
                        .setCancelable(false)
                        .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                saveData();
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.show();
            }
        }
    }

    /*
     * AGS 03072013
     */
    @SuppressLint("SimpleDateFormat")
    void saveData() {

        String mesgErr = "";

        if (mesgErr.equals("")) {

            TbBacaMeter meter = new TbBacaMeter(getApplicationContext());
            meter.KD_PELANGGAN = "'" + txtKDPelTS.getText().toString();
            meter.PELANGGAN = txtPelangganTS.getText().toString();
            meter.NO_METER = txtNoMeterTS.getText().toString();
            meter.M3_AWAL = txtStandAwalTS.getText().toString();
            meter.ALMT_PASANG = txtAlamatTS.getText().toString();
            meter.M3_AKHIR = txtStandMeterTS.getText().toString();
            meter.STATUS_BACA = spinStatusTS.getSelectedItem().toString().split(" = ")[0].trim();
            meter.FOTO = fileName;
            meter.LATITUDE = "" + newlat;
            meter.LONGITUDE = "" + newlong;
            meter.GPS_LAT = "" + newlat;
            meter.GPS_LONG = "" + newlong;
            meter.USER_ID = pref.getString("userid", "");
            meter.S_KIRIM = "0";
            meter.TGL_WAKTU_BACA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            meter.KD_WILAYAH = meter.getKD_WILAYAH();
            meter.PERIODE = meter.getPERIODE();
            meter.PREDIKSI_TAGIHAN = txtTotalTagihanTS.getText().toString().replace("Rp. ", "");
            meter.KUBIKASI = txtTotalPemakaianTS.getText().toString().replace(" M3", "");

            meter.update();

            AlertDialog.Builder builder = new AlertDialog.Builder(InputMeterTwoStepActivity.this);
            builder.setTitle(getString(R.string.action_input))
                    .setMessage("Input Data Sukses")
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            clearText();
                            txtKDPelTS.setEnabled(true);
                            txtKDPelTS.requestFocus();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        }
    }


    /* Button Total Pemakaian */
    public class ButtonTotalPakaiTSClickHandler implements View.OnClickListener {


        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {

            if (txtStandMeterTS.getText().toString().equals("")) {

                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan : ", "Angka Stand Meter Kosong");

            } else if (txtPelangganTS.getText().toString().equals("")) {

                Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan : ", "Data Pelanggan Belum Ada");

            } else {

                int awal = Integer.parseInt(meterAwalTS);
                int akhir = Integer.parseInt(txtStandMeterTS.getText().toString());
                String totalPakai = "" + (akhir - awal) + " M3";

                float total = 0;
                float biayaDiameter = 0;
                float biayaAdmin = 0;
                int pemakaianM3 = Integer.parseInt(String.valueOf(totalPakai).replace(" M3", ""));
                List<TbBPMA> bpmaDetails = getDetilBPMA(getApplicationContext(), txtDiameterTS.getText().toString());
                List<TbTarif> tarifDetails = getDetil(getApplicationContext(), kodeTarifTS);

                int temppemakaianM3 = 0;
                for (int i = 0; i < tarifDetails.size(); i++) {
                    TbTarif tarif = tarifDetails.get(i);
                    biayaAdmin = Float.parseFloat(tarif.getBiayaAdmin());
                    if (pemakaianM3 == 0) {
                        total += 5 * (Float.parseFloat(tarif.getHargaM3()));
                        break;
                    } else if (pemakaianM3 <= (Integer.parseInt(tarif.getMaxM3()) - temppemakaianM3) && pemakaianM3 > 0) {
                        total += pemakaianM3 * Float.parseFloat(tarif.getHargaM3());
                        break;
                    } else if (pemakaianM3 > (Integer.parseInt(tarif.getMaxM3()) - temppemakaianM3)) {
                        total += (Integer.parseInt(tarif.getMaxM3()) - temppemakaianM3) * Float.parseFloat(tarif.getHargaM3());
                        pemakaianM3 -= (Integer.parseInt(tarif.getMaxM3()) - temppemakaianM3);
                        temppemakaianM3 = Integer.parseInt(tarif.getMaxM3());
                    }
                }

                for (int u = 0; u < bpmaDetails.size(); u++) {
                    TbBPMA mbpma = bpmaDetails.get(u);
                    biayaDiameter = Float.parseFloat(mbpma.getBiaya());
                }

                txtTotalPemakaianTS.setText(totalPakai);
                float tt = total + biayaDiameter + biayaAdmin;
                double b = Double.parseDouble(String.valueOf(tt));
                NumberFormat formatter = new DecimalFormat("#0.00");
                String cc = formatter.format(b);
                System.out.println(cc);
                txtTotalTagihanTS.setText("Rp. " + String.valueOf(cc).replace(",00", "").replace(".00", ""));

            }
        }
    }


    private static List<TbTarif> getDetil(Context Context, String kodeTarif) {

        List<TbTarif> tarifDetails = new ArrayList<TbTarif>();
        TbTarif dto = new TbTarif(Context);
        tarifDetails = dto.retrieveForTagihan(kodeTarif);

        return tarifDetails;
    }


    private static List<TbBPMA> getDetilBPMA(Context Context, String diameter) {

        List<TbBPMA> bpmaDetails = new ArrayList<TbBPMA>();
        TbBPMA mbpma = new TbBPMA(Context);
        bpmaDetails = mbpma.retrieveForBPMA(diameter);

        return bpmaDetails;
    }


    /* Key Back Pressed */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (txtPelangganTS.getText().length() != 0) {
                dialogOnBackPress();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void dialogOnBackPress() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Peringatan : ");
        builder.setMessage("Apakah Anda Ingin Menyimpan Data Inputan ? ").setCancelable(false);
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ButtonSaveClickHandler();

            }
        });

        builder.setNeutralButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton("Kembali", null).show();
    }


    /* Button Save */
    @SuppressLint("SimpleDateFormat")
    protected void ButtonSaveClickHandler() {

        String StatusBaca = spinStatusTS.getSelectedItem().toString();
        String tokenStatusBaca = StatusBaca.substring(4);

        if (txtPelangganTS.getText().toString().trim().equals("")) {
            Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Data Pelanggan Belum Ada");
        } else if (txtStandMeterTS.getText().toString().trim().equals("")) {
            Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Bacaan Meter tidak boleh kosong");
        } else if (outputFileUri == null) {
            Util.showmsg(InputMeterTwoStepActivity.this, "Peringatan :", "Foto Meteran tidak boleh kosong");
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(InputMeterTwoStepActivity.this);
            builder.setTitle(getString(R.string.action_input))
                    .setMessage("Kode Pelanggan  = \t "
                            + txtKDPelTS.getText().toString() + "\n"
                            + "Pelanggan \t\t\t\t = \t " + txtPelangganTS.getText().toString() + "\n"
                            + "Alamat \t\t\t\t\t = \t " + txtAlamatTS.getText().toString() + "\n"
                            + "No Meter \t\t\t\t = \t " + txtNoMeterTS.getText().toString() + "\n"
                            + "Meter Awal \t\t\t\t = \t " + txtStandAwalTS.getText().toString() + "\n"
                            + "Meter Akhir \t\t\t = \t " + txtStandMeterTS.getText().toString() + "\n"
                            + "Total Pakai \t\t\t = \t " + txtTotalPemakaianTS.getText().toString() + "\n"
                            + "Total Tagihan \t\t = \t " + txtTotalTagihanTS.getText().toString() + "\n"
                            + "New Lat \t\t\t\t\t = \t " + newlat + "\n"
                            + "New Long \t\t\t\t = \t " + newlong + "\n"
                            + "Tgl Baca \t\t\t\t\t = \t " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "\n"
                            + "Status Baca \t\t\t = \t " + tokenStatusBaca)
                    .setCancelable(false)
                    .setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saveData();
                        }
                    })
                    .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if (txtPelangganTS.getText().length() != 0) {
                    dialogOnBackPress();
                } else {
                    this.finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
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

public class InputMeterOneStepActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText txtKDPelOS, txtPelangganOS, txtAlamatOS, txtStandMeterOS;
    private Button btnQRCodeOS, btnCariOS, btnMapOS, btnFotoMeterOS, btnLanjutOS, btnResetOS, btnInputCancelOS;
    private ImageView imageViewPhotoOS;
    private ScrollView inputScrollViewOS;
    private String noMeterOS, userIDOS, meterAwalOS, golTarifOS, statusOS, namaFile, kodeTarifOS, diameterOS, totalPakaiOS, totalTagihanOS;
    private String StatusBaca, tokenStatusBaca;

    public static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/MeterReading/";
    public static final String PHOTO_PATH_OS = Environment.getExternalStorageDirectory().toString() + "/MeterReading/Foto/";
    public static final String lang = "eng";
    protected static final String PHOTO_TAKEN = "photo_taken";
    private static final String TAG = "InputMeterOneStepActivity.java";
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

    public static final String OneStepPreferences = "OneStepPreferencesConfirm";
    public static final String KodePel = "kodePelKey";
    public static final String NamaPel = "namaPelKey";
    public static final String NomorMeter = "nomorMeterKey";
    public static final String GolTarif = "golTarifKey";
    public static final String Diameter = "diameterKey";
    public static final String Alamat = "alamatKey";
    public static final String Status = "statusKey";
    public static final String KodeStatus = "kodeStatusKey";
    public static final String NamaFile = "namaFileKey";
    public static final String StandMeter = "standMeterKey";
    public static final String TotalPakai = "totalPakaiKey";
    public static final String TotalTagihan = "totalTagihanKey";
    public static final String Latitude = "latitudeKey";
    public static final String Longitude = "longitudeKey";
    public static final String NewLat = "newLatKey";
    public static final String NewLong = "newLongKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_meter_one_step);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Input Meter One Step");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        pref = getApplicationContext().getSharedPreferences("MeterReadingApp", 0);
        sharedpreferences = getSharedPreferences(OneStepPreferences, Context.MODE_PRIVATE);

        getGPSFoto();

        initComponent();
        doInitData();
    }

    @SuppressLint("LongLogTag")
    private void initComponent() {

        inputScrollViewOS = (ScrollView) findViewById(R.id.inputScrollViewOS);

        btnFotoMeterOS = (Button) findViewById(R.id.btnFotoMeterOS);
        btnFotoMeterOS.setOnClickListener(new ButtonMeterClickHandler());

        txtKDPelOS = (EditText) findViewById(R.id.txtKDPelOS);
        btnQRCodeOS = (Button) findViewById(R.id.btnQRCodeOS);
        btnQRCodeOS.setOnClickListener(new ButtonQRCodeClickHandler());

        btnCariOS = (Button) findViewById(R.id.btnCariOS);
        btnCariOS.setOnClickListener(new ButtonCariClickHandler());

        btnInputCancelOS = (Button) findViewById(R.id.btnInputCancelOS);
        btnInputCancelOS.setOnClickListener(new ButtonCancelClickHandler());

        btnLanjutOS = (Button) findViewById(R.id.btnLanjutOS);
        btnLanjutOS.setOnClickListener(new ButtonLanjutClickHandler());

        btnResetOS = (Button) findViewById(R.id.btnResetOS);
        btnResetOS.setOnClickListener(new ButtonResetClickHandler());

        txtPelangganOS = (EditText) findViewById(R.id.txtPelangganOS);
        txtAlamatOS = (EditText) findViewById(R.id.txtAlamatOS);
        txtStandMeterOS = (EditText) findViewById(R.id.txtStandMeterOS);

        btnMapOS = (Button) findViewById(R.id.btnMapOS);
        btnMapOS.setOnClickListener(new ButtonMapClickHandler());

		/* OCR Meter */
        imageViewPhotoOS = (ImageView) findViewById(R.id.imageViewPhotoOS);

        String[] paths = new String[]{PHOTO_PATH_OS, PHOTO_PATH_OS + "tessdata/"};

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
        if (!(new File(PHOTO_PATH_OS + "tessdata/" + lang + ".traineddata"))
                .exists()) {
            try {

                AssetManager assetManager = getAssets();
                InputStream in = assetManager.open("tessdata/eng.traineddata");
                // GZIPInputStream gin = new GZIPInputStream(in);
                OutputStream out = new FileOutputStream(PHOTO_PATH_OS
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
        File dir2 = new File(PHOTO_PATH_OS);
        if (!dir2.exists()) {
            if (!dir2.mkdirs()) {
                Log.v("ISA", "ERROR: Creation of directory " + PHOTO_PATH_OS + " on sdcard failed");
            } else {
                Log.v("ISA", "Created directory " + PHOTO_PATH_OS + " on sdcard");
            }
        }

    }


    /* Init Data */
    @SuppressWarnings("unused")
    private void doInitData() {
        String kdWilayah = pref.getString("kdWilayah", "");
        List<TbBlok> listJalan = new TbBlok(InputMeterOneStepActivity.this).retrieveByWilayah(kdWilayah);
        txtKDPelOS.setFocusable(true);
    }


    /* QRCode */
    private class ButtonQRCodeClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new Intent(ACTION_SCAN);
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCANNER_QRCODE);

            } catch (ActivityNotFoundException anfe) {
                showDialog(InputMeterOneStepActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
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
    public class ButtonCariClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            getData(txtKDPelOS.getText().toString());
        }
    }


    void getData(String sKDPelanggan) {
        try {
            String userid = pref.getString("userid", "");
            TbBacaMeter ds = new TbBacaMeter(this);
            ds = ds.retrieveForMeter(userid, "'" + sKDPelanggan);

            if (ds == null) {
                Util.showmsg(this, "Peringatan :", getString(R.string.msg_no_data));
                txtKDPelOS.setEnabled(true);
                txtKDPelOS.setSelection(txtKDPelOS.getText().length());
            } else {

                txtPelangganOS.setText(ds.getPELANGGAN());
                noMeterOS = ds.getNO_METER();
                txtAlamatOS.setText(ds.getALMT_PASANG());
                txtStandMeterOS.setText(ds.getM3_AKHIR());
                userIDOS = ds.getUSER_ID();
                meterAwalOS = ds.getM3_AWAL();
                golTarifOS = ds.getGOL_TARIF();
                kodeTarifOS = ds.getKD_TARIF();
                diameterOS = ds.getDIAMETER();
                statusOS = ds.getSTATUS_BACA();

                latitude = ds.LATITUDE;
                longitude = ds.LONGITUDE;

                Util.popup(this, "Pelanggan : " + ds.getPELANGGAN());
                txtKDPelOS.setEnabled(false);

            }
        } catch (Exception e) {
            Log.e("getData", "Error: " + e.toString());
        }
    }


    /* Button Meter */
    public class ButtonMeterClickHandler implements View.OnClickListener {
        public void onClick(View view) {
            if (txtPelangganOS.getText().toString().length() == 0)
                Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", getString(R.string.msg_scan_pelanggan));
            else {
                Log.v("SCANNER_OCR", "Starting Camera app");
                txtStandMeterOS.setText("");
                startCameraActivity(view);
            }
        }
    }


    // Simple android photo capture: http://labs.makemachine.net/2010/03/simple-android-photo-capture/
    protected void startCameraActivity(View v) {

        fileName = composeFileFoto(txtKDPelOS.getText().toString());
        _path = PHOTO_PATH_OS + fileName;

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

                    // 1. Take Photo
                    // 2. View Photo
                    // 3. Get GPS Photo
                    onPhotoTaken();
                    viewFoto();
                    break;

                case SCANNER_QRCODE:
                    String contents = data.getStringExtra("SCAN_RESULT");
                    txtKDPelOS.setText(contents);
                    getData(contents);
                    break;

                default:
                    break;
            }
        }
    }


    protected void onPhotoTaken() {
        _taken = true;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;

        options.inJustDecodeBounds = true;

        Bitmap bitmap = ShrinkBitmap(_path, 1280, 720);
        imageViewPhotoOS.setImageBitmap(bitmap);

        Log.v("SCANNER_OCR", "Before baseApi");

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(PHOTO_PATH_OS, lang);
        baseApi.setVariable("tessedit_char_whitelist", "0123456789");
        baseApi.setImage(bitmap);

        String recognizedText = baseApi.getUTF8Text();

        baseApi.end();

        // You now have the text in recognizedText var, you can do anything with it.
        // We will display a stripped out trimmed alpha-numeric version of it (if lang is eng)
        // so that garbage doesn't make it to the display.

        Log.v("SCANNER_OCR", "OCRED TEXT: " + recognizedText);

        // clean up and show
        if (lang.equalsIgnoreCase("eng")) {

            recognizedText = recognizedText.replaceAll("[^0-9]+", ""); // number

        }

        recognizedText = recognizedText.trim();

        if (recognizedText.length() != 0) {

            String angkaMeter = txtStandMeterOS.getText().toString().replace("\\r\\n|\\r|\\n|", "").length() == 0 ? recognizedText : txtStandMeterOS.getText() + " " + recognizedText;
            txtStandMeterOS.setText(angkaMeter.replace(" ", ""));
            txtStandMeterOS.setSelection(txtStandMeterOS.getText().toString().length());


        }

        Util.popup(this, txtStandMeterOS.getText().toString().replace(" ", ""));
        // Cycle done.

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
        String idPel = txtKDPelOS.getText().toString();
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
            System.out.println("Simple Size " + inSampleSize);
        }
        final float totalPixels = width * height;
        System.out.println("Total Pixels " +totalPixels);
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        System.out.println("Total req pix " +totalReqPixelsCap);

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    public void viewFoto() {

        imageViewPhotoOS.setImageURI(outputFileUri);

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(InputMeterOneStepActivity.PHOTO_TAKEN, _taken);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        Log.i(TAG, "onRestoreInstanceState()");
        if (savedInstanceState.getBoolean(InputMeterOneStepActivity.PHOTO_TAKEN)) {
            onPhotoTaken();
        }
    }


    public void getGPSFoto() {
        // create class object
        gps = new GPSTracker(this);

        // check if GPS enabled
        if (gps.canGetLocation()) {
            newlat = gps.getLatitude();
            newlong = gps.getLongitude();

            Toast.makeText(InputMeterOneStepActivity.this, "Your Location is - \nLat: " + newlat + "\nLong: " + newlong, Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingsAlert();
        }
    }


    /* Button Cancel */
    public class ButtonCancelClickHandler implements View.OnClickListener {

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

                InputMeterOneStepActivity.this.finish();

            }
        });

        builder.setNegativeButton("Tidak", null).show();

    }


    /* Button Reset */
    public class ButtonResetClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            clearText();
            txtKDPelOS.setEnabled(true);
            txtKDPelOS.requestFocus();
            inputScrollViewOS.scrollTo(0, 0);
            inputScrollViewOS.fullScroll(ScrollView.FOCUS_UP);

        }
    }

    /* Clear All Text */
    private void clearText() {
        txtKDPelOS.setText("");
        txtPelangganOS.setText("");
        txtAlamatOS.setText("");
        txtStandMeterOS.setText("");
        outputFileUri = null;
        imageViewPhotoOS.setImageResource(R.mipmap.tidak_ada_foto_meter);
    }


    /* Button Map */
    public class ButtonMapClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Intent maps = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?f=d&daddr=" + latitude + "," + longitude));
            startActivity(maps);

        }

    }


    /* Button Input */
    public class ButtonLanjutClickHandler implements View.OnClickListener {

        @SuppressLint("SimpleDateFormat")
        public void onClick(View view) {

            /*StatusBaca = getString(R.string.status_one_step);
            tokenStatusBaca = StatusBaca.substring(4);*/

            if (txtPelangganOS.getText().length() == 0) {
                Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", "Data Pelanggan Belum Ada");
            } else if (txtStandMeterOS.getText().toString().trim().equals("")) {
                Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", "Bacaan Meter Tidak Boleh Kosong");
            } else if (outputFileUri == null) {
                Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", "Foto Water Meter Tidak Boleh Kosong");
            } else {
                lanjutSimpan();
            }
        }
    }

    private void lanjutSimpan() {

        totalPemakaian();

        StatusBaca = getString(R.string.status_one_step);
        tokenStatusBaca = StatusBaca.substring(4);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KodePel, txtKDPelOS.getText().toString());
        editor.putString(NamaPel, txtPelangganOS.getText().toString());
        editor.putString(NomorMeter, noMeterOS);
        editor.putString(GolTarif, golTarifOS);
        editor.putString(Diameter, diameterOS);
        editor.putString(Alamat, txtAlamatOS.getText().toString());
        editor.putString(Status, tokenStatusBaca);
        editor.putString(KodeStatus, "0");
        editor.putString(NamaFile, fileName);
        editor.putString(StandMeter, txtStandMeterOS.getText().toString());
        editor.putString(TotalPakai, totalPakaiOS);
        editor.putString(TotalTagihan, totalTagihanOS);
        editor.putString(Latitude, "" + latitude);
        editor.putString(Longitude, "" + longitude);
        editor.putString(NewLat, "" + newlat);
        editor.putString(NewLong, "" + newlong);
        editor.apply();

        Intent osConfirm = new Intent(getApplicationContext(), InputMeterOneStepKonfirmasiActivity.class);
        startActivity(osConfirm);

    }

    private void totalPemakaian() {

        int awal = Integer.parseInt(meterAwalOS);
        int akhir = Integer.parseInt(txtStandMeterOS.getText().toString());
        String totalPakai = "" + (akhir - awal) + " M3";

        float total = 0;
        float biayaDiameter = 0;
        float biayaAdmin = 0;
        int pemakaianM3 = Integer.parseInt(String.valueOf(totalPakai).replace(" M3", ""));
        List<TbBPMA> bpmaDetails = getDetilBPMA(getApplicationContext(), diameterOS);
        List<TbTarif> tarifDetails = getDetail(getApplicationContext(), kodeTarifOS);

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

        totalPakaiOS = totalPakai;
        float tt = total + biayaDiameter + biayaAdmin;
        double b = Double.parseDouble(String.valueOf(tt));
        NumberFormat formatter = new DecimalFormat("#0.00");
        String cc = formatter.format(b);
        System.out.println(cc);
        totalTagihanOS = "Rp. " + String.valueOf(cc).replace(",00", "").replace(".00", "");

    }

    private static List<TbTarif> getDetail(Context Context, String kodeTarif) {

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
            if (txtPelangganOS.getText().length() != 0) {
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
        builder.setMessage("Apakah Anda Akan Melanjutkan Pembacaan Data ? ").setCancelable(false);
        builder.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (txtStandMeterOS.getText().toString().trim().equals("")) {
                    Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", "Bacaan Meter Tidak Boleh Kosong");
                } else if (outputFileUri == null) {
                    Util.showmsg(InputMeterOneStepActivity.this, "Peringatan :", "Foto Water Meter Tidak Boleh Kosong");
                } else {
                    lanjutSimpan();
                }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                if (txtPelangganOS.getText().length() != 0) {
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

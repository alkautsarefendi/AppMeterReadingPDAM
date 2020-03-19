package com.project.fortuna.meterreading.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.project.fortuna.meterreading.DB.BaseDAO;
import com.project.fortuna.meterreading.DB.TbBacaMeter;
import com.project.fortuna.meterreading.bacameter.InputMeterTwoStepActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Akautsar Efendi on 2/28/2018.
 */

public class ConnectivityWS {

    public static JSONObject postBacaMeter(BaseDAO domain, String url) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        TbBacaMeter meter = (TbBacaMeter) domain;
        nameValuePairs.add(new BasicNameValuePair("USERID", meter.getUSER_ID()));
        nameValuePairs.add(new BasicNameValuePair("CUSTID", meter.getKD_PELANGGAN().replace("'", "")));
        nameValuePairs.add(new BasicNameValuePair("M3AWAL", meter.getM3_AWAL() + ""));
        nameValuePairs.add(new BasicNameValuePair("M3AKHIR", meter.getM3_AKHIR()));
        nameValuePairs.add(new BasicNameValuePair("TGL_BACA", meter.getTGL_WAKTU_BACA()));
        nameValuePairs.add(new BasicNameValuePair("STATUS", meter.getSTATUS_BACA() + ""));
        nameValuePairs.add(new BasicNameValuePair("GPS_LAT", meter.getGPS_LAT()));
        nameValuePairs.add(new BasicNameValuePair("GPS_LONG", meter.getGPS_LONG()));
        nameValuePairs.add(new BasicNameValuePair("PHOTO_METERAN", meter.getFOTO()));

        String pathName = InputMeterTwoStepActivity.PHOTO_PATH_TS + meter.getFOTO();


        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options );

        String encodedString = Base64.encodeToString(Util.convertBitmapToByteArray(bitmap),Base64.DEFAULT);
        String safeString = encodedString.replace('+','-').replace('/','_');

        nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", safeString));*/


        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        String encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);

        nameValuePairs.add(new BasicNameValuePair("PHOTO_FILE", encodedString));
        System.out.print("culo = " + encodedString);

        /*Generate Params from Domain*/
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (field.get(domain) != null) {
                    nameValuePairs.add(
                            new BasicNameValuePair(field.getName().toUpperCase(),
                                    String.valueOf(field.get(domain))
                            )
                    );
                }
            } catch (Exception e) {
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }

        if (url.contains("?")) {
            String dataParams = url.split("\\?")[1];
            String[] params = dataParams.split("&");
            for (String param : params) {
                if (!param.equals("USERID")) {
                    String[] data = param.split("=");
                    nameValuePairs.add(new BasicNameValuePair(data[0], data[1]));
                }
            }
        }
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpContext localContext = new BasicHttpContext();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost, localContext);

            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return new JSONObject("{CODE:99,MESSAGE:" + e.getMessage() + "}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }


    public static JSONObject checkServerAvailability(String url) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject postToServer(BaseDAO domain, String url) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        /*Generate Params from Domain*/
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (field.get(domain) != null) {
                    nameValuePairs.add(
                            new BasicNameValuePair(field.getName().toUpperCase(),
                                    String.valueOf(field.get(domain))
                            )
                    );
                }
            } catch (Exception e) {
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }

        if (url.contains("?")) {
            String dataParams = url.split("\\?")[1];
            Log.v("aa", dataParams);
            String[] params = dataParams.split("&");
            for (String param : params) {
                String[] data = param.split("=");
                nameValuePairs.add(new BasicNameValuePair(data[0], data[1]));
            }
        }
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpContext localContext = new BasicHttpContext();
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost, localContext);

            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            return new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            try {
                return new JSONObject("{CODE:99,MESSAGE:" + e.getMessage() + "}");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject getFromServer(String url, BaseDAO domain) {
        /*Generate Params from Domain*/
        String params = "";
        Field[] declaredFields = domain.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                if (field.get(domain) != null) {
                    params = params + field.getName() + "=" + field.get(domain) + "&";
                }
            } catch (Exception e) {
                Log.v("", e.getMessage());
                e.printStackTrace();
            }
        }
        params = params.substring(0, params.length() - 1);

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI(url);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            return new JSONObject(result.toString());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

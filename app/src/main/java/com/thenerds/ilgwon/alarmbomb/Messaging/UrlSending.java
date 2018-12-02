package com.thenerds.ilgwon.alarmbomb.Messaging;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class UrlSending extends AsyncTask<InputStream, Void, Void> {
    //    String s;
    String body;

    public UrlSending(String body) {
        //        this.s = s;
        this.body = body;
        //Log.i("X","here");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(InputStream... inputstreams) {
        Log.i("HERE", "HERE2");
        URL url = null;
        try {
            url = new URL("https://fcm.googleapis.com/v1/projects/alarmbomb-5fbe8/messages:send");
            //url=new URL("https://fcm.googleapis.com/fcm/send");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpsURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Accecpt", "application/json");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        try {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + AccessToken(inputstreams));
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.setDoOutput(true);
        Log.i("XXXX", "here");
        OutputStream os = null;
        try {
            os = httpURLConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("XXXX", body);
        try {
            os.write(body.getBytes("utf-8"));
            Log.i("Sending", "success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (httpURLConnection.getResponseCode() == 200) {
                StringBuffer responseBody = new StringBuffer();
                BufferedReader br = new BufferedReader(new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF8")));
                String line;
                while ((line = br.readLine()) != null) {
                    responseBody.append(line);
                    Log.i("messageSent", line);
                }
                br.close();
            } else {
                Log.i("messageSent", "error ??");
            }
            httpURLConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String AccessToken(InputStream[] streams) throws IOException {

        //{
        //File token_dir = new File(getFilesDir() + "/sample");
        //File token_file = new File(token_dir.getAbsolutePath() + "/someimage.png");
        //InputStream inputStream = Context.getResources().openRawResource(R.raw.service_account);
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(streams[0])
                // "com/example/ilgwon/alarmbomb/Messaging/alarmbomb-5fbe8-firebase-adminsdk-3tmaz-aa67b3d00b.json"
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredential.refreshToken();
        Log.i("Token123123", String.valueOf(googleCredential.getAccessToken()));
        return googleCredential.getAccessToken();
    }
}
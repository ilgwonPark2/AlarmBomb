package com.thenerds.ilgwon.alarmbomb.Messaging;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class UrlSending extends AsyncTask<InputStream, Void, Void> {
    String s;
    public UrlSending(String s) {
        this.s = s;
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
        //Log.i("HERE","HERE1");
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
        //httpURLConnection.addRequestProperty("Authorization","key=AAAAgkF4FXI:APA91bEemOL50TAv0UPRqI1Lq5kIbwads1oJvVYXFh17DNz1YOda8kgkdn-dEuNnT6vbhM3_JrLiL7Nf5LrztMIb9WvzVtp4LUkq9R-Bc5PT1xdxMBX1YUSso0hyKLCEXRxatnnRNXVL");
        try {
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + AccessToken(inputstreams));
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.setDoOutput(true);
        Log.i("XXXX", "here");
        //StringBuffer responseBody=new StringBuffer();
        OutputStream os = null;
        try {
            os = httpURLConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        JSONObject root = new JSONObject();
        //        JSONObject notification = new JSONObject();
        //        try {
        //            notification.put("body", "hello");
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
        //        try {
        //            notification.put("title", "invitation");
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
        //        try {
        //            root.put("notification", notification);
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
        //        try {
        //            root.put("to", s);
        //        } catch (JSONException e) {
        //            e.printStackTrace();
        //        }
        String body = "{\"message\" : \n\t " +
                "{ \"token\" : \"" + s + "\",\n\t" +
                "\"data\": {\n\t\t" +
                "\"title\": \"" + "invitation" + "\",\n\t\t" +
                "\"body\": " + "\"Hyein\"\n\t\t}" + "\n\t}" + "\n}";
        Log.i("XXXX", String.valueOf(body));
        try {
            os.write(body.toString().getBytes("utf-8"));
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String AccessToken(InputStream[] streams) throws IOException {
        Path current = Paths.get("");
        String ssss = current.toAbsolutePath().toString();
        Log.i("patj", ssss);
        Log.i("Hello", "dfsf");
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
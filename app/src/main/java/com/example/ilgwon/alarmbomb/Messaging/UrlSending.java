package com.example.ilgwon.alarmbomb.Messaging;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class UrlSending extends AsyncTask<Void,Void,Void> {
    String s;

    public UrlSending(String s) {
        this.s=s;
        //Log.i("X","here");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected Void doInBackground(Void... voids) {

        Log.i("HERE","HERE2");
        URL url= null;
        try {
            url = new URL("https://fcm.googleapis.com/v1/projects/alarmbomb-5fbe8/messages:send");
            //url=new URL("https://fcm.googleapis.com/fcm/send");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //Log.i("HERE","HERE1");
        HttpsURLConnection httpURLConnection= null;
        try {
            httpURLConnection = (HttpsURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpURLConnection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type","application/json; UTF-8");
        //httpURLConnection.setRequestProperty("Authorization","key=AAAAgkF4FXI:APA91bEemOL50TAv0UPRqI1Lq5kIbwads1oJvVYXFh17DNz1YOda8kgkdn-dEuNnT6vbhM3_JrLiL7Nf5LrztMIb9WvzVtp4LUkq9R-Bc5PT1xdxMBX1YUSso0hyKLCEXRxatnnRNXVL");
        try {
            httpURLConnection.setRequestProperty("Authorization","Bearer"+AccessToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpURLConnection.setDoOutput(true);
        Log.i("XXXX","here");
        //StringBuffer responseBody=new StringBuffer();
        OutputStream os= null;
        try {
            os = httpURLConnection.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String body="{\"message\" : \n\t " +
                "{ \"token\" : \""+s+"\",\n\t"+
                "\"data\": {\n\t\t"+
                "\"title\": \""+"invitation"+"\",\n\t\t"+
                "\"body\": "+"\"FCM_TEST\"\n\t\t}"+"\n\t}"+"\n}";
        Log.i("XXXX",body);
        try {
            os.write(body.getBytes());
            Log.i("Sending","success");
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
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String AccessToken() throws IOException
//    {   Path current= Paths.get("");
//        String ssss=current.toAbsolutePath().toString();
//        Log.i("patj",ssss);
//        Log.i("Hello", "dfsf");sp
    {

        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("service_account.json"))
       // "com/example/ilgwon/alarmbomb/Messaging/alarmbomb-5fbe8-firebase-adminsdk-3tmaz-aa67b3d00b.json"
                .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));

        googleCredential.refreshToken();
        Log.i("Token", String.valueOf(googleCredential.refreshToken()));
        return googleCredential.getAccessToken();
    }

}

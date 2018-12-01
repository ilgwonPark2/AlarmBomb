package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import com.thenerds.ilgwon.alarmbomb.Messaging.UrlSending;
import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.service.AlarmRingService;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class AlarmFailureActivity extends Activity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_failure);
        intent = getIntent();
        try {
            sendINV("mission_failure");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMoney(View view) {
        final String accountBank = intent.getStringExtra("accountBank");
        final String accountNum = intent.getStringExtra("accountBank");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL githubEndpoint = new URL("https://toss.im/transfer-web/linkgen-api/link");
                    // Create connection
                    HttpsURLConnection myConnection = (HttpsURLConnection) githubEndpoint.openConnection();
                    myConnection.setDoInput(true);
                    myConnection.setRequestMethod("POST");
                    myConnection.setRequestProperty("Content-Type", "application/json");
                    myConnection.connect();
                    JSONObject json = new JSONObject();
                    json.accumulate("apiKey", "c48300c9bdfe48f490154aa354d53072");
                    json.accumulate("bankName", accountBank);
                    json.accumulate("bankAccountNo", accountNum);
                    json.accumulate("amount", "3000");
                    json.accumulate("message", "벌금!!!!");
                    OutputStreamWriter writer = new OutputStreamWriter(myConnection.getOutputStream());
                    writer.write(json.toString());
                    writer.flush();
                    writer.close();

                    int response = myConnection.getResponseCode();
                    if (response == 200) {
                        Log.d("REST POST", "The response is : " + response + "," + myConnection.getResponseMessage());
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader =
                                new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object
                        while (jsonReader.hasNext()) { // Loop through all keys
                            String key = jsonReader.nextName(); // Fetch the next key
                            if (key.equals("success")) { // Check if desired key
                                // Fetch the value as a String
                                jsonReader.beginObject();
                                while (jsonReader.hasNext()) {
                                    String name = jsonReader.nextName();
                                    //                                    Log.d("REST POST", "The response iss?s : " + name);
                                    if (name.equals("link")) {
                                        String link = jsonReader.nextString();
                                        //                                        Log.d("REST POST", "The response issss : " + link);
                                        Intent tossIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                        startActivity(tossIntent);
                                    } else {
                                        jsonReader.skipValue();
                                    }
                                }
                                jsonReader.endObject();
                                break; // Break out of the loop
                            } else {
                                jsonReader.skipValue(); // Skip values of other keys
                            }
                        }
                        jsonReader.close();
                        myConnection.disconnect();
                        sendINV("receiveCheck");
                    }

                } catch (Exception e) {
                    Log.e("REST POST", "Error : " + e.getMessage());
                }
            }
        });
        stopService(new Intent(this, AlarmRingService.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    void sendINV(String title) throws MalformedURLException, IOException {

        InputStream Token_file = getResources().openRawResource(R.raw.service_account);
        JSONObject body = new JSONObject();
        JSONObject message = new JSONObject();
        JSONObject data = new JSONObject();
        try {
            data.put("title", title);
            data.put("friend_name", MainActivity.user_name);
            body.put("data", data);
            message.put("token", AlarmAddActivity.Dest_pushToken);
            message.put("data", data);
            body.put("message", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UrlSending url = (UrlSending) new UrlSending(body.toString()).execute(Token_file);
    }
}

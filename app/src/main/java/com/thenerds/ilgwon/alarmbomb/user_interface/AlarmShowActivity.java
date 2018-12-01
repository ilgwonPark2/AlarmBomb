package com.thenerds.ilgwon.alarmbomb.user_interface;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.service.AlarmRingService;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class AlarmShowActivity extends AppCompatActivity {
    TextView textViewAlarmedTime;
    Intent intent_ringtone;
    Intent intent_pending;
    private String time;
    private String data;
    private String mission;
    private String accountNum;
    private String accountBank;
    private int reqCode;
    boolean isComplete = false;
    public static final int DEFAULT_MISSION_NOTHING = 900;
    public static final int DEFAULT_MISSION_DECIBEL = 901;
    public static final int DEFAULT_MISSION_SHAKING = 902;
    public static final int DEFAULT_MISSION_FEE = 903;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
        textViewAlarmedTime = (TextView) findViewById(R.id.textViewAlarmedTime);
        intent_pending = getIntent();
        // If pending intent has mission extra, it saves extras.
        if (intent_pending.hasExtra("mission")) {
            time = intent_pending.getStringExtra("time");
            data = intent_pending.getStringExtra("data");
            mission = intent_pending.getStringExtra("mission");
            reqCode = intent_pending.getIntExtra("reqCode", 0);
            accountNum = intent_pending.getStringExtra("accountNum");
            accountBank = intent_pending.getStringExtra("accountBank");
            //  Trigger the mission.
            triggerMission(mission);
            textViewAlarmedTime.setText(time + "\n" + data + "\n" + mission + "\n" + reqCode);
        }
    }

    /**
     * onActivityResult Callback
     * <p>
     * if the user completes the mission, it stops the Ringtone service.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            stopService(intent_ringtone);
            switch (requestCode) {
                case DEFAULT_MISSION_NOTHING:
                    break;
                case DEFAULT_MISSION_DECIBEL:
                    break;
                case DEFAULT_MISSION_SHAKING:
                    break;
                case DEFAULT_MISSION_FEE:
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data.getBooleanExtra("fail", false)) {
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
                                            Log.d("REST POST", "The response iss?s : " + name);
                                            if (name.equals("link")) {
                                                String link = jsonReader.nextString();
                                                Log.d("REST POST", "The response issss : " + link);
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
                                isComplete =true;
                                stopService(intent_ringtone);
                            }

                        } catch (Exception e) {
                            Log.e("REST POST", "Error : " + e.getMessage());
                        }
                    }
                });
                //                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com"));
                //                startActivityForResult(intent, 9999);
            }
        } else {
            if (data == null) {
                zombie();
            } else {
                isComplete = true;
            }
        }
    }


    /**
     * onDestroy callback
     * <p>
     * if the mission is incomplete and the user stops the application, it starts mission activity continuously.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        zombie();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    protected void zombie() {
        if (!isComplete) {
            Intent intent_zombie = new Intent(this, AlarmShowActivity.class);
            intent_zombie.putExtra("hour", time);
            intent_zombie.putExtra("data", data);
            intent_zombie.putExtra("mission", mission);
            intent_zombie.putExtra("reqCode", reqCode);
            intent_zombie.putExtra("accountBank", accountBank);
            intent_zombie.putExtra("accountNum", accountNum);
            // stop service since a service begins after starting activity.
            stopService(intent_ringtone);
            startActivity(intent_zombie);
        }
    }

    /**
     * Trigger the mission with given mission
     */
    protected void triggerMission(String mission) {
        switch (mission) {
            //            case "Do Nothing":
            //                Toast.makeText(this, "function comes soon. (pending)", Toast.LENGTH_LONG).show();
            //                break;
            case "Decibel":
                Intent intent_decibel = new Intent(AlarmShowActivity.this, MissionDecibelMeterActivity.class);
                startActivityForResult(intent_decibel, DEFAULT_MISSION_DECIBEL);
                break;
            case "Shaking":
                Intent intent_shaking = new Intent(AlarmShowActivity.this, MissionShakingActivity.class);
                startActivityForResult(intent_shaking, DEFAULT_MISSION_SHAKING);
                break;
            //            case "Fee":
            //                Toast.makeText(this, "function comes soon. (pending)", Toast.LENGTH_LONG).show();
            //                break;
            default:
                break;
        }
        // Play ringtone with background service.
        intent_ringtone = new Intent(this, AlarmRingService.class);
        startService(intent_ringtone);
    }
}

package com.example.ilgwon.alarmbomb.user_interface;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.service.AlarmRingService;


public class AlarmShowActivity extends AppCompatActivity {
    TextView textViewAlarmedTime;
    Intent intent_ringtone;
    Intent intent_pending;
    private String time;
    private String data;
    private String mission;
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

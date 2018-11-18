package com.example.ilgwon.alarmbomb.user_interface;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.service.AlarmRingService;


public class AlarmShowActivity extends AppCompatActivity {
    TextView textViewAlarmedTime;
    Intent intent_ringtone;
    public static final int DEFAULT_MISSION_NOTHING = 900;
    public static final int DEFAULT_MISSION_DECIBEL = 901;
    public static final int DEFAULT_MISSION_SHAKING = 902;
    public static final int DEFAULT_MISSION_FEE = 903;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
        textViewAlarmedTime = (TextView) findViewById(R.id.textViewAlarmedTime);
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String data = intent.getStringExtra("data");
        String mission = intent.getStringExtra("mission");


        switch (mission) {
            case "Do Nothing":
                Toast.makeText(this, "function comes soon. (pending)", Toast.LENGTH_LONG).show();
                break;
            case "Decibel":
                Intent intent_decibel = new Intent(AlarmShowActivity.this, MissionDecibelMeterActivity.class);
                startActivityForResult(intent_decibel, DEFAULT_MISSION_DECIBEL);
                break;
            case "Shaking":
                Intent intent_shaking = new Intent(AlarmShowActivity.this, MissionShakingActivity.class);
                startActivityForResult(intent_shaking, DEFAULT_MISSION_SHAKING);
                break;
            case "Fee":
                Toast.makeText(this, "function comes soon. (pending)", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }


        intent_ringtone = new Intent(this, AlarmRingService.class);
        startService(intent_ringtone);

        int reqCode = intent.getIntExtra("reqCode", 0);
        textViewAlarmedTime.setText(time + "\n" + data + "\n" + mission + "\n" + reqCode);
    }

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
        }
    }
}

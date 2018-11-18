package com.example.ilgwon.alarmbomb.user_interface;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.R;


public class AlarmShowActivity extends AppCompatActivity {
    TextView textViewAlarmedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_show);
        textViewAlarmedTime = (TextView) findViewById(R.id.textViewAlarmedTime);
        Intent intent = getIntent();
        String time = intent.getStringExtra("time");
        String data = intent.getStringExtra("data");
        String mission = intent.getStringExtra("mission");
        switch (mission){
            case "Do Nothing":
                Toast.makeText(this, "function comes soon. (pending)",Toast.LENGTH_LONG).show();
                break;
            case "Decibel":
                Intent intent_decibel = new Intent(AlarmShowActivity.this, MissionDecibelMeterActivity.class);
                startActivity(intent_decibel);
                break;
            case "Shaking":
                Intent intent_shaking = new Intent(AlarmShowActivity.this, MissionShakingActivity.class);
                startActivity(intent_shaking);
                break;
            case "Fee":
                Toast.makeText(this, "function comes soon. (pending)",Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        int reqCode = intent.getIntExtra("reqCode", 0);
        textViewAlarmedTime.setText(time + "\n" + data + "\n" + mission + "\n" + reqCode);
    }
}

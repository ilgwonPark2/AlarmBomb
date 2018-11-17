package com.example.ilgwon.alarmbomb.user_interface;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
        String mission=intent.getStringExtra("mission");
        int reqCode = intent.getIntExtra("reqCode", 0);
        textViewAlarmedTime.setText(time + "\n" + data + "\n" +mission+"\n"+ reqCode);
    }
}

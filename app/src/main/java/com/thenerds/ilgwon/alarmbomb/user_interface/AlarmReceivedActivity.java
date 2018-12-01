package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.service.AlarmRingService;

public class AlarmReceivedActivity extends Activity {
    public static Activity _AlarmReceivedActivity;
    public static boolean isWakeup = false;
    TextView friendView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _AlarmReceivedActivity = AlarmReceivedActivity.this;
        setContentView(R.layout.activity_alarm_received);
        String friend_name = getIntent().getStringExtra("friend_name");
        friendView = findViewById(R.id.from_friend);
        friendView.setText("From: " + friend_name);
        startService(new Intent(this, AlarmRingService.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isWakeup = false) {
            stopService(new Intent(this, AlarmRingService.class));
            startActivity(new Intent(this, AlarmReceivedActivity.class));
        }
    }
}

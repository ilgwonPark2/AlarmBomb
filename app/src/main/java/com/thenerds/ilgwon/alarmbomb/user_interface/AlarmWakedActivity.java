package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.service.AlarmRingService;

public class AlarmWakedActivity extends Activity {
    TextView friendView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlarmReceivedActivity._AlarmReceivedActivity.finish();
        setContentView(R.layout.activity_alarm_money);

        friendView2 = findViewById(R.id.from_friend2);
        String friend_name = getIntent().getStringExtra("friend_name");
        friendView2.setText("From: " + friend_name);

        stopService(new Intent(this, AlarmRingService.class));
    }
}

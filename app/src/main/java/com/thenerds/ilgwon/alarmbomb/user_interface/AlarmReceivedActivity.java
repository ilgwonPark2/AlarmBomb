package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.service.AlarmRingService;

public class AlarmReceivedActivity extends Activity {
    public static Activity _AlarmReceivedActivity;
    public static boolean isWakeup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _AlarmReceivedActivity = AlarmReceivedActivity.this;
        setContentView(R.layout.activity_alarm_received);

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

package com.example.ilgwon.alarmbomb.user_interface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ilgwon.alarmbomb.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void settingAlarm(View view) {

        Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
        startActivity(intent);
    }

    public void settingDecibel(View view) {

        Intent intent = new Intent(MainActivity.this, MissionDecibelMeterActivity.class);
        startActivity(intent);
    }
}

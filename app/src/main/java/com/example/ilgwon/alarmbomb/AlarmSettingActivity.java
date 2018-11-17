package com.example.ilgwon.alarmbomb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmSettingActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private Context mContext;
    public static final int DEFAULT_ALARM_REQUEST = 800;


    Button btnAddAlarm;
    ListView listViewAlarm;
    ArrayList<AlarmData> alarmArray = new ArrayList<AlarmData>();
    GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedEditor;

    AlarmAdapter alarmAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        mContext = getApplicationContext();
        btnAddAlarm = findViewById(R.id.btn_add_activity);
        listViewAlarm = findViewById(R.id.listViewAlarm);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

        alarmAdapter = new AlarmAdapter(mContext, alarmArray);
        listViewAlarm.setAdapter(alarmAdapter);

        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmSettingActivity.this, AlarmAddActivity.class);
                startActivityForResult(intent, 800);
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("check", "onActivityResult");
        if (requestCode == DEFAULT_ALARM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent intent = data;
                int hh = intent.getIntExtra("hour", 0);
                int mm = intent.getIntExtra("minute", 0);
                String mission=intent.getStringExtra("mission");
                int reqCode = DEFAULT_ALARM_REQUEST + alarmArray.size();
                int size = sharedPref.getInt("size", 0);
                int i = (size == 0) ? 1 : size + 1;
                Log.i("check", "check loop alarm array size:" + size + 1);
                alarmAdd(hh, mm,mission, reqCode, i);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("check", "onResume");
        alarmArray.clear();
        int size = sharedPref.getInt("size", 0);
        Log.i("check", "check size:" + size);
        if (size != 0)
            for (int i = 1; i < size + 1; i++) {
                int hh = sharedPref.getInt("list" + i + "hh", 0);
                int mm = sharedPref.getInt("list" + i + "mm", 0);
                int reqCode = sharedPref.getInt("list" + i + "reqCode", 0);
                alarmArray.add(new AlarmData(hh, mm, reqCode));
                Log.i("check", "check loop:" + i + ", " + hh + ", mm: " + mm);
            }
        alarmAdapter.notifyDataSetChanged();
    }


    public void alarmAdd(int hour, int minute, String mission,int reqCode, int i) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
        Log.i("check", "alarmAdd :" + hour + ", mm: " + minute + ", int i: " + i);
        sharedEditor.putInt("list" + i + "hh", hour);
        sharedEditor.putInt("list" + i + "mm", minute);
        sharedEditor.putInt("list" + i + "reqCode", reqCode);
        sharedEditor.putInt("size", i);
        sharedEditor.commit();
        //        int size = sharedPref.getInt("size", 0);
        //        Log.i("check", "check size in alarm Add:" + size);
        //        Log.i("check", "shared commit");
        Intent intent = new Intent(AlarmSettingActivity.this, AlarmShowActivity.class);
        intent.putExtra("time", hour + ":" + minute);
        intent.putExtra("data", "dd: " + currentCalendar.getTime().toLocaleString());
        intent.putExtra("reqCode", reqCode);
        PendingIntent pi = PendingIntent.getActivity(AlarmSettingActivity.this, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

    }
}

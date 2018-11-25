package com.example.ilgwon.alarmbomb.user_interface;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_alarm.AlarmAdapter;
import com.example.ilgwon.alarmbomb.module_alarm.AlarmData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmSettingActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private Context mContext;
    public static final int DEFAULT_ALARM_REQUEST = 800;

    FloatingActionButton btnAddAlarm;
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
        btnAddAlarm = findViewById(R.id.add);
        listViewAlarm = findViewById(R.id.listViewAlarm);

        //  declare "Alarm" sharePref and sharedEditor.
        sharedPref = getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

        // set an adapter to the listViewAlarm.
        alarmAdapter = new AlarmAdapter(mContext, alarmArray, sharedPref);
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


    /**
     * onActivityResult Callback
     * <p>
     * After adding a alarm, it adds that alarm to the array.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEFAULT_ALARM_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent intent = data;
                int hh = intent.getIntExtra("hour", 0);
                int mm = intent.getIntExtra("minute", 0);
                String mission = intent.getStringExtra("mission");
                int reqCode = DEFAULT_ALARM_REQUEST + alarmArray.size();
                int size = sharedPref.getInt("size", 0);
                int i = (size == 0) ? 1 : size + 1;
                alarmAdd(hh, mm, mission, reqCode, i);
            }
        }
    }

    /**
     * onResume Callback
     *
     * after clearing alarmArray, it refreshes data from sharedPref.
     */
    @Override
    protected void onResume() {
        super.onResume();
        alarmArray.clear();
        int size = sharedPref.getInt("size", 0);
        if (size != 0)
            for (int i = 1; i < size + 1; i++) {
                int hh = sharedPref.getInt("list" + i + "hh", 0);
                int mm = sharedPref.getInt("list" + i + "mm", 0);
                int reqCode = sharedPref.getInt("list" + i + "reqCode", 0);
                String mission = sharedPref.getString("list" + i + "mission", null);
                alarmArray.add(new AlarmData(hh, mm, mission, reqCode));
            }
        alarmAdapter.notifyDataSetChanged();
    }

    /**
     * onResume Callback
     *
     * @param hour, minute, mission, reqcode, i
     *              Adding an alarm to the array(list).
     */
    public void alarmAdd(int hour, int minute, String mission, int reqCode, int i) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

        // put following data to the sharedEditor.
        sharedEditor.putInt("list" + i + "hh", hour);
        sharedEditor.putInt("list" + i + "mm", minute);
        sharedEditor.putString("list" + i + "mission", mission);
        sharedEditor.putInt("list" + i + "reqCode", reqCode);
        sharedEditor.putInt("size", i);
        sharedEditor.commit();

        int currentYY = currentCalendar.get(Calendar.YEAR);
        int currentMM = currentCalendar.get(Calendar.MONTH);
        int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

        gregorianCalendar.set(currentYY, currentMM, currentDD, hour, minute, 00);

        if (gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()) {
            gregorianCalendar.set(currentYY, currentMM, currentDD + 1, hour, minute, 00);
        }

        //  Put following data to the intent for the AlarmShowActivity.
        Intent intent = new Intent(AlarmSettingActivity.this, AlarmShowActivity.class);
        intent.putExtra("time", hour + ":" + minute);
        intent.putExtra("data", "dd: " + currentCalendar.getTime().toLocaleString());
        intent.putExtra("mission", mission);
        intent.putExtra("reqCode", reqCode);

        //  Use PendingIntent with timer, to get an alarm in given time.
        PendingIntent pi = PendingIntent.getActivity(AlarmSettingActivity.this,
                reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis(), pi);
    }


}

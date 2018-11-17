package com.example.ilgwon.alarmbomb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmAddActivity extends Activity {
    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
    public static final int DEFAULT_ALARM_REQUEST = 800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        timePickerAlarmTime = (TimePicker) findViewById(R.id.timePickerAlarmTime);
        timePickerAlarmTime.setIs24HourView(false);


        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hh = timePickerAlarmTime.getHour();
                int mm = timePickerAlarmTime.getMinute();
                //                int reqCode = DEFAULT_ALARM_REQUEST + arrayListAlarmTimeItem.size();
                int reqCode = DEFAULT_ALARM_REQUEST;

                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

                int currentYY = currentCalendar.get(Calendar.YEAR);
                int currentMM = currentCalendar.get(Calendar.MONTH);
                int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

                gregorianCalendar.set(currentYY, currentMM, currentDD, hh, mm, 00);

                if (gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()) {
                    gregorianCalendar.set(currentYY, currentMM, currentDD + 1, hh, mm, 00);
                    Log.i("TAG", gregorianCalendar.getTimeInMillis() + ":");
                }

                Intent intent = new Intent(AlarmAddActivity.this, AlarmSettingActivity.class);
                intent.putExtra("hour", hh);
                intent.putExtra("minute", mm);
                intent.putExtra("reqCode", reqCode);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }
}

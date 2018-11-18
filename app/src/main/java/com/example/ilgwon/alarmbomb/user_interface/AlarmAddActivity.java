package com.example.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.ilgwon.alarmbomb.R;

import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmAddActivity extends Activity {
    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
    public static final int DEFAULT_ALARM_REQUEST = 800;
    Spinner s;
    String mission_select;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        timePickerAlarmTime = (TimePicker) findViewById(R.id.timePickerAlarmTime);
        timePickerAlarmTime.setIs24HourView(false);
        s = (Spinner) findViewById(R.id.mission);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mission_select = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hh = timePickerAlarmTime.getHour();
                int mm = timePickerAlarmTime.getMinute();
                int reqCode = DEFAULT_ALARM_REQUEST;

                Intent intent = getIntent();
//                Intent intent = new Intent(AlarmAddActivity.this, AlarmSettingActivity.class);
                intent.putExtra("hour", hh);
                intent.putExtra("minute", mm);
                intent.putExtra("mission", mission_select);
                intent.putExtra("reqCode", reqCode);

                setResult(Activity.RESULT_OK, intent);
                finish();
//                startActivity(intent);
            }
        });

    }
}

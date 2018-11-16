package com.example.ilgwon.alarmbomb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmSettingActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"TEST1", "TEST2", "TEST3"} ;
    private AlarmManager alarmManager;
    private Context mContext;
    public static final int DEFAULT_ALARM_REQUEST = 800;


//    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    ListView listViewAlarm;
    ArrayList<AlarmData> arrayListAlarmTimeItem = new ArrayList<AlarmData>();
//    GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sharedEditor;

    AlarmAdapter arrayAdapterAlarmList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);

        //        혜인코드
        //        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        //        ListView listview = (ListView) findViewById(R.id.alarm_list) ;
        //        listview.setAdapter(adapter) ;


        mContext = getApplicationContext();

//        timePickerAlarmTime = (TimePicker)findViewById(R.id.timePickerAlarmTime);
        btnAddAlarm = (Button)findViewById(R.id.btn_add_activity);
        listViewAlarm	= (ListView)findViewById(R.id.listViewAlarm);

        sharedPref = getPreferences(Context.MODE_PRIVATE);
        sharedEditor = sharedPref.edit();

//        timePickerAlarmTime.setIs24HourView(false);

        arrayAdapterAlarmList = new AlarmAdapter(mContext, arrayListAlarmTimeItem);
        listViewAlarm.setAdapter(arrayAdapterAlarmList);

        alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        btnAddAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmSettingActivity.this, AlarmAddActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 800);
            }

        });
//        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                int hh = timePickerAlarmTime.getCurrentHour();
//                int mm = timePickerAlarmTime.getCurrentMinute();
//                int reqCode = DEFAULT_ALARM_REQUEST+arrayListAlarmTimeItem.size();
//                int i =arrayListAlarmTimeItem.size();
//
//                arrayListAlarmTimeItem.add(new AlarmData(hh, mm, reqCode));
//                arrayAdapterAlarmList.notifyDataSetChanged();
//
//                sharedEditor.putInt("list"+i+"hh", hh);
//                sharedEditor.putInt("list"+i+"mm", mm);
//                sharedEditor.putInt("list"+i+"reqCode", reqCode);
//                sharedEditor.putInt("size", i);
//                sharedEditor.commit();
//
//                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
//
//                int currentYY = currentCalendar.get(Calendar.YEAR);
//                int currentMM = currentCalendar.get(Calendar.MONTH);
//                int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
//
//                gregorianCalendar.set(currentYY, currentMM, currentDD, hh, mm,00);
//
//                if(gregorianCalendar.getTimeInMillis() < currentCalendar.getTimeInMillis()){
//                    gregorianCalendar.set(currentYY, currentMM, currentDD+1, hh, mm,00);
//                    Log.i("TAG",gregorianCalendar.getTimeInMillis()+":");
//                }
//
//
//                Intent intent = new Intent(AlarmSettingActivity.this, AlarmShowActivity.class);
//                intent.putExtra("time", hh+":"+mm);
//                intent.putExtra("data", "dd: " + currentCalendar.getTime().toLocaleString());
//                intent.putExtra("reqCode", reqCode);
//
//                //				Toast.makeText(mContext, "reqCode : "+reqCode, 0).show();
//
//                PendingIntent pi = PendingIntent.getActivity(AlarmSettingActivity.this, reqCode, intent,PendingIntent.FLAG_UPDATE_CURRENT );
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis() ,AlarmManager.INTERVAL_DAY, pi);
//            }
//        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 800) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Intent intent = data;
                String time = intent.getStringExtra("time");
//                String data = intent.getStringExtra("data");
                int reqCode = intent.getIntExtra("reqCode", 0);
                Toast.makeText(this,"result "+time+"," +reqCode, Toast.LENGTH_LONG).show();
//                Toast.makeText(this,"result "+reqCode, Toast.LENGTH_LONG).show();

                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayListAlarmTimeItem.clear();
        int size = sharedPref.getInt("size", 0);
        if(size !=0)
            for(int i = 0 ; i < size+1; i ++ ){
                int hh = sharedPref.getInt("list"+i+"hh", 0);
                int mm = sharedPref.getInt("list"+i+"mm", 0);
                int reqCode = sharedPref.getInt("list"+i+"reqCode", 0);

                arrayListAlarmTimeItem.add(new AlarmData(hh, mm, reqCode));
            }
        arrayAdapterAlarmList.notifyDataSetChanged();
    }
}

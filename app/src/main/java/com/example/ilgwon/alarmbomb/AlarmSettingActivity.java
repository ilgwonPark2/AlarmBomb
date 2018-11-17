package com.example.ilgwon.alarmbomb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AlarmSettingActivity extends AppCompatActivity {
    //    static final String[] LIST_MENU = {"TEST1", "TEST2", "TEST3"};
    private AlarmManager alarmManager;
    private Context mContext;
    public static final int DEFAULT_ALARM_REQUEST = 800;


     Button btnAddAlarm;
     ListView listViewAlarm;
     ArrayList<AlarmData> alarmArray = new ArrayList<AlarmData>();
    GregorianCalendar currentCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
    //    private SharedPreferences sharedPref;
    //    private SharedPreferences.Editor sharedEditor;

     AlarmAdapter alarmAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        mContext = getApplicationContext();
        //        혜인코드
        //        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        //        ListView listview = (ListView) findViewById(R.id.alarm_list) ;
        //        listview.setAdapter(adapter) ;

        btnAddAlarm = findViewById(R.id.btn_add_activity);
        listViewAlarm = findViewById(R.id.listViewAlarm);
        //
        //                sharedPref = getPreferences(Context.MODE_PRIVATE);
        //                sharedEditor = sharedPref.edit();

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
        //        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 800) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Intent intent = data;
                int hh = intent.getIntExtra("hour", 0);
                int mm = intent.getIntExtra("minute", 0);
                String mission=intent.getStringExtra("mission");
                int reqCode = intent.getIntExtra("reqCode", 0);
                GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));

                alarmAdd(hh,mm,mission,reqCode,intent);
//                alarmAdapter.notifyDataSetChanged();
//
//                PendingIntent pi = PendingIntent.getActivity(AlarmSettingActivity.this, reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
//
//                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                // Do something with the contact here (bigger example below)
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        alarmArray.clear();
        //                int size = sharedPref.getInt("size", 0);
        //                if (size != 0)
        //                    for (int i = 0; i < size + 1; i++) {
        //                        int hh = sharedPref.getInt("list" + i + "hh", 0);
        //                        int mm = sharedPref.getInt("list" + i + "mm", 0);
        //                        int reqCode = sharedPref.getInt("list" + i + "reqCode", 0);
        //
        //                        alarmArray.add(new AlarmData(hh, mm, reqCode));
        //                    }
//        alarmAdapter.notifyDataSetChanged();
    }

    public void alarmAdd(int hour, int minute, String mission, int reqCode, Intent intent) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+09:00"));
        Toast.makeText(this, "result " + hour + "," + minute + ',' + reqCode, Toast.LENGTH_LONG).show();

        alarmArray.add(new AlarmData(hour, minute, reqCode));

        for (int i = 0; i < alarmArray.size(); i++) {
            AlarmData abc = alarmArray.get(i);
            Log.i("array", "hihi hh:" + abc.hh + ", mm: " + abc.mm);

        }
        alarmAdapter.notifyDataSetChanged();
        				Intent intent2 = new Intent(AlarmSettingActivity.this, AlarmShowActivity.class);
        				intent2.putExtra("time", hour+":"+minute);
        				intent2.putExtra("data", "dd: " + currentCalendar.getTime().toLocaleString());
        				intent2.putExtra("mission","mission:"+mission);
        				intent2.putExtra("reqCode", reqCode);
        PendingIntent pi = PendingIntent.getActivity(AlarmSettingActivity.this, reqCode, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, gregorianCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

    }
}

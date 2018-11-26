package com.example.ilgwon.alarmbomb.user_interface;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_alarm.FriendAdapter;
import com.example.ilgwon.alarmbomb.module_alarm.FriendSingleItem;

public class AlarmAddActivity extends Activity {
    int hh;
    int mm;

    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    Button btnAddFriend;
    public static final int DEFAULT_ALARM_REQUEST = 800;
    Spinner s;
    String mission_select;


    public static ListView friendView;
    public static FriendAdapter friendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        btnAddFriend=(Button)findViewById(R.id.find_friend);

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

        /**
         * btnAddAlarm setOnClickListener
         *
         * if the user clicks an add button, it puts following data to the intent and finish with the result.
         */
        btnAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hh = timePickerAlarmTime.getHour();
                mm = timePickerAlarmTime.getMinute();
                int reqCode = DEFAULT_ALARM_REQUEST;

                Intent intent = getIntent();
                intent.putExtra("hour", hh);
                intent.putExtra("minute", mm);
                intent.putExtra("mission", mission_select);
                intent.putExtra("reqCode", reqCode);
                setResult(Activity.RESULT_OK, intent);
                if (mission_select == "Decibel") {
                    checkPermission();
                } else {
                    finish();
                }
            }
        });
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view =getLayoutInflater().inflate(R.layout.find_friend,null);
                friendView=view.findViewById(R.id.friendlist);
                friendView.setAdapter(friendAdapter);
                AlertDialog.Builder alertBuilder=new AlertDialog.Builder(AlarmAddActivity.this);
                alertBuilder.setTitle("find friend who wake you up!");
                alertBuilder.setView(view);
                alertBuilder.show();



            }
        });

    }

    /**
     * check permission to the user.
     */
    public void checkPermission() {
        // Check 3 permissions to do this mission.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // displaying AlertDialog with rationale
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                AlertDialog.Builder builder = new AlertDialog.Builder((AlarmAddActivity.this), 0);
                builder.setTitle("AUDIO PERMISSION").setMessage("RECORD AUDIO, STORAGE ACCESS permission is needed to estimate decibel! Would you try to get permission again?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(AlarmAddActivity.this,
                                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                        7777);
                            }
                        });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        7777);
            }
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 7777) {
            finish();
        }
    }
}

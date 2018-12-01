package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thenerds.ilgwon.alarmbomb.Messaging.UrlSending;
import com.thenerds.ilgwon.alarmbomb.Model.NotificationModel;
import com.thenerds.ilgwon.alarmbomb.Model.UserModel;
import com.thenerds.ilgwon.alarmbomb.R;
import com.thenerds.ilgwon.alarmbomb.module_alarm.FriendAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.thenerds.ilgwon.alarmbomb.user_interface.MainActivity.user_name;

public class AlarmAddActivity extends Activity {
    int hh;
    int mm;

    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    Button btnSearchFriend;
    EditText search_phone;
    TextView userid;
    public static final int DEFAULT_ALARM_REQUEST = 800;
    Spinner s;
    String mission_select;
    UserModel destinationModel;
    NotificationModel notificationModel;
    private String Dest_account_bank;
    private String Dest_account;
    private String Dest_id;
    static String Dest_uid;
    static String Dest_pushToken;


    public static ListView friendView;
    public static FriendAdapter friendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        btnSearchFriend = (Button) findViewById(R.id.search);
        search_phone = (EditText) findViewById(R.id.friendtext);
        userid = (TextView) findViewById(R.id.userid);
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
        btnSearchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String friend_phone = search_phone.getText().toString();
                Query query = FirebaseDatabase.getInstance().getReference().child("users").orderByChild("phone").equalTo(friend_phone);
                query.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        destinationModel = new UserModel();
                        List<String> temp = new ArrayList<String>();
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            temp.add(d.getValue().toString());
                        }
                        if (temp.size() != 0) {
                            String value = temp.get(0);
                            value = value.substring(1, value.length() - 1);
                            String[] keyValue = value.split(", ");
                            for (String pair : keyValue) {
                                String[] entry = pair.split("=");
                                map.put(entry[0].trim(), entry[1].trim());
                            }
                            destinationModel.account_bank = map.get("accountBank");
                            Dest_account_bank = destinationModel.account_bank;
                            destinationModel.account = map.get("accountNumber");
                            Dest_account = destinationModel.account;
                            destinationModel.destination_id = map.get("uid");
                            Dest_id = destinationModel.destination_id;
                            destinationModel.uid = map.get("userID");
                            Dest_uid = destinationModel.uid;
                            destinationModel.pushToken = map.get("pushToken");
                            Dest_pushToken = destinationModel.pushToken;
                        } else {
                            Dest_uid = "No such id exists";
                        }
                        userid.setText(Dest_uid);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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

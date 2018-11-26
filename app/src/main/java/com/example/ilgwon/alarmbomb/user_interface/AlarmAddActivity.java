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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.Model.NotificationModel;
import com.example.ilgwon.alarmbomb.Model.UserModel;
import com.example.ilgwon.alarmbomb.R;
import com.example.ilgwon.alarmbomb.module_alarm.FriendAdapter;
import com.example.ilgwon.alarmbomb.module_alarm.FriendSingleItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.ilgwon.alarmbomb.user_interface.MainActivity.uid;

public class AlarmAddActivity extends Activity {
    int hh;
    int mm;

    TimePicker timePickerAlarmTime;
    Button btnAddAlarm;
    Button btnSearchFriend;
    EditText search_phone;
    public static final int DEFAULT_ALARM_REQUEST = 800;
    Spinner s;
    String mission_select;
    UserModel destinationModel;


    public static ListView friendView;
    public static FriendAdapter friendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        btnSearchFriend=(Button)findViewById(R.id.search);
        search_phone=(EditText)findViewById(R.id.friendtext);

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

                //send message to Friend
                sendGcm();
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
                String friend_phone=search_phone.getText().toString();
                Query query=FirebaseDatabase.getInstance().getReference().child("users").orderByChild("phone").equalTo(friend_phone);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        destinationModel=new UserModel();
                        List<String>temp=new ArrayList<String>();
                        for(DataSnapshot d:dataSnapshot.getChildren()){
                            temp.add(d.getValue().toString());
                        }
                        destinationModel.account_bank=temp.get(0).toString();
                        destinationModel.account=temp.get(1).toString();
                        destinationModel.destination_id=temp.get(5).toString();
                        destinationModel.pushToken=temp.get(6).toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                if(FirebaseDatabase.getInstance().getReference().child("users").orderByChild("phone").equalTo(friend_phone)==null){
//                    Toast.makeText(getApplicationContext(),"Nothing",Toast.LENGTH_LONG).show();
//                }
//                else{ //모델 데려와야해
//                    destinationModel=new UserModel();
//                    destinationModel.uid=uid;
//                    destinationModel.account="";
//                    destinationModel.destination_id="";
//                }



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

    void sendGcm(){
        Gson gson=new Gson();
        int new_mm=mm+5;
        NotificationModel notificationModel= new NotificationModel();
        notificationModel.to=destinationModel.pushToken;
        notificationModel.notification.title=""; //보낸이 전화번호 또는 이름
        notificationModel.notification.text="wake me up"+hh+" : "+new_mm+" ! ";
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));
        Request request=new Request.Builder()
                .header("Content-Type","application/json")
                .addHeader("Authorization","key=AIzaSyC0mlFTA7hMF94OS2T8ZBNg3wSSaXfgsFQ")
                .url("https://gcm-http.googleapis.com/gcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });

    }
}

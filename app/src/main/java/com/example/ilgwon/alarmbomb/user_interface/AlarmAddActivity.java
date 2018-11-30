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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.Messaging.Access_Token;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;


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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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
    TextView userid;
    public static final int DEFAULT_ALARM_REQUEST = 800;
    Spinner s;
    String mission_select;
    UserModel destinationModel;
    NotificationModel notificationModel;
    private String Dest_account_bank;
    private String Dest_account;
    private String Dest_id;
    private String Dest_uid;
    private String Dest_pushToken;


    public static ListView friendView;
    public static FriendAdapter friendAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);

        btnAddAlarm = (Button) findViewById(R.id.btnAddAlarm);
        btnSearchFriend=(Button)findViewById(R.id.search);
        search_phone=(EditText)findViewById(R.id.friendtext);
        userid=(TextView)findViewById(R.id.userid);
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
                try {
                    sendINV();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                Log.d("Parkil", query.toString());
                query.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        destinationModel = new UserModel();
                        List<String> temp = new ArrayList<String>();
                        HashMap<String, String> map = new HashMap<String, String>();
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            temp.add(d.getValue().toString());
                        }

                        if(temp.size() !=0){
                            String value = temp.get(0);
                            value = value.substring(1,value.length()-1);
                            String[] keyValue = value.split(", ");
                            for(String pair: keyValue){
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
                            Dest_uid="No such id exists";
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

    void sendINV() throws IOException {
        notificationModel= new NotificationModel();
        notificationModel.to=destinationModel.pushToken;
        notificationModel.notification.title="invitation"; //보낸이 전화번호 또는 이름
        notificationModel.notification.body="wake me up"+hh+" : "+mm+" ! ";
        URL url=new URL("https//fcm.googleapis.com/v1/projects/alarmbomb-5fbe8/messages:send HTTP/1.1");
        HttpsURLConnection httpURLConnection=(HttpsURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestProperty("Content-Type","application/json; UTF-8");
        httpURLConnection.setRequestProperty("Authorization","Bearer "+ Access_Token.AccessToken());
        httpURLConnection.setDoOutput(true);
        OutputStream os=httpURLConnection.getOutputStream();
        StringBuffer responseBody=new StringBuffer();
        String body="{\"message\" : \n\t " +
                "{ \"token\" : \""+notificationModel.to+"\",\n\t"+
                "\"data\": {\n\t\t"+
                "\"title\": \""+notificationModel.notification.title+"\",\n\t"+
                "\"body\": "+notificationModel.notification.body+"\",\n\t}";
        Log.i("XXXX",body);
        os.write(body.getBytes());
        os.flush();
        os.close();
// Gson gson=new Gson();
//        int new_mm=mm+5;
//
//        NotificationModel notificationModel= new NotificationModel();
//        notificationModel.to=destinationModel.pushToken;
//        notificationModel.notification.title="invitation"; //보낸이 전화번호 또는 이름
//        notificationModel.notification.body="wake me up"+hh+" : "+new_mm+" ! ";
//        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json; charset=utf8"),gson.toJson(notificationModel));
//        Request request=new Request.Builder()
//                .header("Content-Type","application/json")
//                .addHeader("Authorization","Bearer AIzaSyC0mlFTA7hMF94OS2T8ZBNg3wSSaXfgsFQ")
//                .url("//fcm.googleapis.com/v1/projects/alarmbomb-5fbe8/messages:send HTTP/1.1")
//                .post(requestBody)
//                .build();
//        OkHttpClient okHttpClient=new OkHttpClient();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//            }
//        });

    }


}

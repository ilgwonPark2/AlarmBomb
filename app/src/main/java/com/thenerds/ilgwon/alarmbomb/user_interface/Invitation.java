package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.thenerds.ilgwon.alarmbomb.Messaging.UrlSending;
import com.thenerds.ilgwon.alarmbomb.Model.UserModel;
import com.thenerds.ilgwon.alarmbomb.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.thenerds.ilgwon.alarmbomb.user_interface.AlarmAddActivity.Dest_pushToken;
import static com.thenerds.ilgwon.alarmbomb.user_interface.MainActivity.user_name;

public class Invitation extends Activity {
    Intent invitation;
    String Friend;
    String time;
    String code;
    String from;
    TextView friend_name;
    TextView Time;
    Button yes;
    Button no;
    String Token_from;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitation);
        Log.i("hi", "there");
        invitation = getIntent();
        friend_name = findViewById(R.id.friendName);
        yes = findViewById(R.id.Yes);
        no = findViewById(R.id.No);
        Time = findViewById(R.id.time_sent);

        time = invitation.getStringExtra("time");
        Friend = invitation.getStringExtra("friend");
        code = invitation.getStringExtra("code");
        from=invitation.getStringExtra("from");

        friend_name.setText(Friend);
        Time.setText(time);

    }

    public void yes_act() throws JSONException {
        InputStream Token_file = getResources().openRawResource(R.raw.service_account);
        JSONObject root = new JSONObject();
        JSONObject second = new JSONObject();
        JSONObject third = new JSONObject();
        third.put("title","YES");
        third.put("code", code);
        second.put("data", third);
        second.put("token", from);
        root.put("message", second);

        UrlSending url = (UrlSending) new UrlSending(root.toString()).execute(Token_file);

    }



    public void no_act() throws JSONException {
        InputStream Token_file = getResources().openRawResource(R.raw.service_account);
        JSONObject root = new JSONObject();
        JSONObject second = new JSONObject();
        JSONObject third = new JSONObject();
        third.put("title","NO");
        third.put("code", code);
        second.put("data", third);
        second.put("token", from);
        root.put("message", second);

        UrlSending url = (UrlSending) new UrlSending(root.toString()).execute(Token_file);

    }

}
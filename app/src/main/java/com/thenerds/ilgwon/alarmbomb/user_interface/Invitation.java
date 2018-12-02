package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;

public class Invitation extends Activity {
    Intent invitation;
    String Friend;
    String time;
    String code;
    TextView friend_name;
    TextView Time;
    Button yes;
    Button no;


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

        friend_name.setText(Friend);
        Time.setText(time);

    }

    public void yes_act() {


    }

    public void no_act() {

    }
}

package com.thenerds.ilgwon.alarmbomb.user_interface;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;

public class Invitation extends AppCompatActivity {
    Intent invitation;
    String Friend;
    String hh;
    String mm;
    TextView friend_name;
    TextView Time;
    Button yes;
    Button no;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.invitation);
        friend_name = findViewById(R.id.friendName);
        yes = findViewById(R.id.Yes);
        no = findViewById(R.id.No);
        Time = findViewById(R.id.time_sent);
        invitation = getIntent();
        hh = invitation.getStringExtra("hour");
        mm = invitation.getStringExtra("minute");
        Friend = invitation.getStringExtra("friend");
        friend_name.setText(Friend);
        Time.setText(hh + " : " + mm);


        super.onCreate(savedInstanceState);
    }

    public void yes_act() {


    }

    public void no_act() {

    }
}

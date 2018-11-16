package com.example.ilgwon.alarmbomb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AlarmSettingActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"TEST1", "TEST2", "TEST3"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;
        ListView listview = (ListView) findViewById(R.id.alarm_list) ;
        listview.setAdapter(adapter) ;


    }
}

package com.example.ilgwon.alarmbomb.module_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.user_interface.AlarmShowActivity;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {

    Context mContext;
    //    ArrayList<String> mData;
    LayoutInflater mInflate;
    ArrayList<AlarmData> arrayListAlarmData;
    SharedPreferences sharedPref;

    public AlarmAdapter(Context context, ArrayList<AlarmData> arrayListAlarmData, SharedPreferences sharedPreferences) {
        mContext = context;
        this.arrayListAlarmData = arrayListAlarmData;
        mInflate = LayoutInflater.from(mContext);
        this.sharedPref = sharedPreferences;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListAlarmData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        Toast.makeText(mContext, "position : " + position, Toast.LENGTH_LONG).show();
        return arrayListAlarmData.get(position).reqCode;
    }

    public boolean removeData(int position) {
        arrayListAlarmData.remove(position);
        notifyDataSetChanged();
        Log.i("check", "check position:" + position);
        int size = sharedPref.getInt("size", 0);
        Log.i("check", "remove check size:" + size);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("list" + position + 1 + "hh");
        editor.remove("list" + position + 1 + "mm");
        editor.remove("list" + position + 1 + "reqCode");
        editor.remove("size");
        editor.putInt("size", size - 1);
        editor.commit();
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutSingleAlarmItem layoutSingleAlarmItem = (LinearLayoutSingleAlarmItem) convertView;

        if (layoutSingleAlarmItem == null) {
            layoutSingleAlarmItem = new LinearLayoutSingleAlarmItem(mContext);
            layoutSingleAlarmItem.setOnRemoveButtonClickListener(onRemoveButtonClickListener);
        }
        layoutSingleAlarmItem.setData(arrayListAlarmData.get(position), position);
        return layoutSingleAlarmItem;
    }

    LinearLayoutSingleAlarmItem.OnRemoveButtonClickListener onRemoveButtonClickListener = new LinearLayoutSingleAlarmItem.OnRemoveButtonClickListener() {

        @Override
        public void onClicked(int hh, int mm, String mission, int reqCode, int position) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(mContext, AlarmShowActivity.class);
            PendingIntent pi = PendingIntent.getActivity(mContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pi);
            removeData(position);
        }
    };

}

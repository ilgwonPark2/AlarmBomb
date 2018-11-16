package com.example.ilgwon.alarmbomb;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> mData;
    LayoutInflater mInflate;
    ArrayList<AlarmData> arrayListAlarmDatas;

    public AlarmAdapter(Context context, ArrayList<AlarmData> arrayListAlarmDatas) {
        mContext = context;
        this.arrayListAlarmDatas = arrayListAlarmDatas;
        mInflate = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayListAlarmDatas.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return arrayListAlarmDatas.get(position).reqCode;
    }

    public boolean removeData(int position) {
        arrayListAlarmDatas.remove(position);
        notifyDataSetChanged();
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayoutSingleAlarmItem layoutSingleAlarmItem = (LinearLayoutSingleAlarmItem) convertView;

        if (layoutSingleAlarmItem == null) {
            layoutSingleAlarmItem = new LinearLayoutSingleAlarmItem(mContext);
            layoutSingleAlarmItem.setOnRemoveButtonClickListener(onRemoveButtonClickListener);
        }
        layoutSingleAlarmItem.setData(arrayListAlarmDatas.get(position), position);
        return layoutSingleAlarmItem;
    }
//    LinearLayoutSingleAlarmItem.OnRemoveButtonClickListner on
    LinearLayoutSingleAlarmItem.OnRemoveButtonClickListener onRemoveButtonClickListener = new LinearLayoutSingleAlarmItem.OnRemoveButtonClickListener() {

        @Override
        public void onClicked(int hh, int mm, int reqCode, int position) {
            Toast.makeText(mContext, "position : " + position + " reqCode :" + reqCode, 0).show();
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            //			    Intent i = new Intent(mContext ,AlarmTestForHaruActivity.class);
            Intent intent = new Intent(mContext, AlarmShowActivity.class);
            Toast.makeText(mContext, "reqCode : " + reqCode, 0).show();
            PendingIntent pi = PendingIntent.getActivity(mContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pi);
            removeData(position);
        }
    };

}

package com.example.ilgwon.alarmbomb.module_alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.ilgwon.alarmbomb.user_interface.AlarmShowActivity;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflate;
    ArrayList<AlarmData> arrayListAlarmData;
    SharedPreferences sharedPref;

    public AlarmAdapter(Context context, ArrayList<AlarmData> arrayListAlarmData, SharedPreferences sharedPreferences) {
        // initialize data
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

    /**
     * Remove an alarm item.
     */
    public boolean removeData(int position) {
        //  Remove an alarm item from the list
        arrayListAlarmData.remove(position);
        notifyDataSetChanged();
        int size = sharedPref.getInt("size", 0);
        //  remove an alarm item from the SharedPreferences with editor and commit.
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
        AlarmSingleItem alarmSingleItemLayout = (AlarmSingleItem) convertView;
        if (alarmSingleItemLayout == null) {
            alarmSingleItemLayout = new AlarmSingleItem(mContext);
            alarmSingleItemLayout.setOnRemoveButtonClickListener(onRemoveButtonClickListener);
        }
        alarmSingleItemLayout.setData(arrayListAlarmData.get(position), position);
        return alarmSingleItemLayout;
    }

    /**
     * Each AlarmSingleItem has an OnRemoveButtonClickListener.
     */
    AlarmSingleItem.OnRemoveButtonClickListener onRemoveButtonClickListener = new AlarmSingleItem.OnRemoveButtonClickListener() {

        @Override
        public void onClicked(int hh, int mm, String mission, int reqCode, int position) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            //  Cancel the PendingIntent since it is stored in this intent.
            Intent intent = new Intent(mContext, AlarmShowActivity.class);
            PendingIntent pi = PendingIntent.getActivity(mContext, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pi);
            //  call remove data method.
            removeData(position);
        }
    };

}

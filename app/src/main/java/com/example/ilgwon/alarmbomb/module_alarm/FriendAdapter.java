package com.example.ilgwon.alarmbomb.module_alarm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    final ArrayList<FriendSingleItem> frienddata=new ArrayList<FriendSingleItem>();
    Context mContext;

    @Override
    public int getCount() {
        return frienddata.size();
    }

    @Override
    public Object getItem(int position) {
        return frienddata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        FriendItemView friendItemView=(FriendItemView)convertview;
        if (friendItemView == null) {
            friendItemView = new FriendItemView(mContext);
        }
        return convertview;

    }
}

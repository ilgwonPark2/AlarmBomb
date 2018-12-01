package com.thenerds.ilgwon.alarmbomb.module_alarm;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thenerds.ilgwon.alarmbomb.R;

public class FriendItemView extends LinearLayout {
    TextView tv;
    public FriendItemView(Context context) {
        super(context);
        init(context);
    }


    public FriendItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.single_find_friend,this,true);
        tv=(TextView)findViewById(R.id.friend);

    }
    public void friend_data(String friend){
        tv.setText(friend);
    }
}

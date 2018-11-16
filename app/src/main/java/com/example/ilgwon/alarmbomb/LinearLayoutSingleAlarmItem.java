package com.example.ilgwon.alarmbomb;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LinearLayoutSingleAlarmItem extends LinearLayout {
    Context mContext;
    TextView textViewTime;
    Button btnSingleAlarmItemCancel;

    AlarmData alarmData;
    private int position;

    public LinearLayoutSingleAlarmItem(Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_single_alarm_item, this);
        textViewTime = (TextView) layout.findViewById(R.id.textViewTime);
        btnSingleAlarmItemCancel = (Button) findViewById(R.id.btnSingleAlarmItemCancel);

        btnSingleAlarmItemCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onRemoveButtonClickListener != null)
                    onRemoveButtonClickListener.onClicked(alarmData.hh, alarmData.mm, alarmData.reqCode, position);
            }
        });
    }

    public interface OnRemoveButtonClickListener {
        void onClicked(int hh, int mm, int reqCode, int position);
    }

    OnRemoveButtonClickListener onRemoveButtonClickListener;

    void setOnRemoveButtonClickListener(OnRemoveButtonClickListener OnRemoveButtonClickListener) {
        this.onRemoveButtonClickListener = OnRemoveButtonClickListener;
    }

    public boolean setData(AlarmData alarmData, int position) {

        this.alarmData = alarmData;
        this.position = position;

        textViewTime.setText(alarmData.hh + ":" + alarmData.mm + " and requestCode : " + alarmData.reqCode);

        return true;
    }


}

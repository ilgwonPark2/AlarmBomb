package com.example.ilgwon.alarmbomb.module_alarm;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ilgwon.alarmbomb.R;

public class AlarmSingleItem extends LinearLayout {
    Context mContext;
    TextView textViewTime;
    TextView missionTextView;
    ImageButton btnSingleAlarmItemCancel;

    AlarmData alarmData;
    private int position;

    /**
     * AlarmSingleItem constructor
     */
    public AlarmSingleItem(Context context) {
        super(context);
        mContext = context;
        //  make an instance and inflate to the view.
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.layout_single_alarm_item, this);
        textViewTime = layout.findViewById(R.id.textViewTime);
        missionTextView = layout.findViewById(R.id.missionTextView);
        //  add setOnClickListener to the btnSingleAlarmItemCancel
        btnSingleAlarmItemCancel = findViewById(R.id.btnSingleAlarmItemCancel);
        btnSingleAlarmItemCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onRemoveButtonClickListener != null)
                    onRemoveButtonClickListener.onClicked(alarmData.hh, alarmData.mm, alarmData.mission, alarmData.reqCode, position);
            }
        });
    }

    /**
     * onRemoveButtonClickListener interface
     */
    public interface OnRemoveButtonClickListener {
        void onClicked(int hh, int mm, String mission, int reqCode, int position);
    }

    OnRemoveButtonClickListener onRemoveButtonClickListener;
    void setOnRemoveButtonClickListener(OnRemoveButtonClickListener onRemoveButtonClickListener) {
        this.onRemoveButtonClickListener = onRemoveButtonClickListener;
    }

    /**
     * setData to the AlarmData
     */
    public boolean setData(AlarmData alarmData, int position) {
        this.alarmData = alarmData;
        this.position = position;
        textViewTime.setText(alarmData.Alarm_time());
        missionTextView.setText(alarmData.Alarm_mission());
        return true;
    }


}

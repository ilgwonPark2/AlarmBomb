package com.thenerds.ilgwon.alarmbomb.module_alarm;

public class AlarmData {
    public int hh;
    public int mm;
    public int reqCode;
    public String mission;

    public AlarmData(int hh, int mm, String mission, int reqCode) {
        this.hh = hh;
        this.mm = mm;
        this.reqCode = reqCode;
        this.mission = mission;
    }


    public String Alarm_time() {
        return hh + " : " + mm;
    }

    public String Alarm_mission() {
        return "Mission: " + mission;
    }

}



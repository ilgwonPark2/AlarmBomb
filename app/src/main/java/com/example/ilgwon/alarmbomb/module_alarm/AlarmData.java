package com.example.ilgwon.alarmbomb.module_alarm;

public class AlarmData {
    public int hh;
    public int mm;
    public int reqCode;

    public AlarmData(int hh, int mm, int reqCode) {
        this.hh = hh;
        this.mm = mm;
        this.reqCode = reqCode;
    }

    @Override
    public String toString() {
        return hh+":"+mm +" and requestCode : "+reqCode;
    }
}



package com.tcl.example.com.tcl.dejun.xie.xmlResource;

/**
 * Created by dejun.xie on 2016/8/16.
 */
public class SetTime {

    int setHour;
    int setMinute;

    public SetTime(int setHour, int setMinute) {
        this.setHour = setHour;
        this.setMinute = setMinute;
    }

    public int getSetHour() {
        return setHour;
    }

    public void setSetHour(int setHour) {
        this.setHour = setHour;
    }

    public int getSetMinute() {
        return setMinute;
    }

    public void setSetMinute(int setMinute) {
        this.setMinute = setMinute;
    }
}

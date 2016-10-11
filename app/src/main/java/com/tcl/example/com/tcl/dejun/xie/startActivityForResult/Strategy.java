package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.view.MotionEvent;

/**
 * Created by dejun.xie on 2016/5/23.
 */
public interface Strategy {

    void down(MotionEvent event);
    void move(MotionEvent event);
    void up(MotionEvent event);

    void setStrategyContext(StrategyContext strategyContext);
}

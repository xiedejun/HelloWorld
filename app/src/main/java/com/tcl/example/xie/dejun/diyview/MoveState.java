package com.tcl.example.xie.dejun.diyview;

import android.view.View;

/**
 * Created by dejun.xie on 2016/5/24.
 */
public abstract class MoveState{
    View headerView;

    public MoveState(View headerView) {
        this.headerView = headerView;
    }

    abstract void handleMove(int deltaY);
}

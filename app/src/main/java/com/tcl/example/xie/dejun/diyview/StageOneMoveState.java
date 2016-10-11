package com.tcl.example.xie.dejun.diyview;

import android.view.View;

/**
 * Created by dejun.xie on 2016/5/24.
 */
public class StageOneMoveState extends MoveState {

    public StageOneMoveState(View headerView) {
        super(headerView);
    }

    @Override
    public void handleMove(int deltaY) {
        headerView.scrollTo(0, -deltaY);
    }
}

package com.tcl.example.xie.dejun.diyview;

import android.view.View;

/**
 * Created by dejun.xie on 2016/5/24.
 */
public class MoveStateContext{

    public static final int STAGE_ONE=10;
    public static final int STAGE_TWO=20;

    MoveState moveState;
    View headerView;

    int deltaY;

    public MoveStateContext(MoveState moveState) {
        this.moveState = moveState;
    }

    public void requestHandle(int state){
        if(state==STAGE_ONE){
            moveState=new StageOneMoveState(headerView);
        }else if(state==STAGE_TWO){
            moveState=new StageTwoMoveState(headerView);
        }
        moveState.handleMove(deltaY);
    }

    public void init(View headerView) {
        this.headerView=headerView;
    }

    public void setMoveDistance(int deltaY) {
        this.deltaY=deltaY;
    }
}

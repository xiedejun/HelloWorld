package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by dejun.xie on 2016/5/24.
 */

//策略模式上下文，持有具体的策略类，并为其提供所需的数据
public class StrategyContext {

    Strategy mStrategy;

    public int refreshCriticalPx;
    public int headerHeightPx;
    public int scrollRangePx;
    public int bounceBackPx;

    public int[] getHeadViewPos() {
        return headViewPos;
    }

    public void setHeadViewPos(int[] headViewPos) {
        this.headViewPos = headViewPos;
    }

    int[] headViewPos;

    public StrategyContext(Strategy mStrategy) {
        this.mStrategy = mStrategy;
    }

    public int getRefreshCriticalPx() {
        return refreshCriticalPx;
    }

    public int getHeaderHeightPx() {
        return headerHeightPx;
    }

    public int getScrollRangePx() {
        return scrollRangePx;
    }

    public int getBounceBackPx() {
        return bounceBackPx;
    }

    public void initData(int refreshCriticalPx,int headerHeightPx,int scrollRangePx,int bounceBackPx){
        this.refreshCriticalPx=refreshCriticalPx;
        this.headerHeightPx=headerHeightPx;
        this.scrollRangePx=scrollRangePx;
        this.bounceBackPx=bounceBackPx;
        mStrategy.setStrategyContext(this);
    }

//    public void initView(View headerView) {
//        this.headerView=headerView;
//    }

    public void down(MotionEvent event) {
        mStrategy.down(event);
    }

    public void move(MotionEvent event) {
        mStrategy.move(event);
    }

    public void up(MotionEvent event) {
        mStrategy.up(event);
    }
}

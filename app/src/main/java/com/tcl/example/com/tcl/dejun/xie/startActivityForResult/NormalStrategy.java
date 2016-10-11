package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dejun.xie on 2016/5/23.
 */

//具体的策略类之一 —— NormalStrategy
public class NormalStrategy implements Strategy {

    FrameLayout headerView;
    FrameLayout contentView;
    RefreshView refreshView;
    StrategyContext strategyContext;

    int downX,downY,currentX,currentY;
    int deltaX,deltaY,tempdeltaX,tempdeltaY;
    int tempX,tempY;
    int bottomRange;

    ImageView ivIndicateRotate;
    ProgressBar progressBar;
    TextView tvIndicator;
    TextView tvTime;

    public NormalStrategy(FrameLayout headerView, FrameLayout contentView) {
        this.headerView = headerView;
        this.contentView = contentView;
    }

    public NormalStrategy(RefreshView refreshView) {
        this.refreshView=refreshView;
        initView();
    }

    private void initView() {
        ivIndicateRotate= (ImageView) refreshView.findViewById(R.id.iv_indicate_rotate);
        progressBar= (ProgressBar) refreshView.findViewById(R.id.progress_bar);
        tvIndicator= (TextView) refreshView.findViewById(R.id.tv_indicator);
        tvTime= (TextView) refreshView.findViewById(R.id.tv_time);
        //初始化各个view的状态
        ivIndicateRotate.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tvIndicator.setText(RefreshView.PULL_TO_REFRESH);
    }

    private String getTime() {
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateformat.format(new Date());
    }

    @Override
    public void down(MotionEvent event) {
        downX= (int) event.getX();
        downY= (int) event.getY();
        tvTime.setText("更新于："+getTime());
    }

    @Override
    public void move(MotionEvent event) {
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        deltaY=currentY-downY;
        deltaX=currentX-downX;

        if(Math.abs(deltaY)>Math.abs(deltaX)){
            //未超过headerView的高度
            if(deltaY<=strategyContext.getHeaderHeightPx() && deltaY>0){
                refreshView.scrollTo(0, -deltaY);
                viewStateChange(RefreshView.DOWN_PULL_REFRESH);
            }else{//超过headerView的高度
            }
        }

    }

    private void viewStateChange(int refreshState) {
        switch(refreshState){
            case RefreshView.DOWN_PULL_REFRESH:
                break;
            case RefreshView.LOOSEN_REFRESH:
                break;
            case RefreshView.LOADING:
                break;
        }
    }

    @Override
    public void up(MotionEvent event) {

        if(deltaY>0){
            //收起动画
            if(deltaY<=strategyContext.getHeaderHeightPx()){
                Log.d("XDJ","收起");
            }else{//刷新中动画
                Log.d("XDJ","刷新中");
            }
        }
    }

    @Override
    public void setStrategyContext(StrategyContext strategyContext) {
        this.strategyContext=strategyContext;
    }


}

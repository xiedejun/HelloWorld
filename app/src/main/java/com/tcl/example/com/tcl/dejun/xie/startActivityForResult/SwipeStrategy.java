package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tcl.example.xie.dejun.diyview.MoveState;
import com.tcl.example.xie.dejun.diyview.MoveStateContext;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.Arrays;

/**
 * Created by dejun.xie on 2016/5/23.
 */
//具体的策略类之一 —— SwipeStrategy
public class SwipeStrategy implements Strategy {

    FrameLayout headerView;
    View headContent;
    StrategyContext strategyContext;

    int downX,downY,currentX,currentY;
    int deltaX,deltaY,tempdeltaX,tempdeltaY;
    int tempX,tempY;
    int bottomRange;
    private final int ANIM_DURATION=500;

    public SwipeStrategy(FrameLayout headerView) {
        this.headerView = headerView;
        headContent=headerView.findViewById(R.id.header_view_ll);
    }

    @Override
    public void down(MotionEvent event) {
        downX= (int) event.getX();
        downY= (int) event.getY();
//        Log.d("XDJ","headerView.getScrollY()=" +headerView.getScrollY());
    }

    @Override
    public void move(MotionEvent event) {
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        deltaY=currentY-downY;
        deltaX=currentX-downX;
        //超过可滑动的距离范围，则回弹
        if(Math.abs(deltaY)>Math.abs(deltaX) && deltaY>=strategyContext.getScrollRangePx()){
            bounceBack(deltaY);
        }
        //未超过可滑动范围
        else if(Math.abs(deltaY)>Math.abs(deltaX) && deltaY>0 && deltaY<=strategyContext.getScrollRangePx()){
            headerView.scrollTo(0, -deltaY);
        }
    }

    @Override
    public void up(MotionEvent event) {
        //超过临界点，开启回弹动画
        if(deltaY>strategyContext.getRefreshCriticalPx()){
            bounceBack(deltaY);
        }else{ //未超过临界点，开启消失动画
            disappear();
        }
    }

    private void disappear() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(ANIM_DURATION);
//        alphaAnimation.setFillAfter(true);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束后headerView回至初始位置
                headerView.clearAnimation();
                if(deltaY>0){
                    headerView.scrollTo(0, 0);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        headerView.startAnimation(alphaAnimation);
    }

    private void bounceBack(final int deltaY) {
        Log.d("XDJ", "bounceBack()");
        ObjectAnimator oa= ObjectAnimator.ofFloat(headerView, "translationY", 0,strategyContext.getBounceBackPx()-deltaY);
        oa.setDuration(ANIM_DURATION);
        oa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                headerView.clearAnimation();
                headerView.scrollTo(0, strategyContext.getBounceBackPx() - deltaY);
                headerView.setTranslationY(0);//设置headerView归位至0
                headerView.setScrollY(0);//设置headerView的内容至其顶部
                Log.d("XDJ", "headerView.getTop()=" + headerView.getTop()+",headerView.getBottom()="+headerView.getBottom());
//                headContent.scrollTo(0,0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        oa.start();
    }

    @Override
    public void setStrategyContext(StrategyContext strategyContext) {
        this.strategyContext=strategyContext;
        bottomRange=strategyContext.getScrollRangePx();
    }
}

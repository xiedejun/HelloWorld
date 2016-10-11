package com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/19.
 */
public class PorterDuffActivity_2 extends Activity {

    private LinearLayout llRoot;
    private WindowManager windowManager;
    private Context mContext;
    private DisplayMetrics displayMetrics;
    private PorterDuffView_2 porterDuffView_2;
    private AnimationSet animationSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff_2);
        initData(this);
        findViews();
        initListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void initListeners() {
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                porterDuffView_2.setCanDraw(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                porterDuffView_2.setCanDraw(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initData(Context context) {
        mContext=context;
        //获取屏幕宽高
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    private void findViews() {
        llRoot = (LinearLayout) findViewById(R.id.ll_root);
        porterDuffView_2 = (PorterDuffView_2) findViewById(R.id.iv_scratch);
        setChildViewTransition(llRoot);
    }

    //设置过渡动画
    private void setChildViewTransition(LinearLayout llRoot) {

        //缩放动画
//        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,
//                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
//        scaleAnimation.setDuration(2000);
//        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(scaleAnimation,0.5f);
//        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        llRoot.setLayoutAnimation(layoutAnimationController);

//        平移动画
//        TranslateAnimation translateAnimation=new TranslateAnimation(-porterDuffView_2.getmWidth()-(displayMetrics.widthPixels-porterDuffView_2.getmWidth())/2,0,0,0);
//        translateAnimation.setInterpolator(new BounceInterpolator());
//        translateAnimation.setDuration(1000);
//        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(translateAnimation,0.1f);
//        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        llRoot.setLayoutAnimation(layoutAnimationController);

        //透明度动画
//        AlphaAnimation alphaAnimation=new AlphaAnimation(0.3f,1.0f);
//        alphaAnimation.setDuration(1000);
//        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(alphaAnimation,0.5f);
//        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_NORMAL);
//        llRoot.setLayoutAnimation(layoutAnimationController);

//        动画合集---旋转+透明度+缩放
        RotateAnimation rotateAnimation=new RotateAnimation(0.0f,360.0f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(1000);
        animationSet.setInterpolator(new DecelerateInterpolator());
        LayoutAnimationController layoutAnimationController=new LayoutAnimationController(animationSet,0.5f);
        layoutAnimationController.setOrder(LayoutAnimationController.ORDER_REVERSE);
        llRoot.setLayoutAnimation(layoutAnimationController);
    }
}

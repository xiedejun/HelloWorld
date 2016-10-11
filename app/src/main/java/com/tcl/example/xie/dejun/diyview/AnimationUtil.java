package com.tcl.example.xie.dejun.diyview;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ProgressBar;

/**
 * Created by dejun.xie on 2016/2/24.
 */
public class AnimationUtil {

    public static final int DURATION_TIME=500;
    public static void rotateClockwise(View v){
        RotateAnimation ra=new RotateAnimation(-180,-360,v.getWidth()/2,v.getHeight()/2);
        ra.setDuration(DURATION_TIME);
        ra.setFillAfter(true);
        v.startAnimation(ra);
    }

    public static void rotateAntiClockwise(View v){
        RotateAnimation ra=new RotateAnimation(0,-180,v.getWidth()/2,v.getHeight()/2);
        ra.setDuration(DURATION_TIME);
        ra.setFillAfter(true);
        v.startAnimation(ra);
    }

    public static void pbRotate(ProgressBar pb){
        RotateAnimation ra=new RotateAnimation(0,360,pb.getWidth()/2,pb.getHeight()/2);
        ra.setDuration(DURATION_TIME);
        ra.setFillAfter(true);
        ra.setRepeatMode(Animation.RESTART);
        ra.setRepeatCount(Integer.MAX_VALUE);
        pb.startAnimation(ra);
    }
}

package com.tcl.example.com.tcl.dejun.xie.tintAndClipping;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dejun.xie on 2016/8/25.
 */
public class TintActivity extends Activity implements View.OnClickListener {

    private int off_X;//X轴的偏移量
    private  int off_Y = 0;//Y轴的偏移量
    private ImageView ivCenter;
    private Button btRoundRect;
    private Button btCircle;
    private TextView tvBottom;
    private boolean circleFinished=false;
    private int diameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tint);
        findViews();
        initListeners();
    }

    private void initListeners() {
        btRoundRect.setOnClickListener(this);
        btCircle.setOnClickListener(this);
    }

    private void findViews() {
        ivCenter = (ImageView) findViewById(R.id.iv_center);
        btRoundRect = (Button) findViewById(R.id.bt_round_rect);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);
        btCircle = (Button) findViewById(R.id.bt_circle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_round_rect:
                clipRoundRectView();
                break;
            case R.id.bt_circle:
                if(!circleFinished){
                    translateView(tvBottom);
                }else{
                    clipCircleView(diameter,tvBottom);
                }
                break;
        }
    }

    private void translateView(View view) {
        if(view.getWidth()>view.getHeight()){
            diameter =view.getWidth();
            off_Y=view.getHeight()/2;
        }else {
            diameter =view.getWidth();
            off_X=view.getWidth()/2;
        }
        translateAnim(tvBottom, diameter);
    }

    private void clipRoundRectView() {
        ViewOutlineProvider outlineProvider=new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 20);
            }
        };
        tvBottom.setOutlineProvider(outlineProvider);
    }

    private void translateAnim(final TextView tvBottom, final int diameter) {
        TranslateAnimation translateAnimation=new TranslateAnimation(0,0,0,diameter/2);
        translateAnimation.setDuration(1000);
        translateAnimation.setInterpolator(new BounceInterpolator());
        translateAnimation.setFillAfter(true);
        tvBottom.startAnimation(translateAnimation);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                clipCircleView(diameter, tvBottom);
                circleFinished=true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void clipCircleView(final int diameter, TextView tvBottom) {
        ViewOutlineProvider outlineProvider=new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, -diameter/2+off_Y, diameter, diameter/2+off_Y);
            }
        };
        tvBottom.setOutlineProvider(outlineProvider);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        circleFinished=false;
    }
}

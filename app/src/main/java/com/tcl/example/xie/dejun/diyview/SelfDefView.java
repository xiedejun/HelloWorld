package com.tcl.example.xie.dejun.diyview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dejun.xie on 2015/11/11.
 */
public class SelfDefView extends View {

    Bitmap slideBackground;
    Bitmap switchBackground;
    ToggleState toggleState;
    boolean isSliding=false;
    int currentX;

    public enum ToggleState{
        Open,Close
    }

    public SelfDefView(Context context) {
        super(context);
    }

    //SelfDefView会在xml中使用，故调用该构造方法
    public SelfDefView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelfDefView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置滑动背景
    public void setSlideBackgroud(int slideBackground){
        this.slideBackground=BitmapFactory.decodeResource(getResources(),slideBackground);
    }

    //设置开关背景
    public void setSwitchBackgroud(int switchBackground){
        this.switchBackground=BitmapFactory.decodeResource(getResources(),switchBackground);
    }

    //设置开关状态
    public void setSwitchState(ToggleState toggleState){
        this.toggleState=toggleState;
    }

    //确定在屏幕的绘制区域
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(slideBackground.getWidth(),slideBackground.getHeight());
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制开关背景
        canvas.drawBitmap(slideBackground,0,0,null);
        //绘制滑动开关
        //canvas.drawBitmap(switchBackground,0,0,null);
        int left=currentX-switchBackground.getWidth()/2;
        if(isSliding){
//            int left=currentX-switchBackground.getWidth()/2;
            if(left<0){
                left=0;
//                Log.e("===", "left=" + left);
//                canvas.drawBitmap(switchBackground,left,0,null);
            }

            if(left>slideBackground.getWidth()-switchBackground.getWidth()){
                left=slideBackground.getWidth()-switchBackground.getWidth();
//                Log.e("===", "left=" + left);
//                canvas.drawBitmap(switchBackground,left,0,null);
            }

            canvas.drawBitmap(switchBackground,left,0,null);

        }
        else {
            if(toggleState== ToggleState.Close){
                    canvas.drawBitmap(switchBackground,0,0,null);
            }
            else{
                canvas.drawBitmap(switchBackground,slideBackground.getWidth()-switchBackground.getWidth(),
                        0,null);
            }

        }
    }

    //响应滑动时的处理
    //@Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX= (int) event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                isSliding=true;
            }break;
            case MotionEvent.ACTION_MOVE:{
            }break;
            case MotionEvent.ACTION_UP:{
                isSliding=false;
                if(currentX<slideBackground.getWidth()/2){
                    if(toggleState!= ToggleState.Close){
                        toggleState= ToggleState.Close;
                        //状态改变了才去设置toggleState的监听
                        if(sscListener!=null){
                            sscListener.onToggleStateChange(toggleState);
                        }
                    }
                }
                else{
                    if(toggleState!= ToggleState.Open){
                        toggleState= ToggleState.Open;
                        //状态改变了才去设置toggleState的监听
                        if(sscListener!=null){
                            sscListener.onToggleStateChange(toggleState);
                        }
                    }

                }


            }break;
        }
        invalidate();//刷新UI，调用onDraw()方法
        return true;
    }
    //给外部的使用者提供一个接口
    SwitchStateChangeListener sscListener;
    public void setOnSwitchStateChangeListener(SwitchStateChangeListener sscListener){
        this.sscListener=sscListener;
    }
    public interface SwitchStateChangeListener{
        void onToggleStateChange(ToggleState toggleState);
    }


}

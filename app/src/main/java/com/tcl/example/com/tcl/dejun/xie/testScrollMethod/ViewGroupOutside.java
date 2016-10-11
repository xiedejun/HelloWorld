package com.tcl.example.com.tcl.dejun.xie.testScrollMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by dejun.xie on 2016/7/22.
 */
public class ViewGroupOutside extends FrameLayout{

    private boolean dispatchTouchEvent=true;
    private boolean onInterceptTouchEvent=true;
    private boolean onTouchEvent=true;

    public ViewGroupOutside(Context context) {
        super(context);
    }

    public ViewGroupOutside(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroupOutside(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("XDJ", "ViewGroupOutside dispatchTouchEvent(ev)---" + ev.getAction());
        dispatchTouchEvent = super.dispatchTouchEvent(ev);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d("XDJ", "ViewGroupOutside onInterceptTouchEvent(ev)---"+ev.getAction());
        onInterceptTouchEvent = super.onInterceptTouchEvent(ev);
        return onInterceptTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("XDJ", "ViewGroupOutside onTouchEvent(ev)---"+ event.getAction());
        onTouchEvent = super.onTouchEvent(event);
        Log.d("XDJ","ViewGroupOutside dispatchTouchEvent="+dispatchTouchEvent
        +",onInterceptTouchEvent="+onInterceptTouchEvent
        +",onTouchEvent="+onTouchEvent);
        return onTouchEvent;
    }
}

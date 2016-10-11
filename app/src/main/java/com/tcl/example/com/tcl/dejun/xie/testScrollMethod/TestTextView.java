package com.tcl.example.com.tcl.dejun.xie.testScrollMethod;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by dejun.xie on 2016/7/22.
 */
public class TestTextView extends TextView {

    private boolean dispatchTouchEvent=true;
    private boolean onTouchEvent=true;

    public TestTextView(Context context) {
        super(context);
    }

    public TestTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("XDJ", "TestTextView dispatchTouchEvent()---"+event.getAction());
        dispatchTouchEvent = super.dispatchTouchEvent(event);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("XDJ", "TestTextView onTouchEvent()---"+event.getAction());
        onTouchEvent = super.onTouchEvent(event);
        Log.d("XDJ","TestTextView dispatchTouchEvent="+dispatchTouchEvent
                +",onTouchEvent="+onTouchEvent);
        return onTouchEvent;
    }
}

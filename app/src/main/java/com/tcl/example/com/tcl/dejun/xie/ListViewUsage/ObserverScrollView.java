package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by dejun.xie on 2016/7/30.
 */
public class ObserverScrollView extends HorizontalScrollView {

    Scroller mScroller;
    Context mContext;
    private boolean isMoved=false;
    private DisplayMetrics displayMetrics;
    private WindowManager windowManager;
    public static final int SCROLL_DURATION=600;

    public ObserverScrollView(Context context) {
        super(context);
        init(context);
    }

    public ObserverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ObserverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context){
        mContext=context;
        mScroller=new Scroller(mContext);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    public void beginScroll(int statrX,int deltaX){
//        Log.d("XDJ", "beginScroll()");
        mScroller.startScroll(statrX,0,deltaX,0,SCROLL_DURATION);
        postInvalidate();
    }

    @Override
    public void computeScroll() {

        if (mScroller.computeScrollOffset()) {

            //这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            //必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d("XDJ","onTouchEvent( event )");
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

//        LinearLayout root= (LinearLayout) getChildAt(0);
//        LinearLayout llContent= (LinearLayout) root.getChildAt(0);
//        LinearLayout llDrag= (LinearLayout) root.getChildAt(1);
//        llContent.measure(0, 0);
//        llDrag.measure(0, 0);
//
//        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) llDrag.getLayoutParams();
//        layoutParams.setMargins(displayMetrics.widthPixels - llContent.getMeasuredWidth(), 0, 0, 0);
//        llDrag.setLayoutParams(layoutParams);
//
//        Log.d("XDJ", "llContent.getMeasuredWidth()="+llContent.getMeasuredWidth()+
//        ",displayMetrics.widthPixels - llContent.getMeasuredWidth()=" +
//                (displayMetrics.widthPixels - llContent.getMeasuredWidth())+
//        ",llDrag.getMeasuredWidth()="+llDrag.getMeasuredWidth());

        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void fling(int velocityX) {
//        super.fling(velocityX);
    }
}

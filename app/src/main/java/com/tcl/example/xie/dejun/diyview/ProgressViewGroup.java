package com.tcl.example.xie.dejun.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by dejun.xie on 2016/3/28.
 */
public class ProgressViewGroup extends FrameLayout {

    MyProgressBar myProgressBar;
    ImageView imgCenter;
    RelativeLayout rl;

    {
//        Log.d("XDJ","构造代码块执行了---initItemView()");
        initItemView();
    }

    private void initItemView() {
        View.inflate(getContext(), R.layout.progress_layout, this);
    }

    public ProgressViewGroup(Context context) {
        super(context);
//        Log.d("XDJ","ProgressViewGroup---111");
    }

    public ProgressViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
//        Log.d("XDJ", "ProgressViewGroup---222");
    }

    public ProgressViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        Log.d("XDJ", "ProgressViewGroup---333");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        super.onLayout(changed,l,t,r,b);
        rl.layout(0, 0, r, b);
        Log.d("XDJ", "ViewGroup onLayout()");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("XDJ", "ViewGroup onMeasure()");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        Log.d("XDJ", "onFinishInflate() --- getChildAt(0)=" + getChildAt(0).getClass());
        rl= (RelativeLayout) findViewById(R.id.relativelayout);
        myProgressBar= (MyProgressBar) rl.findViewById(R.id.my_progress);
        imgCenter= (ImageView) rl.findViewById(R.id.iv_center);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

    }
}

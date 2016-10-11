package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/5.
 */
public class LeftDragItemView extends FrameLayout {

    Context context;
    private LeftWrapperItem wrapperItem;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private ViewDragHelper viewDragHelper;
    private int llDragWidth;
    private int deltaX=0;

    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback(){

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==wrapperItem;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            wrapperItem.setmLeft(left);
//            Log.d("XDJ","llDragWidth="+llDragWidth+",left="+left);
//            Log.d("XDJ","clampViewPositionHorizontal layout "
//                    +",leftDragItemView.getLeft()="+getLeft()
//                    +",leftDragItemView.getRight()="+getRight());
            if(left<=0){
                left=0;
            }else if(left>=llDragWidth){
                left=llDragWidth;
            }

            deltaX=left;
            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
//            Log.d("XDJ", "onViewReleased() xvel=" + xvel
//                    + ",minFlingVelocity="+minFlingVelocity
//            +",maxFlingVelocity="+maxFlingVelocity);

            //优先处理fling事件
            //向右fling，显示dragView
            if(!isDragViewShow && xvel>0 && Math.abs(xvel)>minFlingVelocity){
//                Log.d("XDJ","right fling");
                showDragView(releasedChild);
                return;
            }
            //向左fling,隐藏dragView
            else if(isDragViewShow && xvel<0 && Math.abs(xvel)>minFlingVelocity){
//                Log.d("XDJ","left fling");
                hideDragView(releasedChild);
                return;
            }

            if(deltaX<llDragWidth/2){
                hideDragView(releasedChild);
            }else {
                showDragView(releasedChild);
            }

        }

        @Override
        public void onViewDragStateChanged(int state) {
            switch (state){
                case ViewDragHelper.STATE_DRAGGING:
//                    Log.d("XDJ","ViewDragHelper.STATE_DRAGGING");
                    break;
                case ViewDragHelper.STATE_IDLE:
//                    Log.d("XDJ","ViewDragHelper.STATE_IDLE");
                    break;
                case ViewDragHelper.STATE_SETTLING:
//                    Log.d("XDJ","ViewDragHelper.STATE_SETTLING");
                    break;
            }
            super.onViewDragStateChanged(state);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
//            Log.d("XDJ","isMoved()="+isMoved);
            if(isMoved){
                return 1;
            }else {
                return 0;
            }
        }
    };
    private ViewConfiguration viewConfiguration;
    private int minFlingVelocity;
    private int maxFlingVelocity;
    private boolean isDragViewShow=false;
    private TextView tvDelete;
    private TextView tvTop;
    private TextView tvTab;
    private LinearLayout llContent;
    private boolean isMoved;

    public boolean getIsMoved() {
        return isMoved;
    }

    public TextView getTvContent() {
        return wrapperItem.getTvContent();
    }

    public ImageView getIvContent() {
        return wrapperItem.getIvContent();
    }

    private void showDragView(View releasedChild) {
        viewDragHelper.smoothSlideViewTo(releasedChild,llDragWidth,0);
        ViewCompat.postInvalidateOnAnimation(LeftDragItemView.this);
        isDragViewShow=true;
    }

    private void hideDragView(View releasedChild) {
        viewDragHelper.smoothSlideViewTo(releasedChild,0,0);
        ViewCompat.postInvalidateOnAnimation(LeftDragItemView.this);
        isDragViewShow=false;
    }

    public LeftDragItemView(Context context) {
        super(context);
        init(context);
    }

    public LeftDragItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LeftDragItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context=context;
        viewDragHelper=ViewDragHelper.create(this,callback);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        viewConfiguration = ViewConfiguration.get(context);
        minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        maxFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        findViews();
        initListeners();
    }

    private void initListeners() {
        llContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDragViewShow) {
                    hideDragView(wrapperItem);
                } else {
                    clickAnimation(llContent);
                }
            }
        });
    }

    public TextView getTvDelete() {
        return tvDelete;
    }

    public TextView getTvTop() {
        return tvTop;
    }

    public TextView getTvTab() {
        return tvTab;
    }

    private void findViews() {
        wrapperItem = (LeftWrapperItem) findViewById(R.id.wrapper_item);
        wrapperItem.setParentView(this);

        llContent= (LinearLayout) findViewById(R.id.ll_content);

        //llDrag的子View
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvTab = (TextView) findViewById(R.id.tv_tab);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int totalWidth=0;
        int totalHeight=0;

        for(int i=0;i<getChildCount();i++){
            int width=0;
            int height=0;

            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            width=child.getMeasuredWidth();
            height=child.getMeasuredHeight();

            totalWidth +=width;

            if(totalHeight <height){
                totalHeight =height;
            }
        }
//        Log.d("XDJ", "LeftDragItemView onMeasure(),totalWidth=" + totalWidth);

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        wrapperItem.layout(left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.d("XDJ","LeftDragItemView onInterceptTouchEvent ACTION_DOWN");
                isMoved=false;
//                viewDragHelper.shouldInterceptTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.d("XDJ","LeftDragItemView onInterceptTouchEvent ACTION_MOVE");
                isMoved=true;
                break;
            case MotionEvent.ACTION_UP:
//                Log.d("XDJ","LeftDragItemView onInterceptTouchEvent ACTION_UP");
                isMoved=false;
                break;
        }

        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setLlDragWidth(int llDragWidth) {
        this.llDragWidth=llDragWidth;
    }

    private void clickAnimation(View view) {
        if(view!=null){
            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotationX",0.0f,360.0f);
            animator.cancel();
            animator.setDuration(1000);
            animator.start();
        }
    }

    public void backToHide(){
        ViewCompat.offsetLeftAndRight(wrapperItem,-llDragWidth);
        isDragViewShow=false;
    }

//    private void backToHide(View view,int scrollDuration) {
//        if(view!=null){
//            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"tanslationX",0,-llDragWidth);
//            animator.cancel();
//            animator.setDuration(scrollDuration);
//            animator.start();
//            view.setTranslationX(-llDragWidth);
//            isDragViewShow=false;
//        }
//    }
}

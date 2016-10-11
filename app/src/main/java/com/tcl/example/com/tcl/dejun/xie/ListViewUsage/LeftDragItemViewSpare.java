package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/4.
 */
public class LeftDragItemViewSpare extends FrameLayout {

    Context mContext;
    LinearLayout llContent;
    LinearLayout llDrag;
    private int llDragWidth;
    private int llDragHeight;
    private ViewDragHelper viewDragHelper;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private int scrollX;

    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback(){

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.d("XDJ","child==flContent--->"+ (child==llContent));
            Log.d("XDJ","child==flContent--->"+ (child==llDrag));
            return child==llContent || child==llDrag;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

//            if(left>=llDragWidth){
//                left=llDragWidth;
//                if(child==llContent){
////                llDrag.scrollBy(-dx,0);
//                    int l=llDrag.getLeft()+dx;
//                    int t=llDrag.getTop();
//                    int r=llDrag.getRight()+dx;
//                    int b=llDrag.getBottom();
//                    if(l>=0){
//                        l=0;
//                        r=llDragWidth;
//                    }
//
//                    llDrag.layout(l,t,r,b);
//                }else if(child==llDrag){
////                llContent.scrollBy(-dx,0);
//                    int l=llContent.getLeft()+dx;
//                    int t=llContent.getTop();
//                    int r=llContent.getRight()+dx;
//                    int b=llContent.getBottom();
//                    llContent.layout(l,t,r,b);
//                }
//            }
            if(left<=0){
                left=0;
                llDrag.layout(-llDragWidth, 0, 0, llDrag.getBottom());
            }else if(left<llDragWidth){
                if(child==llContent) {
                    int l = llDrag.getLeft() + dx;
                    int r = llDrag.getRight() + dx;
                    int b = llDrag.getBottom();
                    llDrag.layout(l, 0, r, b);
                }
            }else {
                left=llDragWidth;
                llDrag.layout(0, 0, llDragWidth, llDrag.getBottom());
            }

            scrollX=left;

//            Log.d("XDJ","left="+left+",llDragWidth="+llDragWidth);

            return left;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return 0;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.d("XDJ","llDrag.getRight()="+llDrag.getRight());
            //显示 llDrag
            if(llDrag.getRight()>llDragWidth/2){
//                viewDragHelper.smoothSlideViewTo(llDrag,0,llDragWidth);
//                viewDragHelper.smoothSlideViewTo(llContent,llDragWidth,0);
                viewDragHelper.smoothSlideViewTo(LeftDragItemViewSpare.this,llDragWidth-scrollX,0);
                ViewCompat.postInvalidateOnAnimation(LeftDragItemViewSpare.this);
            }
            //隐藏 llDrag
            else {
                viewDragHelper.smoothSlideViewTo(llDrag,-scrollX,0);
//                viewDragHelper.smoothSlideViewTo(llContent,0,0);
//                viewDragHelper.smoothSlideViewTo(LeftDragItemView.this,-scrollX,0);
                ViewCompat.postInvalidateOnAnimation(LeftDragItemViewSpare.this);
            }
        }

    };
//    private FrameLayout root;
    private int totalWidth;
    private int totalHeight;

    public LeftDragItemViewSpare(Context context) {
        super(context);
        initView(context);
    }

    public LeftDragItemViewSpare(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LeftDragItemViewSpare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        mContext=context;
//        addView(View.inflate(mContext, R.layout.left_drag_content, null));
//        addView(View.inflate(mContext, R.layout.letf_drag_view, null));

        viewDragHelper=ViewDragHelper.create(this,callback);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        llContent= (LinearLayout) findViewById(R.id.ll_content);
        llDrag= (LinearLayout) findViewById(R.id.ll_drag);
//        Log.d("XDJ","llContent="+llContent+",llDrag="+llDrag);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        totalWidth = 0;
        totalHeight = 0;

        for(int i=0;i<getChildCount();i++){
            int width=0;
            int height=0;

            View child=getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            width=child.getMeasuredWidth();
            height=child.getMeasuredHeight();
//            Log.d("XDJ","width="+width+",height="+height);

            totalWidth +=width;

            if(totalHeight <height){
                totalHeight =height;
            }
        }

        Log.d("XDJ","totalWidth="+totalWidth+",totalHeight="+totalHeight);
//        setMeasuredDimension(totalWidth, totalHeight);

        setMeasuredDimension(totalWidth, totalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        Log.d("XDJ","right="+right+",bottom="+bottom);

        llDrag.measure(0,0);
        llDragWidth=llDrag.getMeasuredWidth();
        llDragHeight=llDrag.getMeasuredHeight();

        llContent.layout(0,0,right,bottom);
//        llDrag.layout(-llDragWidth,0,left,bottom);
        llDrag.layout(-llDragWidth,0,left,bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
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
}


//            if(i==0){
////                Log.d("XDJ", "displayMetrics.widthPixels=" + displayMetrics.widthPixels);
//                width=MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels,MeasureSpec.EXACTLY);
//                height=MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(mContext,52),MeasureSpec.EXACTLY);
//                child.measure(width, height);
//                Log.d("XDJ", "i="+i+"displayMetrics.widthPixels=" + displayMetrics.widthPixels
//                +"width="+width+",height="+height);
//            }else if(i==1){
////                measureChild(child,widthMeasureSpec,heightMeasureSpec);
////                width=child.getMeasuredWidth();
////                height=child.getMeasuredHeight();
////                Log.d("XDJ","i="+i+",width="+width+",height="+height);
//                width=MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(mContext,200),MeasureSpec.EXACTLY);
//                height=MeasureSpec.makeMeasureSpec(DensityUtil.dp2px(mContext,52),MeasureSpec.EXACTLY);
//                child.measure(width, height);
//                Log.d("XDJ", "i="+i+"displayMetrics.widthPixels=" + displayMetrics.widthPixels
//                        +"width="+width+",height="+height);
//            }

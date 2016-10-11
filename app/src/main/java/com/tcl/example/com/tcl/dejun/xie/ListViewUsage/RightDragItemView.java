package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/7/29.
 */
public class RightDragItemView extends FrameLayout {

    Context context;
    private TextView tvDelete;
    private TextView tvTop;
    private TextView tvTab;
    private int downX;
    private int currentX;
    private int lastCurrentX=0;
    private int upX;
    private LinearLayout llDrag;
    private LinearLayout llContent;
    private int llContenWidth;
    private int llDragWidth;
    private int llContenHeiht;
    private int llDragHeight;
    private ObserverScrollView scrollView;
    private int endDelatX;
    private boolean isScrollViewShow=false;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private VelocityTracker mVelocityTracker;
    private int mMaximumVelocity;
    private int mActivePointerId = INVALID_POINTER;
    private static final int INVALID_POINTER = -1;
    private boolean isClick;
    ImageView iv;
    TextView tv;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private int deltaX;

    public LinearLayout getLlContent() {
        return llContent;
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

    public ImageView getIv() {
        return iv;
    }

    public TextView getTv() {
        return tv;
    }

    public RightDragItemView(Context context) {
        super(context);
        initItemView(context);
    }

    public RightDragItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initItemView(context);
    }

    public RightDragItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initItemView(context);


    }

    private void initItemView(Context context) {
        this.context=context;
//        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.lva_item_1, this);
        View.inflate(context, R.layout.lva_item_1, this);
        //addView(main,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        findViews();
        initData();
        initListeners();
    }

    private void initData() {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    private void initListeners() {

        scrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        Log.d("XDJ", "MotionEvent.ACTION_DOWN");
                        downX = (int) event.getX();
//                        Log.d("XDJ", "downX=" + downX);
                        isClick = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        Log.d("XDJ", "MotionEvent.ACTION_MOVE ");
//                        Log.d("XDJ", "scrollView.getScrollX()=" + scrollView.getScrollX());
                        if (mVelocityTracker == null) {
                            mVelocityTracker = VelocityTracker.obtain();
                        }
                        mVelocityTracker.addMovement(event);
                        isClick = false;

                        currentX= (int) event.getX();
                        deltaX=currentX-lastCurrentX;
                        lastCurrentX=currentX;
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.d("XDJ", "MotionEvent.ACTION_UP");
                        upX = (int) event.getX();
//                        endDelatX = upX - downX;
                        if(isScrollViewShow){
//                            Log.d("XDJ","llDrag.getWidth()="+llDrag.getWidth());
//                            Log.d("XDJ", "scrollView.getScrollX()="+scrollView.getScrollX());
                            endDelatX=llDrag.getWidth()-scrollView.getScrollX();
                        }else{
                            endDelatX=scrollView.getScrollX();
                        }

//                        Log.d("XDJ","endDelatX="+endDelatX);

                        //如果仅仅是点击事件
                        if (isClick) {
                            //如果dragView显示了，则当即隐藏
                            if(isScrollViewShow){
                                hideDragView();
                            }
                            //否则响应点击事件
                            else {
                                clickAnimation(llContent);
                            }

                            return true;
                        }

                        //优先处理Fling事件
                        if (mVelocityTracker == null) {
                            mVelocityTracker = VelocityTracker.obtain();
                        }
                        final VelocityTracker velocityTracker = mVelocityTracker;
                        velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                        int initialVelocity = (int) velocityTracker.getXVelocity(mActivePointerId);
                        if (getChildCount() > 0) {
                            if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
//                                Log.d("XDJ", "fling() deltaX="+deltaX+",isScrollViewShow="+isScrollViewShow);
                                recycleVelocityTracker();

                                //向左Fling且未显示llDrag时
                                if(deltaX<0 && !isScrollViewShow){
                                    showDragView();
//                                    showFullDragView();
//                                    Log.d("XDJ","showDragView()");
                                }
                                //向右Fling且已经显示了llDrag时
                                else if(deltaX>0 && isScrollViewShow){
                                    hideDragView();
//                                    hideFullDragView();
//                                    Log.d("XDJ", "hideDragView()");
                                }else if(deltaX==0){
                                    if(isScrollViewShow){
                                        hideDragView();
                                    }else {
                                        showDragView();
                                    }
                                }
                                return true;
                            }
                        }

                        //ScrollView未被拖出时的滑动处理
                        if (!isScrollViewShow) {
                            //向左滑动的距离超过llDrag宽度的一半，则显示llDrag
                            if (endDelatX > 0) {
                                if (Math.abs(endDelatX) > (llDrag.getWidth()) / 2) {
                                    showDragView();
                                } else {
                                    hideDragView();
                                }
                            }else if(deltaX<0){
//                                Log.d("XDJ","ACTION_UP deltaX<0 showDragView()");
                                showDragView();
                            }
                        }
                        //ScrollView已被拖出时的滑动处理
                        else {
                            //向右滑动的距离超过llDrag宽度的一半，则隐藏llDrag
                            if (endDelatX >= 0) {
                                if (Math.abs(endDelatX) < (llDrag.getWidth()) / 2) {
                                    showDragView();
                                } else {
                                    hideDragView();
                                }
                            }else if(deltaX>=0){
//                                Log.d("XDJ","ACTION_UP deltaX>0 hideDragView()");
                                hideDragView();
                            }
                        }
                        break;
                }
                return false;
            }
        });

        scrollView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("XDJ", "scrollView onClick");
            }
        });
    }

    private void hideFullDragView() {
        scrollView.scrollTo(-llDrag.getWidth(),0);
        isScrollViewShow = false;
    }

    private void showFullDragView() {
        scrollView.scrollTo(llDrag.getWidth(),0);
        isScrollViewShow = true;
    }

    private void hideDragView() {
//        Log.d("XDJ", "隐藏llDrag scrollView.getScrollX()="+scrollView.getScrollX());
        scrollView.beginScroll(scrollView.getScrollX(), -scrollView.getScrollX());
        isScrollViewShow = false;
    }

    private void showDragView() {
//        Log.d("XDJ", "拖出llDragscroll");
        scrollView.beginScroll(scrollView.getScrollX(), llDrag.getWidth() - scrollView.getScrollX());
        isScrollViewShow = true;
    }

    @Override
    protected void onFinishInflate() {
//        Log.d("XDJ", "onFinishInflate()");
        super.onFinishInflate();
    }

    private void findViews() {
        scrollView= (ObserverScrollView) findViewById(R.id.horizontal_scrollView);

        llContent= (LinearLayout) findViewById(R.id.ll_content);
        iv= (ImageView) llContent.findViewById(R.id.iv_left);
        tv= (TextView) llContent.findViewById(R.id.tv_right);

        llDrag = (LinearLayout) findViewById(R.id.ll_drag);
        tvDelete = (TextView) llDrag.findViewById(R.id.tv_delete);
        tvTop = (TextView) llDrag.findViewById(R.id.tv_top);
        tvTab = (TextView) llDrag.findViewById(R.id.tv_tab);
        
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.d("XDJ","onMeasure()");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.d("XDJ", "onLayout() , left=" + left + ",top=" + top +
//                ",right=" + right + ",bottom=" + bottom);
//        scrollView.layout(0,0,right,bottom);

//        llContent.measure(0, 0);
////        llDrag.measure(0, 0);
//
//        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) llDrag.getLayoutParams();
//        layoutParams.setMargins(displayMetrics.widthPixels - llContent.getMeasuredWidth(), 0, 0, 0);
//        llDrag.setLayoutParams(layoutParams);

        super.onLayout(changed, left, top, right, bottom);
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void clickAnimation(View view) {
        if(view!=null){
            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotationX",0.0f,360.0f);
            animator.cancel();
            animator.setDuration(1000);
            animator.start();
        }
    }

    //记录当前RightDragItemView的位置
//    private int position;
//    public void setPosition(int position) {
//        this.position=position;
//    }
//    public int getPosition() {
//        return position;
//    }

    //给外部Adapter的回调接口
    OnRightDragItemDeleteClickListener deleteClickListener;

    public interface OnRightDragItemDeleteClickListener{
        void onItemDelete();
    }

    public void setOnRightDragItemClickListener(OnRightDragItemDeleteClickListener deleteClickListener){
        this.deleteClickListener=deleteClickListener;
    }

    public void backToHide(){
        int width=llDrag.getWidth();
        scrollView.scrollBy(-width, 0);
        isScrollViewShow=false;
    }
}

package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.FragmentManager;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/5/23.
 */
//自定义上拉下拉刷新控件RefreshView,使用前必须做如下配置：
//setContentView(),setHeaderView(),setRefreshType();
// setCurrentState()表示是否允许上拉或者下拉刷新
public class RefreshView extends FrameLayout{

    public static final int LOOSEN_REFRESH=1;//松开刷新
    public static final int UP_PULL_LOAD_MORE=2;//上拉加载更多
    public static final int DOWN_PULL_REFRESH=3;//下拉刷新
    public static final int LOADING=4;//加载中
    public int currentState;

    public static final String PULL_TO_REFRESH="下拉刷新";
    public static final String LOOSEN_TO_REFRESH="松开刷新";
    public static final String REFRESHING="刷新中...";

    public static final int NORMAL_TYPE=10;
    public static final int SWIPE_TYPE=11;
    public int currentType;

    public int getCurrentType() {
        return currentType;
    }

    //设置具体的滑动类型，不同类型的下滑效果不同
    public void setRefreshType(int currentType) {
        if(currentType==SWIPE_TYPE){
            strategyContext=new StrategyContext(new SwipeStrategy(headerView));
        }else{
            strategyContext=new StrategyContext(new NormalStrategy(this));
        }
        strategyContext.initData(getRefreshCriticalPx(), getHeaderHeightPx(), getScrollRangePx(),getBounceFinalPx());
//        strategyContext.initView(headerView);
    }

    //dp值
    public static final int SCROLL_RANGE=400;//可滑动的距离范围
    public static final int REFRESH_CRITICAL=160;//是否要刷新的临界点
    public static final int BOUNCE_FINAL=100;//回弹动画的终点
    public static final int HEADER_HEIGHT=52;//headerView的高度

    //dp值对应的px值
    public int refreshCriticalPx;
    public int headerHeightPx;
    public int scrollRangePx;
    public int bounceBackPx;

    public int getRefreshCriticalPx() {
        return refreshCriticalPx;
    }

    public int getHeaderHeightPx() {
        return headerHeightPx;
    }

    public int getBounceFinalPx() {
        return bounceBackPx;
    }

    public int getScrollRangePx() {
        return scrollRangePx;
    }

    FrameLayout headerView;
    FrameLayout contentView;
    Context mContext;
    StrategyContext strategyContext;

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState=currentState;
    }

    {
        init();
    }

    public RefreshView(Context context) {
        super(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(){
        mContext=getContext();
        headerHeightPx=DensityUtil.dp2px(mContext, HEADER_HEIGHT);
        scrollRangePx=DensityUtil.dp2px(mContext,SCROLL_RANGE);
        refreshCriticalPx=DensityUtil.dp2px(mContext,REFRESH_CRITICAL);
        bounceBackPx=DensityUtil.dp2px(mContext,BOUNCE_FINAL);
//        Log.d("XDJ", "init(Context context)");
    }

    public void setHeaderView(View v) throws Exception {
//        View v= View.inflate(mContext,R.layout.header_view,null);//R.layout.header_view
        if(v==null){
            throw new Exception("HeaderView is null");
        }
//        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                headerHeightPx+scrollRangePx);
//        v.setLayoutParams(layoutParams);
        headerView.addView(v);
//        Log.d("XDJ", "setHeaderView(View v)");
    }

    public void setHeaderView(int res) throws Exception {
        View v= View.inflate(mContext,res,null);//R.layout.header_view
        if(v==null){
            throw new Exception("HeaderView is null");
        }
//        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.MATCH_PARENT);
//        v.setLayoutParams(layoutParams);
        headerView.addView(v);
//        Log.d("XDJ", "setHeaderView(int res)");
    }

    public void setContentView(View v) throws Exception {
        //v= View.inflate(mContext,R.layout.content_view,null);
        if(v==null){
            throw new Exception("ContentView is null");
        }
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        contentView.addView(v);
//        Log.d("XDJ", "setContentView(View v)");

    }

    public void setContentView(int res) throws Exception {
        View v= View.inflate(mContext,res,null);//R.layout.content_view
        if(v==null){
            throw new Exception("ContentView is null");
        }
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(layoutParams);
        contentView.addView(v);
//        Log.d("XDJ", "setContentView(int res)");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
        headerView.layout(0, 0, right, headerHeightPx+scrollRangePx);
        contentView.layout(0, headerHeightPx, right,bottom);
        strategyContext.setHeadViewPos(new int[]{0, 0, right, headerHeightPx+scrollRangePx});
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headerView= (FrameLayout) findViewById(R.id.fl_head);
        contentView= (FrameLayout) findViewById(R.id.fl_content);
//        Log.d("XDJ", "onFinishInflate()");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(currentState==DOWN_PULL_REFRESH || currentState==UP_PULL_LOAD_MORE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                strategyContext.down(event);
                break;
            case MotionEvent.ACTION_MOVE:
                strategyContext.move(event);
                break;
            case MotionEvent.ACTION_UP:
                strategyContext.up(event);
                break;
        }
        return true;
    }
}

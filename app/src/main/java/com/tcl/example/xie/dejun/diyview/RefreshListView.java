package com.tcl.example.xie.dejun.diyview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dejun.xie on 2016/2/23.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    public static final int PULL_TO_REFRESH=1;
    public static final int PUSH_TO_LOAD_MORE=2;
    View headerView;
    View footerView;
    int headerHeight;
    int footerHeight;
    int downY;
    int currentY;
    int deltaY;
    TextView tvNote;
    TextView tvTime;
    ImageView ivIndicator;
    boolean pulled=false;
    boolean pushed=true;
    boolean isRefreshing=false;
    ProgressBar pb_hv;
    ProgressBar pb_fv;
    RefreshListener refreshListener;

    public RefreshListView(Context context) {
        super(context);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView(){
        initHeaderView();
        initFooterView();
        setOnScrollListener(this);
    }

    private void initFooterView() {
        footerView=View.inflate(getContext(),R.layout.footer_view,null);
        pb_fv= (ProgressBar) footerView.findViewById(R.id.progress_bar);
        addFooterView(footerView);
        footerView.measure(0,0);
        footerHeight=footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerHeight, 0, 0);
    }

    private void initHeaderView() {
        headerView= View.inflate(getContext(), R.layout.header_view, null);
        headerView.measure(0,0);
        headerHeight=headerView.getMeasuredHeight();
//        Log.d("XDJ", "headerHeight="+headerHeight);//headerHeight=148
        headerView.setPadding(0,-headerHeight,0,0);
        addHeaderView(headerView);
        tvNote= (TextView) headerView.findViewById(R.id.tv_indicator);
        tvTime=(TextView) headerView.findViewById(R.id.tv_time);
        ivIndicator= (ImageView) headerView.findViewById(R.id.iv_indicate_rotate);
        pb_hv= (ProgressBar) headerView.findViewById(R.id.progress_bar);
        tvTime.setText(getCurrentTime());
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY= (int) ev.getY();//获得View坐标系的Y轴坐标
                break;
            case MotionEvent.ACTION_MOVE:

                //如果当前正在刷新，则不允许滑动
                if(isRefreshing){
//                    deltaY=0;
                    break;
                }

                ivIndicator.setVisibility(View.VISIBLE);
                pb_hv.clearAnimation();
                pb_hv.setVisibility(View.INVISIBLE);
                currentY= (int) ev.getY();
                deltaY=currentY-downY;//滑动的距离
                int paddingTop=-headerHeight+deltaY;
                if(deltaY>0 && getFirstVisiblePosition()==0) {
                    headerView.setPadding(0, paddingTop, 0, 0);
                    if(deltaY>headerHeight){
                        tvNote.setText("松开刷新");
                        if(!pulled){
                            pulled=true;
                            pushed=false;
                            AnimationUtil.rotateAntiClockwise(ivIndicator);
                        }
                    }else{
                        tvNote.setText("下拉刷新");
                        if(!pushed){
                            pushed=true;
                            pulled=false;
                            AnimationUtil.rotateClockwise(ivIndicator);
                        }
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(deltaY>headerHeight){
                    ivIndicator.clearAnimation();//注意：要先清除ivIndicator动画效果
                    ivIndicator.setVisibility(View.INVISIBLE);
                    pb_hv.setVisibility(View.VISIBLE);
                    tvNote.setText("正在加载...");
                    headerView.setPadding(0, 0, 0, 0);
                    isRefreshing=true;
//                    AnimationUtil.pbRotate(pb);

                    refreshListener.onRefresh(PULL_TO_REFRESH);
                }else{
                    headerView.setPadding(0, -headerHeight, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnRefreshListener(RefreshListener refreshListener){
        this.refreshListener=refreshListener;
    }

    public void completeRefresh(int sign){
        if(sign==PULL_TO_REFRESH){
            ivIndicator.setVisibility(View.VISIBLE);
            pb_hv.setVisibility(View.INVISIBLE);
            tvNote.setText("下拉刷新");
            tvTime.setText(getCurrentTime());
            headerView.setPadding(0, -headerHeight, 0, 0);
            isRefreshing=false;
        }
        if(sign==PUSH_TO_LOAD_MORE){
            footerView.setPadding(0,-footerHeight,0,0);
        }
    }

    public String getCurrentTime(){
        SimpleDateFormat format=new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    /**
     * SCROLL_STATE_IDLE:闲置状态，就是手指松开
     * SCROLL_STATE_TOUCH_SCROLL：手指触摸滑动，就是按着来滑动
     * SCROLL_STATE_FLING：快速滑动后松开
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE && getLastVisiblePosition()==getCount()-1){
            footerView.setPadding(0,0,0,0);
            setSelection(getCount());
            refreshListener.onRefresh(PUSH_TO_LOAD_MORE);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}

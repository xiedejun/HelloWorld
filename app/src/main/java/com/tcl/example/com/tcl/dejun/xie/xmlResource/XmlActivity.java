package com.tcl.example.com.tcl.dejun.xie.xmlResource;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.tcl.example.xie.dejun.diyview.R;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/8/11.
 */
public class XmlActivity extends Activity implements RunningClock.TimeChangeListener{

    private RunningClock mRunningClock;
    private View circleProgress;
    private HourAdapter hourAdapter;
    private int firstHourVisibleItem;
    private int firstMinuteVisibleItem;
    private ListView lvHourTime;
    private ListView lvMinuteTime;
    List<String> hourList;
    List<String> minuteList;
    private MinuteAdapter minuteAdapter;

    public static final int INIT_HOUR=4;
    public static final int INIT_MINUTE=19;
    private Button btConfirm;
    private TextView time_1;
    private TextView time_2;
    private TextView time_3;
    private TextView time_4;
    private boolean minuteEnable;
    private boolean hourEnable;

//    private int firstHourvisibleItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml);
        findViews();
        initViews();
        initData();
        initListeners();
    }

    private void initListeners() {
        
        //时针ListView设置监听
        lvHourTime.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
//                        Log.d("XDJ","hourEnable SCROLL_STATE_IDLE");
                        if (needScrollToNext(lvHourTime)) {
//                            lvHourTime.smoothScrollToPosition(firstHourVisibleItem+1);//该方法会有一些误差
                            lvHourTime.setSelection(firstHourVisibleItem + 1);
                        } else {
//                            lvHourTime.smoothScrollToPosition(firstHourVisibleItem);
                            lvHourTime.setSelection(firstHourVisibleItem);
                        }

                        hourEnable=true;
                        if(minuteEnable && hourEnable){
                            btConfirm.setEnabled(true);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
//                        Log.d("XDJ","hourEnable SCROLL_STATE_TOUCH_SCROLL");
                        hourEnable=false;
                        btConfirm.setEnabled(false);
                        break;
                    case SCROLL_STATE_FLING:
//                        Log.d("XDJ","hourEnable SCROLL_STATE_FLING");
                        hourEnable=false;
                        btConfirm.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstHourVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstHourVisibleItem <= INIT_HOUR) {
                    lvHourTime.setSelection(hourList.size() + INIT_HOUR);
                } else if (firstHourVisibleItem + visibleItemCount >= totalItemCount - INIT_HOUR) {
                    lvHourTime.setSelection(firstHourVisibleItem - hourList.size());
                }
                XmlActivity.this.firstHourVisibleItem = firstHourVisibleItem;

                View child_1 = lvHourTime.getChildAt(0);
//                Log.d("XDJ","child_1="+child_1);
                if (child_1 != null) {
                    if (needScrollToNext(lvHourTime)) {
                        for (int i = 0; i < visibleItemCount; i++) {
                            //中间的Item加上突出效果
                            if (i == 2) {
                                TextView tv = (TextView) lvHourTime.getChildAt(i);
                                tv.setAlpha(1.0f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.light_blue));
                            }
                            //其它的Item加上模糊效果
                            else {
                                TextView tv = (TextView) lvHourTime.getChildAt(i);
                                tv.setAlpha(0.5f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.gray));
                            }
                        }
                    } else {
                        for (int i = 0; i < visibleItemCount; i++) {
                            //中间的Item加上突出效果
                            if (i == 1) {
                                TextView tv = (TextView) lvHourTime.getChildAt(i);
                                tv.setAlpha(1.0f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.light_blue));
                            }
                            //其它的Item加上模糊效果
                            else {
                                TextView tv = (TextView) lvHourTime.getChildAt(i);
                                tv.setAlpha(0.5f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.gray));
                            }
                        }
                    }
                }
            }
        });

        //分针ListView设置监听
        lvMinuteTime.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
//                        Log.d("XDJ", "minuteEnable SCROLL_STATE_IDLE");
                        if (needScrollToNext(lvMinuteTime)) {
                            lvMinuteTime.setSelection(firstMinuteVisibleItem + 1);
                        } else {
                            lvMinuteTime.setSelection(firstMinuteVisibleItem);
                        }

                        minuteEnable=true;
                        if(minuteEnable && hourEnable){
                            btConfirm.setEnabled(true);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
//                        Log.d("XDJ","minuteEnable SCROLL_STATE_TOUCH_SCROLL");
                        minuteEnable=false;
                        btConfirm.setEnabled(false);
                        break;
                    case SCROLL_STATE_FLING:
//                        Log.d("XDJ","minuteEnable SCROLL_STATE_FLING");
                        minuteEnable=false;
                        btConfirm.setEnabled(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstMinuteVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d("XDJ","onScroll()");
                if (firstMinuteVisibleItem <= INIT_MINUTE) {
                    lvMinuteTime.setSelection(minuteList.size() + INIT_MINUTE);
                } else if (firstMinuteVisibleItem + visibleItemCount >= totalItemCount - INIT_MINUTE) {
                    lvMinuteTime.setSelection(firstMinuteVisibleItem - minuteList.size());
                }
                XmlActivity.this.firstMinuteVisibleItem = firstMinuteVisibleItem;

                View child_1 = lvMinuteTime.getChildAt(0);
//                Log.d("XDJ","lvMinuteTime child_1="+child_1);
                if (child_1 != null) {
                    if (needScrollToNext(lvMinuteTime)) {
                        for (int i = 0; i < visibleItemCount; i++) {
                            //中间的Item加上突出效果
                            if (i == 2) {
                                TextView tv = (TextView) lvMinuteTime.getChildAt(i);
                                tv.setAlpha(1.0f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.light_blue));
                            }
                            //其它的Item加上模糊效果
                            else {
                                TextView tv = (TextView) lvMinuteTime.getChildAt(i);
                                tv.setAlpha(0.5f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.gray));
                            }
                        }
                    } else {
                        for (int i = 0; i < visibleItemCount; i++) {
                            //中间的Item加上突出效果
                            if (i == 1) {
                                TextView tv = (TextView) lvMinuteTime.getChildAt(i);
                                tv.setAlpha(1.0f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.light_blue));
                            }
                            //其它的Item加上模糊效果
                            else {
                                TextView tv = (TextView) lvMinuteTime.getChildAt(i);
                                tv.setAlpha(0.5f);
                                tv.setTextColor(XmlActivity.this.getResources().getColor(R.color.gray));
                            }
                        }
                    }
                }
            }
        });

        //“确认”按钮的点击事件
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvHour = (TextView) lvHourTime.getChildAt(1);
                TextView tvMinute = (TextView) lvMinuteTime.getChildAt(1);
//                Log.d("XDJ","tvHour.getText()="+tvHour.getText());
//                Log.d("XDJ","tvMinute.getText()="+tvMinute.getText());
                String setHour = tvHour.getText().toString();
                String setMinute = tvMinute.getText().toString();
                mRunningClock.setTime(setHour, setMinute);
            }
        });

        mRunningClock.setTimeChangeListener(this);
    }

    //判断是否移出下一个时间点
    private boolean needScrollToNext(ListView lv) {
        View v=lv.getChildAt(0);

        if(Math.abs(v.getTop())<v.getHeight()/2){
//            Log.d("XDJ","众神归位");
            return false;
        }else{
//            Log.d("XDJ","显山露水");
            return true;
        }
    }

    private void initData() {

        //初始化时针的数据
        hourList=new ArrayList<>();
        for(int i=0;i<24;i++){
            if(i<10){
                hourList.add("0"+String.valueOf(i)+" 时");
            }else {
                hourList.add(String.valueOf(i)+" 时");
            }
        }

        //初始化分针的数据
        minuteList=new ArrayList<>();
        for(int i=0;i<60;i++){
            if(i<10){
                minuteList.add("0"+String.valueOf(i)+" 分");
            }else {
                minuteList.add(String.valueOf(i)+" 分");
            }
        }

        //初始化时针的ListView
        hourAdapter = new HourAdapter(this,hourList);
        lvHourTime.setAdapter(hourAdapter);
//        lvHourTime.setSelection(3);

        //初始化分针的ListView
        minuteAdapter = new MinuteAdapter(this,minuteList);
        lvMinuteTime.setAdapter(minuteAdapter);
//        lvMinuteTime.setSelection(18);

        //初始化数字钟的时间
        time_1.setText(getDigitFirst(mRunningClock.getmSetHour()));
        time_2.setText(getDigitSecond(mRunningClock.getmSetHour()));
        time_3.setText(getDigitFirst(mRunningClock.getmSetMinute()));
        time_4.setText(getDigitSecond(mRunningClock.getmSetMinute()));
    }

    private String getDigitSecond(String s) {
        if(s.length()==1){
            return s.substring(0,1);
        }else{
            return s.substring(1,2);
        }
    }

    private String getDigitFirst(String s) {
        if(s.length()==1){
            return "0";
        }else{
            return s.substring(0,1);
        }
    }

    private void initViews() {
        circleProgress.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_anim));
//        mRunningClock.startRun();
    }

    private void findViews() {
        circleProgress = findViewById(R.id.circle_progress);
        mRunningClock = (RunningClock) findViewById(R.id.running_clock);
        lvHourTime = (ListView) findViewById(R.id.lv_hour_time);
        lvMinuteTime = (ListView) findViewById(R.id.lv_minute_time);
        btConfirm = (Button) findViewById(R.id.bt_confirm);

        //数字时钟的四个数字
        time_1 = (TextView) findViewById(R.id.time_1);
        time_2 = (TextView) findViewById(R.id.time_2);
        time_3 = (TextView) findViewById(R.id.time_3);
        time_4 = (TextView) findViewById(R.id.time_4);
    }

    @Override
    public void timeChanged(int hour, int minute) {
        //时间改变时重置数字钟的时间
        if("6".equals(getDigitFirst(mRunningClock.getmSetMinute()))
                && "0".equals(getDigitSecond(mRunningClock.getmSetMinute()))){
            time_3.setText("0");
            time_4.setText("0");
            if("2".equals(getDigitFirst(mRunningClock.getmSetHour())) &&
                    "4".equals(getDigitSecond(mRunningClock.getmSetHour()))){
                time_1.setText("0");
                time_2.setText("0");
            }else {
                time_1.setText(getDigitFirst(mRunningClock.getmSetHour()));
                time_2.setText(getDigitSecond(mRunningClock.getmSetHour()));
            }
        }else{
            time_1.setText(getDigitFirst(mRunningClock.getmSetHour()));
            time_2.setText(getDigitSecond(mRunningClock.getmSetHour()));
            time_3.setText(getDigitFirst(mRunningClock.getmSetMinute()));
            time_4.setText(getDigitSecond(mRunningClock.getmSetMinute()));
        }
    }
}

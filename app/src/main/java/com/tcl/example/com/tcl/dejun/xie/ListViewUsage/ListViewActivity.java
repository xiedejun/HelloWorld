package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/7/25.
 */
public class ListViewActivity extends Activity {

    private ListView lv;
    public final int LV_SIZE=30;
    private List<ItemData> list;
    private Resources resources;
    private MyLvAdapter myLvAdapter;
    private TextView tv;
    private Context context;
    private int downY;
    private int currentY;
    private int lastCurrentY;
    private boolean upMove;
    private boolean hasFling;
    private int add=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        findViews();
        initData();
        initListView();
        initListeners();
    }

    private void findViews() {
        tv = (TextView) findViewById(R.id.tv_empty_listView);
    }

    private void initListeners() {

        //添加一个Item
        findViewById(R.id.bt_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()>50){
                    ToastUtils.showMessage(context,"It can hold 50 items most!",
                            Toast.LENGTH_SHORT);
                    return;
                }
                String fileName="nba";
                int drawableId=resources.getIdentifier(fileName,"drawable","com.tcl.example.xie.dejun.diyview");
                ItemData itemData=new ItemData();
                itemData.setDrawableId(drawableId);
                itemData.setFileName(fileName);
                itemData.setType(add++ % 2);
                list.add(itemData);
                myLvAdapter.notifyDataSetChanged();
                lv.smoothScrollToPosition(list.size()-1);
            }
        });

        //移除最后一个Item
        findViewById(R.id.bt_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size()!=0){
                    int last=list.size()-1;
                    list.remove(last);
                    myLvAdapter.notifyDataSetChanged();
                    lv.smoothScrollToPosition(last - 1);
                }else{
//                    Toast.makeText(ListViewActivity.this,
//                      "No Item can be removed!",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //设置ListView的滑动监听-1
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
//                        Log.d("XDJ","listView ACTION_DOWN");
                        downY = (int) event.getY();
                        lastCurrentY = (int) event.getY();
                        upMove=false;
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        Log.d("XDJ","listView ACTION_MOVE");
                        currentY = (int) event.getY();
                        lastCurrentY=downY;
                        int deltaY=currentY-lastCurrentY;
                        if(deltaY<0){
                            upMove=true;
                        }else{
                            upMove=false;
                        }

                        //到达listView的第一项的头部，给出提示
                        if(lv.getFirstVisiblePosition()==0){
                            View firstView=lv.getChildAt(0);
                            if(firstView!=null){
                                if(firstView.getTop()==0 && !upMove){
                                    ToastUtils.showMessage(context,"arrived to top!",Toast.LENGTH_SHORT);
                                }
                            }
                        }

                        //到达listView的最后一项的底部，给出提示
                        if(lv.getLastVisiblePosition()==list.size()-1){
                            int visibleItemCount=lv.getLastVisiblePosition()-lv.getFirstVisiblePosition()+1;
                            View lastView=lv.getChildAt(visibleItemCount-1);
                            if(lastView!=null){
                                int lastTop=lastView.getHeight()+lastView.getTop();
                                int lvBottom=lv.getBottom();
                                if(lastTop==lvBottom && upMove){
                                    ToastUtils.showMessage(context,"arrived to bottom!",Toast.LENGTH_SHORT);
                                }
                            }
                        }

                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.d("XDJ","listView ACTION_UP");
                        break;
                }
                return false;
            }
        });

        //设置ListView的滑动监听-2
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_TOUCH_SCROLL:
//                        Log.d("XDJ","SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    case SCROLL_STATE_IDLE:
//                        Log.d("XDJ","SCROLL_STATE_IDLE");
                        if(hasFling){
//                            Log.d("XDJ","onScroll(),lv.getLastVisiblePosition()="
//                                    +lv.getLastVisiblePosition()+",getFirstVisiblePosition()="+
//                                    lv.getFirstVisiblePosition());
                            if(lv.getLastVisiblePosition()==list.size()-1){
                                ToastUtils.showMessage(context,"arrived to bottom!",Toast.LENGTH_SHORT);
                            }else if(lv.getFirstVisiblePosition()==0){
                                ToastUtils.showMessage(context,"arrived to top!",Toast.LENGTH_SHORT);
                            }
                        }
                        hasFling =false;
                        break;
                    case SCROLL_STATE_FLING:
//                        Log.d("XDJ","SCROLL_STATE_FLING");
                        hasFling =true;
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                Log.d("XDJ","onScroll()");
            }
        });

        //点击某个Item的回调
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                clickAnimation(view);
//            }
//        });

    }

    private void clickAnimation(View view) {
        if(view!=null){
            ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotationX",0.0f,360.0f);
            animator.cancel();
            animator.setDuration(1000);
            animator.start();
        }
    }

    private void initData() {
        context=this;

        list = new ArrayList<>();
        resources = getResources();

        for(int i=0;i<LV_SIZE;i++){
            String fileName="nba"+(i+1);
            //根据图片名获取drawable目录下的图片资源
            int drawableId= resources.getIdentifier(fileName, "drawable", "com.tcl.example.xie.dejun.diyview");
//            Log.d("XDJ","i="+i+",drawableId="+drawableId);
            ItemData itemData=new ItemData();
            itemData.setFileName(fileName);
            itemData.setDrawableId(drawableId);
            itemData.setType(i % 2);
            list.add(itemData);
        }
    }

    private void initListView() {
        lv = (ListView) findViewById(R.id.lv);
        myLvAdapter = new MyLvAdapter(this,list);
        lv.setAdapter(myLvAdapter);
//        lv.setSelection(list.size()/2);
        lv.setEmptyView(tv);
    }
}

package com.tcl.example.com.tcl.dejun.xie.mainActivity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.MainThread;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.ProgressViewGroup;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity{

    ListView listView;
    ListViewAdapter listViewAdapter;
    List<ButtonBean> demoNames;
    Context mContext;
    Intent intent;
    int index=0;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
//        ProgressViewGroup progressViewGroup= (ProgressViewGroup) findViewById(R.id.diy_progress_bar);

        setContentView(R.layout.activity_choose);

        mContext=this;
        initData();

        if(demoNames.size()!=0){
            listView= (ListView) findViewById(R.id.lv);
            listViewAdapter=new ListViewAdapter();
            listView.setAdapter(listViewAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("XDJ","parent="+parent+",view="+view+",position="+position);
                }
            });
        }

//        index++;
//        Log.d("XDJ","index="+index);
    }

    private void initData() {
        demoNames=new ArrayList<>();
        demoNames.add(new ButtonBean("startActivityForResult","com.tcl.example.com.tcl.dejun.xie.startActivityForResult.ActivityForResultA",null,null,null,null));
        demoNames.add(new ButtonBean("AsyncTaskActivityDemo","com.tcl.example.com.tcl.dejun.xie.asnycTask.AsyncTaskActivity",
                "com.tcl.example.com.tcl.dejun.xie.action.asnycTask",Intent.CATEGORY_DEFAULT,
                null,"text/message"));
        demoNames.add(new ButtonBean("testScrollMethod","com.tcl.example.com.tcl.dejun.xie.testScrollMethod.ScrollTestActivity",null,null,null,null));
        demoNames.add(new ButtonBean("listViewUsage","com.tcl.example.com.tcl.dejun.xie.ListViewUsage.ListViewActivity",null,null,null,null));
        demoNames.add(new ButtonBean("XmlActivity","com.tcl.example.com.tcl.dejun.xie.xmlResource.XmlActivity",null,null,null,null));
        demoNames.add(new ButtonBean("PorterDuffActivity","com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo.PorterDuffActivity",null,null,null,null));
        demoNames.add(new ButtonBean("PorterDuffActivity_2","com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo.PorterDuffActivity_2",null,null,null,null));
        demoNames.add(new ButtonBean("TintActivity","com.tcl.example.com.tcl.dejun.xie.tintAndClipping.TintActivity",null,null,null,null));
        demoNames.add(new ButtonBean("LoginActivity","com.tcl.example.com.tcl.dejun.xie.ReadAndWriteData.LoginActivity",null,null,null,null));
        demoNames.add(new ButtonBean("RecyclerViewActivity","com.tcl.example.com.tcl.dejun.xie.RecyclerViewDemo.RecyclerViewActivity",null,null,null,null));
        demoNames.add(new ButtonBean("ImageDownLoadActivity","com.tcl.example.com.tcl.dejun.xie.imageDownloag.ImageDownLoadActivity",null,null,null,null));
    }

    public class ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return demoNames.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return super.areAllItemsEnabled();
        }

        @Override
        public boolean isEnabled(int position) {
            return super.isEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FrameLayout frameLayout;
            AbsListView.LayoutParams layoutParams;
            FrameLayout.LayoutParams fLayoutParams;
            ButtonBean bean=demoNames.get(position);

            if(convertView==null){
                frameLayout=new FrameLayout(mContext);
                layoutParams=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                        AbsListView.LayoutParams.WRAP_CONTENT);
                frameLayout.setLayoutParams(layoutParams);
                frameLayout.setPadding(DensityUtil.dp2px(mContext, 50), DensityUtil.dp2px(mContext, 5),
                        DensityUtil.dp2px(mContext, 50), DensityUtil.dp2px(mContext, 5));

                fLayoutParams =new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                bt = new Button(mContext);
                bt.setLayoutParams(fLayoutParams);
                bt.setTextSize(DensityUtil.sp2px(mContext, 10));
                bt.setGravity(Gravity.CENTER);

                frameLayout.addView(bt);



            }else{
                frameLayout= (FrameLayout) convertView;
                bt= (Button) frameLayout.getChildAt(0);
            }

//            Button bt= (Button) frameLayout.getChildAt(0);
            bt.setFocusable(false);
            bt.setText(bean.getDemoName());
            bt.setBackgroundResource(R.drawable.tv_bg);
            bt.setTag(bean);
            initListeners(bt);
            return frameLayout;
        }
    }

    private void initListeners(View v) {
        final ButtonBean bean= (ButtonBean) v.getTag();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initIntent(bean);
                startActivity(intent);
//                Log.d("XDJ","button click");
            }
        });
    }

    public void initIntent(ButtonBean bean){
        //Activity的显示跳转
        String activityName= bean.getEnterActivity();
        if(bean.getAction()==null){
            try {
                Class clazz = Class.forName(activityName);
                intent=new Intent(mContext,clazz);
                Log.d("XDJ","Activity的显示跳转");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{ //Activity的隐式跳转
            intent=new Intent();
            //匹配action
            intent.setAction(bean.getAction());
            //匹配category
            if(bean.getCategory()!=null){
                intent.addCategory(bean.getCategory());
            }
            //匹配data
            bean.setScheme("15112264504");
            if(bean.getScheme()!=null){
                intent.setDataAndType(Uri.parse("data:"+bean.getScheme()),
                        bean.getMimeType());
            }
            Log.d("XDJ","Activity的隐式跳转");
        }
    }
}

package com.tcl.example.com.tcl.dejun.xie.RecyclerViewDemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tcl.example.xie.dejun.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/9/7.
 */
public class RecyclerViewActivity extends Activity{

    public static final int SPAN_COUNT=4;
    private RecyclerView recyclerView;
    private List<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        findViews();
        initData();
        initViews();
    }

    //模拟RecyclerView的数据来源
    private void initData() {
        dataList = new ArrayList<>();
        for(int i=0;i<21;i++){
            dataList.add("item_" + i);
        }
    }

    private void initViews() {
        //设置布局管理器
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,SPAN_COUNT);
        recyclerView.setLayoutManager(gridLayoutManager);

        //为RecyclerView设置Adapter
        MyAdapter myAdapter=new MyAdapter(dataList,this);
        recyclerView.setAdapter(myAdapter);
    }

    private void findViews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }
}

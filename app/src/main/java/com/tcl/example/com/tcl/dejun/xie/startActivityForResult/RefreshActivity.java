package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.Activity;
import android.os.Bundle;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/5/23.
 */
public class RefreshActivity extends Activity{
    public RefreshView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_layout);
        findViews();
        initData();
    }

    private void findViews() {
        refreshView= (RefreshView) findViewById(R.id.refresh_view);
    }

    private void initData() {
        try {
            refreshView.setContentView(R.layout.content_view);
            refreshView.setHeaderView(R.layout.header_view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshView.setRefreshType(RefreshView.NORMAL_TYPE);
        refreshView.setCurrentState(RefreshView.DOWN_PULL_REFRESH);
    }

}

package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/5/16.
 */
public class ActivityForResultB extends Activity implements View.OnClickListener {
    Button btBack;
    TextView tvText;
    Context mContext;
    String message;
    public static final String BUNDLE_KEY="messageFromB";
    public static final int RESULT_CODE_B=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_a);
        findViews();
        initData();
        initViews();
        initListeners();
    }

    private void initData() {
        message=this.getIntent().getExtras().getString(ActivityForResultA.BUNDLE_KEY);
        message="A:".concat(message);
    }

    private void initViews() {
        tvText.setText(message);
        tvText.setTextColor(mContext.getResources().getColor(R.color.black));
        tvText.setBackgroundResource(R.color.red);
    }

    private void initListeners() {
        btBack.setOnClickListener(this);
    }

    private void findViews() {
        btBack= (Button) findViewById(R.id.bt_back);
        tvText= (TextView) findViewById(R.id.tv_text);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_back:
                Intent intent=new Intent();
                intent.putExtra(BUNDLE_KEY, "silence");
                this.setResult(RESULT_CODE_B, intent);
                finish();
                Log.d("XDJ","bt_back");
                break;
            case R.id.bt_next:
//                Log.d("XDJ","bt_next");
                break;
        }
    }

}

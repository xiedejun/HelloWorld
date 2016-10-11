package com.tcl.example.com.tcl.dejun.xie.ReadAndWriteData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;
import com.tcl.example.com.tcl.dejun.xie.mainActivity.MainActivity;
import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/9/5.
 */
public class EnterActivity extends Activity implements View.OnClickListener {

    private String account;
    private boolean autoLogin;
    private TextView tvAccount;
    private TextView tvQuit;
    private boolean firstBack=true;
    private long firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        getDataFromPreviousActivity();
        findViews();
        initViews();
        initListeners();
    }

    private void initListeners() {
        tvQuit.setOnClickListener(this);
    }

    private void initViews() {
        tvAccount.setText(account);
    }

    private void findViews() {
        tvAccount = (TextView) findViewById(R.id.tv_account);
        tvQuit = (TextView) findViewById(R.id.tv_quit);
    }

    private void getDataFromPreviousActivity() {
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null){
            account = bundle.getString(ConstField.ACCOUNT);
            autoLogin = bundle.getBoolean(ConstField.AUTO_LOGIN);
        }
    }

    @Override
    public void onBackPressed() {
//        //自动登录，则直接返回MainActivity
//        if(autoLogin){
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//        //否则，返回上一级登录界面
//        else {
//            super.onBackPressed();
//        }

        if(firstBack){
            firstTime = System.currentTimeMillis();
            firstBack=false;
            ToastUtils.showMessage(this,"再按一次返回键回到主界面",ConstField.TOAST_DURATION);
        }else if(Math.abs(System.currentTimeMillis()-firstTime)<ConstField.TOAST_DURATION){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }else {
            ToastUtils.showMessage(this,"再按一次返回键回到主界面",ConstField.TOAST_DURATION);
            firstTime = System.currentTimeMillis();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_quit:
                Intent intent=new Intent();
                intent.putExtra(ConstField.ACCOUNT,account);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }
}

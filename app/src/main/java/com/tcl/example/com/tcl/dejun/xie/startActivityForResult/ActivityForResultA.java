package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/5/16.
 */
public class ActivityForResultA extends Activity implements View.OnClickListener {
    Button btBack,btNext,btStartService,btBindService;
    TextView tvText;
    Context mContext;
    public static final String BUNDLE_KEY="messageFromA";
    public static final int REQUEST_CODE_A=100;

//    Button btRefreshView;
    private Button btunBindService;

    MyServiceConnection conn;
    private boolean unbound;

    Spinner spinner;
    private ProgressBar progressBar;
//    private SeekBar seekBar;
    private RatingBar ratingBar;
//    private Button btCheckContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;

        setContentView(R.layout.activity_forresult_a);
        findViews();
        initViews();
        initListeners();
        Log.d("XDJ", "ActivityForResultA onCreate()---> getTaskId()--->" + getTaskId());
    }

    private void initViews() {
        tvText.setText("Activity---A,"
                + "--------," + "http://www.iciba.com"
                + ", https://www.hao123.com");
        tvText.setTextColor(mContext.getResources().getColor(R.color.blue));
        tvText.setBackgroundResource(R.color.green);
        Drawable drawable=getResources().getDrawable(R.drawable.icon_150);

        if(drawable!=null){
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth()/3, drawable.getIntrinsicHeight()/3);
        }

        tvText.setCompoundDrawables(drawable, null, drawable, null);
        tvText.setCompoundDrawablePadding(DensityUtil.dp2px(this,10));

        //spinner的初始化

        //1.获取数据源
        String[] strs=getResources().getStringArray(R.array.spinner_list);
        //2.建立数据源和Adapter的联系
        SpinnerAdapter spinnerAdapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,strs);
        //3.建立Adapter和Spinner的联系
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("XDJ", "onItemSelected()--->" + ((TextView) view).getText().toString() + ",position=" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Log.d("XDJ", "onNothingSelected()");
            }
        });

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////                Log.d("XDJ", "onProgressChanged()--->progress=" + progress + ",fromUser=" + fromUser);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d("XDJ", "onStartTrackingTouch()--->progress=" + seekBar.getProgress());
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d("XDJ", "onStopTrackingTouch()--->progress=" + seekBar.getProgress());
//            }
//        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d("XDJ", "rating=" + rating + ",fromUser=" + fromUser);
            }
        });
    }

    private void initListeners() {
        btBack.setOnClickListener(this);
        btNext.setOnClickListener(this);
        btStartService.setOnClickListener(this);
        btBindService.setOnClickListener(this);
//        btRefreshView.setOnClickListener(this);
        btunBindService.setOnClickListener(this);
        progressBar.setOnClickListener(this);
//        seekBar.setOnClickListener(this);
//        btCheckContacts.setOnClickListener(this);
    }

    private void findViews() {
        btBack= (Button) findViewById(R.id.bt_back);
        btNext= (Button) findViewById(R.id.bt_next);
        tvText= (TextView) findViewById(R.id.tv_text);
        btStartService= (Button) findViewById(R.id.bt_start_service);
        btBindService=(Button) findViewById(R.id.bt_bind_service);
        btunBindService= (Button) findViewById(R.id.bt_unbind_service);
//        btRefreshView= (Button) findViewById(R.id.bt_refresh_view);
        spinner= (Spinner) findViewById(R.id.spinner);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        seekBar= (SeekBar) findViewById(R.id.seek_bar);
        ratingBar= (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setIsIndicator(false);
//        btCheckContacts= (Button) findViewById(R.id.bt_check_contacts);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_back:
                finish();
//                Log.d("XDJ", "bt_back");
                break;
            case R.id.bt_next:
                Intent intent=new Intent();
                intent.setClass(mContext, ActivityForResultB.class);
                Bundle bundle=new Bundle();
                bundle.putString(BUNDLE_KEY, "what do you want from me?");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_A);
//                Log.d("XDJ", "bt_next");
                break;
            case R.id.bt_start_service:
                startMyService();
                break;
            case R.id.bt_bind_service:
                bindLocalService();
                break;
            case R.id.bt_unbind_service:
                unBindMyService();
                break;
            case R.id.progress_bar:
                progressBar.setProgress(progressBar.getProgress() + 10);
                progressBar.setSecondaryProgress(progressBar.getSecondaryProgress()+10);
                if(progressBar.getProgress()>=100){
                    progressBar.setProgress(10);
                    progressBar.setSecondaryProgress(20);
                }
                break;
        }
    }

    private void saveContactsInfo() {
        Intent intent=new Intent(this,ActivityofContacts.class);
        startActivity(intent);
    }

    private void unBindMyService() {

        if(conn==null && unbound){
            Toast.makeText(getApplicationContext(), "请不要多次解绑MyService", Toast.LENGTH_SHORT).show();
        }else if(conn==null && !unbound){
            Toast.makeText(getApplicationContext(), "尚未绑定MyService", Toast.LENGTH_SHORT).show();
        }else{
            unbindService(conn);
            unbound=true;
            conn=null;
            Toast.makeText(getApplicationContext(), "解绑MyService", Toast.LENGTH_SHORT).show();
        }
    }

    //绑定本地服务
    private void bindLocalService() {
        Intent intent=new Intent(mContext,MyService.class);
        conn=new MyServiceConnection();
        conn.setOnMyServiceConnectionListener(new MyServiceConnection.MyServiceConnectionListener() {
            @Override
            public void onStateChange(IBinderImp binderImp) {
                binderImp.method();
            }
        });
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void startMyService() {
        Intent intent=new Intent(mContext,MyService.class);
        startService(intent);
//        stopService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==ActivityForResultB.RESULT_CODE_B){
            Log.d("XDJ","requestCode==Activity.RESULT_CODE_B");
            String str="B:".concat(data.getStringExtra(ActivityForResultB.BUNDLE_KEY));
            tvText.setText(str);
        }else if(requestCode==Activity.RESULT_CANCELED){
            Log.d("XDJ","requestCode==Activity.RESULT_CANCELED");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("XDJ","ActivityForResultA onDestroy()");
    }
}

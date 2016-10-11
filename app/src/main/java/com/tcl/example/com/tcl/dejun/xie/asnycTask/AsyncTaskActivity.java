package com.tcl.example.com.tcl.dejun.xie.asnycTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.example.com.tcl.dejun.xie.mainActivity.MainActivity;
import com.tcl.example.xie.dejun.diyview.ConfirmDialog;
import com.tcl.example.xie.dejun.diyview.R;
import com.tcl.example.xie.dejun.diyview.SelfDefDialog;

/**
 * Created by dejun.xie on 2016/5/17.
 */
public class AsyncTaskActivity extends Activity implements View.OnClickListener,SelfDefDialog.SelfDefDialogListener {

    TextView tvText;
    Button btCall,btNotify,btExit,btToast,btAnotherApp;
    String phoneNumber;
    Context mContext;
    private Toast mToast;
    public static final int NOTIFICATION_REQUEST_CODE=1000;
    Bundle bundle;
    EditText editText;
    public static final String KEY_1="KEY_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=savedInstanceState;
        setContentView(R.layout.activity_async_task);
        findViews();
        initData();
        initListeners();
        Log.d("YYY", "AsyncTaskActivity getTaskId()=" + getTaskId());
    }

    private void initListeners() {
        btCall.setOnClickListener(this);
        btNotify.setOnClickListener(this);
        btExit.setOnClickListener(this);
        btToast.setOnClickListener(this);
        btAnotherApp.setOnClickListener(this);
    }

    private void findViews() {
        tvText= (TextView) findViewById(R.id.tv_text);
        btCall= (Button) findViewById(R.id.bt_call);
        btNotify= (Button) findViewById(R.id.bt_notification);
        btExit= (Button) findViewById(R.id.bt_exit);
        btToast= (Button) findViewById(R.id.bt_toast);
        editText= (EditText) findViewById(R.id.et_input);
        btAnotherApp= (Button) findViewById(R.id.bt_another_app);
    }

    private void initData() {
        mContext=this;
        Intent intent=getIntent();
//        Log.d("XDJ","uri.toString()="+uri.toString());
        tvText.setText(abstractDataFromFrontActivity(intent.getData().toString()));

        if(bundle!=null){
            editText.setText("KEY_1---"+bundle.getString(KEY_1));
        }
    }

    private String abstractDataFromFrontActivity(String s) {
        phoneNumber=s.substring(s.indexOf(":")+1);
        return "get message from frontPage:"+phoneNumber;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_call:
                phoneCall();
                break;
            case R.id.bt_notification:
                popNotification();
                break;
            case R.id.bt_exit:
                popConfirmDialog();
                break;
            case R.id.bt_toast:
                popAnToast();
                break;
            case R.id.bt_another_app:
                toAnotherApp();
                break;
        }
    }

    private void toAnotherApp() {
        Intent intent=new Intent();
        intent.setAction("demo2.MainActivity");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivity(intent);
    }

    private void popAnToast() {
        showToast("what do you want?");
    }

    private void popConfirmDialog() {
        SelfDefDialog confirmDialog=new SelfDefDialog(mContext,R.style.ConfirmDialog);
        confirmDialog.show();
        confirmDialog.setOnSelfDefDialogListener(this);
    }

    private void popNotification() {
        Intent intent=new Intent(this,MainActivity.class);
        Notification notification=new Notification.Builder(mContext)
                .setAutoCancel(true)
                .setTicker(".......")
                .setSmallIcon(R.drawable.icon_150)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon_150))
                .setContentTitle("标题")
                .setContentText("内容")
//                .setSubText("子内容")
                .setOngoing(false)
                .setContentIntent(PendingIntent.getActivity(mContext, NOTIFICATION_REQUEST_CODE,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .build();
        NotificationManager nm= (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_REQUEST_CODE, notification);
    }

    private void phoneCall() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_DIAL);
//        intent.setAction(Intent.ACTION_CALL);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        if(phoneNumber.isEmpty()){
            showToast(null);
            return;
        }
        intent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }

    public void showToast(String text){
        if(mToast!=null){
            mToast.cancel();
        }
        if(text==null){
            mToast=Toast.makeText(mContext,"空号无法拨打",Toast.LENGTH_SHORT);
        }else{
            mToast=Toast.makeText(mContext,text,Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("XDJ","onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("XDJ", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("XDJ", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("XDJ", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("XDJ", "onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("XDJ", "onRestart()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bundle=outState;
        bundle.putString(KEY_1,editText.getText().toString());
        Log.d("XDJ", "onSaveInstanceState(Bundle outState)");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
        Log.d("XDJ", "onRestoreInstanceState(Bundle outState)");
    }

    @Override
    public void onConfirmClick() {
//        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("XDJ", "onNewIntent(Intent intent)");
    }
}

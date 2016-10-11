package com.tcl.example.xie.dejun.diyview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tcl.example.com.tcl.dejun.xie.asnycTask.AsyncTaskActivity;
import com.tcl.example.com.tcl.dejun.xie.mainActivity.MainActivity;

/**
 * Created by dejun.xie on 2016/5/18.
 */
public class SelfDefDialog extends AlertDialog implements View.OnClickListener {

    Button btConfirm,btCancel;
    Context mContext;

    public SelfDefDialog(Context context) {
        super(context);
        mContext=context;
    }

    public SelfDefDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext=context;
    }

    public SelfDefDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        setCanceledOnTouchOutside(false);
        initData();
        findViews();
        initListeners();
    }

    private void initData() {
//        setTitle("退出");
    }

    private void initListeners() {
        btConfirm.setOnClickListener(this);
        btCancel.setOnClickListener(this);
    }

    private void findViews() {
        btConfirm= (Button) findViewById(R.id.bt_confirm);
        btCancel= (Button) findViewById(R.id.bt_cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_confirm:
//                System.exit(0);
                backToMainActivity();
                break;
            case R.id.bt_cancel:
                SelfDefDialog.this.dismiss();
                break;
        }
    }

    private void backToMainActivity() {
        Intent intent=new Intent(mContext, MainActivity.class);
        mContext.startActivity(intent);
        selfDefDialogListener.onConfirmClick();
        SelfDefDialog.this.dismiss();
    }

    SelfDefDialogListener selfDefDialogListener;

    public interface SelfDefDialogListener{
        void onConfirmClick();
    }

    public void setOnSelfDefDialogListener(SelfDefDialogListener selfDefDialogListener){
        this.selfDefDialogListener=selfDefDialogListener;
    }
}

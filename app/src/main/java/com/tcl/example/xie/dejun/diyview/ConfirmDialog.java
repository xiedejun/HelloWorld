package com.tcl.example.xie.dejun.diyview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by dejun.xie on 2016/5/18.
 */
public class ConfirmDialog extends Dialog implements View.OnClickListener {
    Button btConfirm,btCancel;
    Context mContext;

    public ConfirmDialog(Context context) {
        super(context);
        mContext=context;
    }

    public ConfirmDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext=context;
    }

    protected ConfirmDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
        setTitle("退出");
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
                System.exit(0);
                break;
            case R.id.bt_cancel:
                ConfirmDialog.this.dismiss();
                break;
        }
    }

}

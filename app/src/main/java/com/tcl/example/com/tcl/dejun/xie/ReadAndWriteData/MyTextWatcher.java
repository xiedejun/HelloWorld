package com.tcl.example.com.tcl.dejun.xie.ReadAndWriteData;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;

/**
 * Created by dejun.xie on 2016/9/5.
 */
public class MyTextWatcher implements TextWatcher {

    EditText editText;
    Context mContext;
    private String strTemp;

    public MyTextWatcher(EditText editText,Context mContext) {
        this.editText = editText;
        this.mContext=mContext;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(s.length()==LoginActivity.MAX_LENGTH){
            ToastUtils.showMessage(mContext, "已达到最大长度——" + LoginActivity.MAX_LENGTH, 2000);
            strTemp=s.toString();
        }

        if(s.length()>LoginActivity.MAX_LENGTH){
            subText(s,editText);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void subText(CharSequence s,EditText editText) {
        if(editText.getSelectionStart()>LoginActivity.MAX_LENGTH){
            editText.setText(s.subSequence(0, LoginActivity.MAX_LENGTH));
        }else{
            editText.setText(strTemp);
        }
        Editable editable = editText.getText();
        Selection.setSelection(editable, LoginActivity.MAX_LENGTH);
    }
}

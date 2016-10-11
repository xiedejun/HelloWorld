package com.tcl.example.com.tcl.dejun.xie.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by dejun.xie on 2016/7/28.
 */
public class ToastUtils {

    private static Toast toast=null;

    public static void showMessage(Context context,String msg,int duration){
        if(toast==null){
            toast=Toast.makeText(context,msg,duration);
        }else{
            toast.setText(msg);
            toast.setDuration(duration);
        }
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}

package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by dejun.xie on 2016/5/28.
 */
public class MyServiceConnection implements ServiceConnection {

    IBinderImp binderImp;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("XDJ","onServiceConnected()");
        binderImp = (IBinderImp) service;
//        binderImp.getMyService().popToast();
//        binderImp.method();
        listener.onStateChange(binderImp);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //onServiceDisconnected() 在连接 正常关闭 的情况下是不会被调用的
        //该方法只在Service 被破坏了或者被杀死的时候调用. 例如, 系统资源不足,要关闭一些Services
        Log.d("XDJ","onServiceDisconnected()");
    }

    //对外暴露给 Activity 的接口
    MyServiceConnectionListener listener;
    public interface MyServiceConnectionListener{
        void onStateChange(IBinderImp binderImp);
    }

    public void setOnMyServiceConnectionListener(MyServiceConnectionListener listener){
        this.listener=listener;
    }
}

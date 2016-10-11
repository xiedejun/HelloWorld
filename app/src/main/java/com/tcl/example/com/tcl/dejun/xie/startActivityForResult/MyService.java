package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by dejun.xie on 2016/5/20.
 */
public class MyService extends Service {
    private int i=0;
    IBinderImp binderImp;
    private static int serviceCount=0;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("XDJ","onBind() getTaskId()--->");
//        popToast();//注意：该方法在绑定时会被调用一次且仅一次，因为绑定(即onBind()方法)只会执行一次

        //方法一
//        binderImp=new IBinderImp();
//        binderImp.setService(MyService.this);

        //方法二
        return new IBinderImp(){
            @Override
            public void method() {
//                super.method();
                popToast();
            }
        };
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("XDJ","myService onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("XDJ","MyService onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("XDJ", "onStartCommand()");
        new Thread(){
            @Override
            public void run() {
                try {

                    for (int j=0;j<10;j++){
                        Log.d("XDJ", "i=" + ++i);
                        Thread.sleep(1000);
                        if(i>3){
                            stopSelf();
                        }
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
//        popToast();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("XDJ", "MyService onDestroy()");
    }

    public void popToast(){
        Toast.makeText(getApplicationContext(),"MyService的方法被调用了",Toast.LENGTH_SHORT).show();
    }
}

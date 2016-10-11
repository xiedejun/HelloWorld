package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.os.Binder;
import android.os.IBinder;

/**
 * Created by dejun.xie on 2016/5/28.
 */
public class IBinderImp extends Binder {

    public MyService getMyService() {
        return myService;
    }

    MyService myService;

    public void setService(MyService myService){
        this.myService=myService;
    }

    public void method(){

    }
}

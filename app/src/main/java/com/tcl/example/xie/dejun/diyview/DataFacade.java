package com.tcl.example.xie.dejun.diyview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/2/23.
 */
public class DataFacade {
    public static final int SIZE=15;
    public static List<String> getRefLvData(){
        List<String> stringList=new ArrayList<>();
        for(int i=0;i<SIZE;i++){
            stringList.add("RefLv original data--->"+(i+1));
        }
        return stringList;
    }
}

package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

/**
 * Created by dejun.xie on 2016/6/23.
 */
//手机联系人信息
public class ContactInfo {
    int id;
    String name;
    String telNum;

    public ContactInfo() {
    }

    public ContactInfo(int id, String name, String telNum) {
        this.id = id;
        this.name = name;
        this.telNum = telNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNum() {
        return telNum;
    }

    public void setTelNum(String telNum) {
        this.telNum = telNum;
    }
}

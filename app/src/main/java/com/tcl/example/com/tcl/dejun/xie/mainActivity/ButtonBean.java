package com.tcl.example.com.tcl.dejun.xie.mainActivity;

/**
 * Created by dejun.xie on 2016/5/16.
 */
public class ButtonBean {

    private String demoName;//按钮名
    private String enterActivity;//全包名
    private String action;//intentFilter的action值
    private String category;//intentFilter的category值
    private String scheme;//data的 scheme
    private String mimeType;//data的 mimeType

    public ButtonBean(String demoName, String enterActivity, String action, String category, String scheme, String mimeType) {
        this.demoName = demoName;
        this.enterActivity = enterActivity;
        this.action = action;
        this.category = category;
        this.scheme = scheme;
        this.mimeType = mimeType;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDemoName() {
        return demoName;
    }

    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }

    public String getEnterActivity() {
        return enterActivity;
    }

    public void setEnterActivity(String enterActivity) {
        this.enterActivity = enterActivity;
    }
}

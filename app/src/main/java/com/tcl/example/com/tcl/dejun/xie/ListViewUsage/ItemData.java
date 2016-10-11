package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

/**
 * Created by dejun.xie on 2016/7/29.
 */
public class ItemData {

    int drawableId;
    int type;
    String fileName;
    boolean hasBeenRead=false;

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean getHasBeenRead() {
        return hasBeenRead;
    }

    public void setHasBeenRead(boolean hasBeenRead) {
        this.hasBeenRead = hasBeenRead;
    }
}

package com.tcl.example.com.tcl.dejun.xie.imageDownloag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/9/29.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    public static final int CB_TYPE=1;
    public static final int ITEM_TYPE=2;
    private List<String> imgsUrlList;
    private Context mContext;
    private ArrayList<Integer> checkPos;

    public int getLastPos() {
        return lastPos;
    }

    private int lastPos=-1;

    public MyAdapter(List<String> imgsUrlList,Context mContext) {
        this.imgsUrlList=imgsUrlList;
        this.mContext=mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=View.inflate(mContext, R.layout.rv_img_item,null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv.setText(getFileName(imgsUrlList.get(position)));

        if(checkPos!=null && checkPos.contains(position)){
            holder.cb.setChecked(true);
        }else {
            holder.cb.setChecked(false);
        }

        //使用"R.color.orange"表示上一次选中的Item
        if(lastPos!=-1 && lastPos==position){
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.orange));
        }else {
            holder.tv.setTextColor(mContext.getResources().getColor(R.color.black));
        }

        //设置Item的点击监听
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickEvent(holder,position,CB_TYPE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickEvent(holder,position,ITEM_TYPE);
            }
        });
    }

    private void itemClickEvent(ViewHolder holder, int position,int type) {
//        Log.d("XDJ","holder.itemView.setOnClickListener()");

        if(type==ITEM_TYPE){
            holder.cb.setChecked(!holder.cb.isChecked());
        }

        //记录被勾选的Item
        if(holder.cb.isChecked()){
            if(checkPos==null){
                checkPos=new ArrayList<Integer>();
            }
            checkPos.add(position);
        }else{
            if(checkPos!=null && checkPos.contains(position)){
                checkPos.remove(new Integer(position));
            }
        }

        //通知lastPos和当前position的Item的文字颜色变化
        if(lastPos!=-1 && lastPos!=position){
            notifyItemChanged(lastPos);
        }

        lastPos=position;
        notifyItemChanged(position);
    }

    private String getFileName(String s) {
        return s.substring(s.lastIndexOf('/')+1);
    }

    @Override
    public int getItemCount() {
        return imgsUrlList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox cb;
        private TextView tv;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            initView(itemView);
        }

        private void initView(View itemView) {
            cb = (CheckBox) itemView.findViewById(R.id.cb_img);
            tv = (TextView) itemView.findViewById(R.id.tv_description);
        }
    }
}

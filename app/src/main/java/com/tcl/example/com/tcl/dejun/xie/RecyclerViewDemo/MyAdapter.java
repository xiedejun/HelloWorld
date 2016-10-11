package com.tcl.example.com.tcl.dejun.xie.RecyclerViewDemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.List;

/**
 * Created by dejun.xie on 2016/9/7.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final WindowManager windowManager;
    private final DisplayMetrics displayMetrics;
    private List<String> dataList;
    private Context mContext;

    public MyAdapter(List<String> dataList,Context mContext) {
        this.dataList = dataList;
        this.mContext=mContext;

        //获取屏幕宽高
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView tv=new TextView(viewGroup.getContext());
//        ViewGroup.LayoutParams layoutParams=viewGroup.getLayoutParams();
//        layoutParams.width=displayMetrics.widthPixels/RecyclerViewActivity.SPAN_COUNT;
//        layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
//        tv.setLayoutParams(layoutParams);
        tv.setTextSize(20);
        tv.setTextColor(mContext.getResources().getColor(R.color.black));
        ViewHolder viewHolder=new ViewHolder(tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int i) {
        viewHolder.tv.setText(dataList.get(i));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView;
        }
    }
}

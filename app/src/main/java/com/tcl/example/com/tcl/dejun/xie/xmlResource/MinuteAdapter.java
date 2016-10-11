package com.tcl.example.com.tcl.dejun.xie.xmlResource;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

import java.util.List;

/**
 * Created by dejun.xie on 2016/8/16.
 */
public class MinuteAdapter extends BaseAdapter {

    List<String> minuteList;
    Context mContext;

    public MinuteAdapter(Context context, List<String> minuteList) {
        mContext=context;
        this.minuteList=minuteList;
    }

    @Override
    public int getCount() {
        return minuteList.size()*3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Log.d("XDJ","position="+position);
        TextView tv=null;
        if(convertView==null){
            tv=new TextView(mContext);
            tv.setTextSize(DensityUtil.sp2px(mContext, 20));
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.setTvMinute(tv);
            convertView=tv;
            convertView.setTag(viewHolder);
        }else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            tv = viewHolder.getTvMinute();
            convertView=tv;
        }
        ListView.LayoutParams layoutParams = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        tv.setGravity(Gravity.CENTER);
        tv.setText(minuteList.get(position % minuteList.size()));
        tv.setTextColor(mContext.getResources().getColor(R.color.gray));
        return convertView;
    }

    public class ViewHolder{

        TextView tvMinute;

        public TextView getTvMinute() {
            return tvMinute;
        }

        public void setTvMinute(TextView tvMinute) {
            this.tvMinute = tvMinute;
        }
    }
}

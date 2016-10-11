package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.content.ContentResolver;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

import java.util.List;

/**
 * Created by dejun.xie on 2016/6/23.
 */
public class ContactAdapter extends BaseAdapter {
    List<ContactInfo> contactInfoList;
    Context context;

    public ContactAdapter(Context context,List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
        this.context=context;
    }

    @Override
    public int getCount() {
        return contactInfoList.size();
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
        ContactInfo contactInfo=contactInfoList.get(position);
        View v;

        if(convertView==null){
            v=View.inflate(context, R.layout.contact_list_view,null);
        }else{
            v=convertView;
        }

        TextView tvName= (TextView) v.findViewById(R.id.tv_name);
        TextView tvTel= (TextView) v.findViewById(R.id.tv_tel);
        tvName.setText(contactInfo.getName());
        tvTel.setText(contactInfo.getTelNum());
        return v;
    }
}

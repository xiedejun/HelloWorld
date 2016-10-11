package com.tcl.example.com.tcl.dejun.xie.startActivityForResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/6/23.
 */
public class ActivityofContacts extends Activity{

    private ListView listView;
    List<ContactInfo> contactInfoList;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    notifyListViewAdapterChanged();
                    break;
            }
        }
    };

    private void notifyListViewAdapterChanged() {
        ContactAdapter contactAdapter=new ContactAdapter(getApplicationContext(),contactInfoList);
        listView.setAdapter(contactAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(createListView());
        showContacts();
    }

    private void showContacts() {
        new Thread(){
            @Override
            public void run() {
                ContentResolver contentResolverContacts=getContentResolver();
                Cursor cursorRawCursor=contentResolverContacts.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},null,null,null);
                if(cursorRawCursor==null){
                    Log.d("XDJ", "cursor==null,无联系人信息！");
                    return;
                }
                contactInfoList=new ArrayList<ContactInfo>();
                while(cursorRawCursor.moveToNext()){
                    ContactInfo contactInfo=new ContactInfo();
                    int rawContactId=cursorRawCursor.getInt(0);
                    contactInfo.setId(rawContactId);
//                    Log.d("XDJ", "rawContactId=" + rawContactId);
                    Cursor cursorData = contentResolverContacts.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"mimetype", "data1"}, "raw_contact_id=?",
                            new String[]{rawContactId + ""}, null);
                    while (cursorData.moveToNext()){
                        if("vnd.android.cursor.item/name".equals(cursorData.getString(0))){
                            contactInfo.setName(cursorData.getString(1));
                        }else if("vnd.android.cursor.item/phone_v2".equals(cursorData.getString(0))){
                            contactInfo.setTelNum(cursorData.getString(1));
                        }
                    }
                    contactInfoList.add(contactInfo);
//                    Log.d("XDJ","contactInfo.getName()="+contactInfo.getName()+",contactInfo.getTelNum()="+contactInfo.getTelNum());
                }
                if(contactInfoList!=null){
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    private View createListView() {
        listView = new ListView(this);
        ListView.LayoutParams layoutParams=new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
                ListView.LayoutParams.MATCH_PARENT);
        listView.setLayoutParams(layoutParams);
        return listView;
    }
}

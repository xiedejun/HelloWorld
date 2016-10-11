package com.tcl.example.com.tcl.dejun.xie.imageDownloag;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;
import com.tcl.example.com.tcl.dejun.xie.mainActivity.ButtonBean;
import com.tcl.example.com.tcl.dejun.xie.startActivityForResult.ActivityForResultA;
import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dejun.xie on 2016/9/28.
 */
public class ImageDownLoadActivity extends Activity implements View.OnClickListener {

    public static final String IMGS_URL="http://10.128.208.100:8080/all_imgs_list.txt";
    public static final int NET_WORK_ABNORMAL=0;
    public static final int NET_WORK_NORMAL=1;
    public static final int NO_IMGS=2;
    public static final int IMG_SUCCESS = 3;
    public static final int IMG_FAIL = 4;
    public static final int DOWNLOAD_SUCCESS = 5;
    private TextView tvIP;
    private List<String> imgsUrlList;
    private RecyclerView recyclerView;
    private ViewStub viewStub;
    private TextView tvTip;
    private Context mContext;

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                //网络异常
                case NET_WORK_ABNORMAL:
                    onFail(R.string.net_work_abnormal);
                    break;
                //服务器成功响应
                case NET_WORK_NORMAL:
                    onSuccess();
                    break;
                //没有图片资源
                case NO_IMGS:
                    onFail(R.string.no_imgs_resource);
                    break;
                case IMG_SUCCESS:
                    ivPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivPreview.setImageBitmap((Bitmap) msg.obj);
                    break;
                case IMG_FAIL:
                    ivPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivPreview.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.load_fail));
                    break;
                case DOWNLOAD_SUCCESS:
                    ToastUtils.showMessage(mContext,"图片成功下载！",2000);
                    break;
                default:
                    break;
            }
        }
    };

    private Message msg;
    private Button btDownload;
    private Button btPreview;
    private MyAdapter myAdapter;
    private ImageView ivPreview;

    private void onSuccess() {
        Log.d("XDJ","first onSuccess()");

        if(tvTip!=null){
            tvTip.setVisibility(View.GONE);
        }

        viewStub= (ViewStub) findViewById(R.id.view_stub_success);
        if(viewStub!=null){
            viewStub.inflate();
        }

        myAdapter = new MyAdapter(imgsUrlList,mContext);
        recyclerView = (RecyclerView) findViewById(R.id.rv_img_list);
        //初始化recyclerView
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void onFail(int str) {
        Log.d("XDJ","first onFail()");
        viewStub= (ViewStub) findViewById(R.id.view_stub_fail);
        if(viewStub!=null){
            viewStub.inflate();
        }

        tvTip = (TextView) findViewById(R.id.tv_tip);
        tvTip.setText(str);
        tvTip.setTextSize(DensityUtil.sp2px(mContext,20));
        tvTip.setTextColor(mContext.getResources().getColor(R.color.red));

        tvTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImgsData();
                tvTip.setText("... ...");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_download);
        mContext = this;

        findViews();
        initViews();
        initDatas();
        initListeners();
    }

    private void initDatas() {
        getImgsData();
    }

    private void getImgsData() {
        //获取Tomcat资源文件下的所有图片资源的txt文件
        new Thread(){
            @Override
            public void run() {
                try {
                    URL imgsUrl=new URL(IMGS_URL);
                    HttpURLConnection httpURLConnection= (HttpURLConnection) imgsUrl.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);
                    httpURLConnection.connect();

                    msg = handler.obtainMessage();
                    //服务器成功响应请求
                    if(httpURLConnection.getResponseCode()==200){
                        InputStreamReader inputStreamReader=new InputStreamReader(httpURLConnection.getInputStream());
                        BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                        String imgUrl=null;
                        while ((imgUrl=bufferedReader.readLine())!=null){
                            if(imgsUrlList==null){
                                imgsUrlList=new ArrayList<>();
                            }
                            imgsUrlList.add(imgUrl);
                        }
                        if(imgsUrlList==null){
                            //没有图片资源
                            msg.what=NO_IMGS;
                        }else {
                            //有图片资源
                            msg.what=NET_WORK_NORMAL;
                        }
                    }else{
                        //网络异常
                        msg.what=NET_WORK_ABNORMAL;
                        ToastUtils.showMessage(ImageDownLoadActivity.this,"网络异常！",1000);
                    }
                    handler.sendEmptyMessage(msg.what);
                } catch (MalformedURLException e) {
                    Log.d("XDJ","MalformedURLException()");
                    handler.sendEmptyMessage(NET_WORK_ABNORMAL);
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("XDJ","IOException()");
                    handler.sendEmptyMessage(NET_WORK_ABNORMAL);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void findViews() {
        tvIP = (TextView) findViewById(R.id.tv_ip);
        btDownload = (Button) findViewById(R.id.bt_preview);
        btPreview = (Button) findViewById(R.id.bt_download);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
    }

    private void initViews() {
        tvIP.setText(IMGS_URL);
    }

    private void initListeners() {
        btDownload.setOnClickListener(this);
        btPreview.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        File file=new File(getFilesDir(),getFileName(imgsUrlList.get(myAdapter.getLastPos())));
        switch (v.getId()) {
            case R.id.bt_preview:
                //如果图片已存在，则直接从缓存取图片
                if(file.exists()){
                    Log.d("XDJ","从缓存取得的图片");
                    ivPreview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    ivPreview.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }else{
                    Log.d("XDJ","从网络下载的图片");
                    new Thread() {
                        @Override
                        public void run() {
                            URL imgsUrl = null;
                            try {
//                            Log.d("XDJ","imgsUrlList.get(myAdapter.getLastPos())="+imgsUrlList.get(myAdapter.getLastPos()));
                                imgsUrl = new URL(imgsUrlList.get(myAdapter.getLastPos()));
                                HttpURLConnection httpURLConnection = (HttpURLConnection) imgsUrl.openConnection();
                                httpURLConnection.setRequestMethod("GET");
                                httpURLConnection.setReadTimeout(5000);
                                httpURLConnection.setConnectTimeout(5000);
                                httpURLConnection.connect();
                                if (httpURLConnection.getResponseCode() == 200) {
                                    InputStream inputStream=httpURLConnection.getInputStream();
                                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                    Message msg = handler.obtainMessage();
                                    msg.what = IMG_SUCCESS;
                                    msg.obj = bitmap;
                                    handler.sendMessage(msg);
//                                Log.d("XDJ","msg.what=IMG_SUCCESS");
                                    inputStream.close();
                                } else {
                                    handler.sendEmptyMessage(IMG_FAIL);
//                                Log.d("XDJ","msg.what=IMG_FAIL");
                                }
                            } catch (Exception e) {
                                handler.sendEmptyMessage(IMG_FAIL);
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }

                break;
            case R.id.bt_download:
                //如果图片已存在，则不再下载
                if(file.exists()){
                    ToastUtils.showMessage(mContext,"图片已下载过了！",2000);
                }else{
                    //下载至本地
                    new Thread() {
                        @Override
                        public void run() {
                            URL imgsUrl = null;
                            try {
                                imgsUrl = new URL(imgsUrlList.get(myAdapter.getLastPos()));
                                Log.d("XDJ", "imgsUrl=" + imgsUrl.toString());
                                HttpURLConnection httpURLConnection = (HttpURLConnection) imgsUrl.openConnection();
                                httpURLConnection.setRequestMethod("GET");
                                httpURLConnection.setReadTimeout(5000);
                                httpURLConnection.setConnectTimeout(5000);
                                httpURLConnection.connect();
                                if (httpURLConnection.getResponseCode() == 200) {
                                    //写入指定的文件夹下
                                    InputStream inputStream=httpURLConnection.getInputStream();
                                    File imgFile = new File(getFilesDir(), getFileName(imgsUrl.toString()));
                                    FileOutputStream fileOutputStream=new FileOutputStream(imgFile);
                                    byte[] bytes=new byte[1024];
                                    int len=0;
                                    while((len=inputStream.read(bytes))!=-1){
                                        fileOutputStream.write(bytes,0,len);
                                    }
                                    //关闭流
                                    fileOutputStream.close();
                                    inputStream.close();

                                    handler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                                } else {
                                    handler.sendEmptyMessage(IMG_FAIL);
//                                Log.d("XDJ","msg.what=IMG_FAIL");
                                }
                            } catch (Exception e) {
                                handler.sendEmptyMessage(IMG_FAIL);
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            default:
                break;
        }
    }

    private String getFileName(String s) {
        return s.substring(s.lastIndexOf("/")+1);
    }
}

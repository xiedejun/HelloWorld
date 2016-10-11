package com.tcl.example.com.tcl.dejun.xie.xmlResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tcl.example.com.tcl.dejun.xie.Utils.ToastUtils;
import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/13.
 */
public class RunningClock extends View {

    Context mContext;
    int mWidth;
    int mHeight;
    Paint mPaint;
    int mRadius;
    private int mStrokeWidth;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private int screenWidth;
    private int screenHeight;
    private float mOffsetY;
    private int mOffsetX;
    private float lineLenght;

    //秒钟直线的终点坐标
    private float[] mSecondEnd=new float[]{0,0};
    //秒钟直线的终点坐标
    private float[] mMinuteEnd=new float[]{0,0};
    //秒钟直线的终点坐标
    private float[] mHourEnd=new float[]{0,0};

    private int mSecondCount=0;
    private int mMinuteCount=0;
    private int mHourCount=0;

    private float mSecondLength;
    private float mMinuteLength;
    private float mHourLength;

    private int mSetHour=XmlActivity.INIT_HOUR+1;
    private int mSetMinute=XmlActivity.INIT_MINUTE+1;

    public String getmSetHour() {
        return String.valueOf(mSetHour);
    }

    public String getmSetMinute() {
        return String.valueOf(mSetMinute);
    }

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case  1:
                    mSecondCount++;
//                    Log.d("XDJ","cos="+(Math.PI / 2- 6 * count * Math.PI / 180)*(180/Math.PI));

                    //秒针的终点坐标计算
                    mSecondEnd[0]= (float) (mSecondLength*Math.cos((Math.PI / 2 - 6 * mSecondCount * Math.PI / 180)));
                    mSecondEnd[1]= (-1)*(float) (mSecondLength*Math.sin((Math.PI / 2 - 6* mSecondCount * Math.PI / 180)));

                    //分针的终点坐标计算
                    if(mSecondCount-60==0){
                        mSecondCount=0;
                        mMinuteCount++;
                        mSetMinute=mMinuteCount;
                        mMinuteEnd[0]=(float) (mMinuteLength*Math.cos((Math.PI / 2 - 6 * mMinuteCount * Math.PI / 180)));
                        mMinuteEnd[1]= (-1)*(float) (mMinuteLength*Math.sin((Math.PI / 2 - 6* mMinuteCount * Math.PI / 180)));
                        mTimeChangeListener.timeChanged(mSetHour,mSetMinute);
                    }

                    //时针的终点坐标计算
                    if(mMinuteCount-60==0){
                        mMinuteCount=0;
                        mHourCount++;
                        mSetHour=mHourCount;
                        if(mHourCount-12==0){
                            mHourCount=0;
                        }
                        mHourEnd[0]=(float) (mHourLength*Math.cos((Math.PI / 2- 30 * mHourCount * Math.PI / 180)));
                        mHourEnd[1]= (-1)*(float) (mHourLength*Math.sin((Math.PI / 2 - 30 * mHourCount * Math.PI / 180)));
                        mTimeChangeListener.timeChanged(mSetHour,mSetMinute);
                    }

                    invalidate();
                    mHandler.sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    SetTime setTime= (SetTime) msg.obj;
                    int hour=setTime.getSetHour();
                    int minute=setTime.getSetMinute();

                    //重新设置时针的终点坐标
                    mHourCount=hour;
                    mHourEnd[0]=(float) (mHourLength*Math.cos((Math.PI / 2 - 30 * mHourCount * Math.PI / 180)));
                    mHourEnd[1]= (-1)*(float) (mHourLength*Math.sin((Math.PI / 2 - 30 * mHourCount * Math.PI / 180)));

                    //重新设置分针的终点坐标
                    mMinuteCount=minute;
                    mMinuteEnd[0]=(float) (mMinuteLength*Math.cos((Math.PI / 2 - 6 * mMinuteCount * Math.PI / 180)));
                    mMinuteEnd[1]= (-1)*(float) (mMinuteLength*Math.sin((Math.PI / 2 - 6* mMinuteCount * Math.PI / 180)));
                    invalidate();
                    mTimeChangeListener.timeChanged(mSetHour,mSetMinute);
//                    mMinuteCount++;//add by XDJ
                    mHandler.sendEmptyMessageDelayed(1, 0);
                default:
                    break;
            }
        }
    };

    public RunningClock(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext=context;
        //设置View的默认宽高
        mWidth=DensityUtil.dp2px(mContext,100);
        mHeight=DensityUtil.dp2px(mContext,100);
        //初始化画笔
        mPaint=new Paint();
        //初始化大圆边的宽度
        mStrokeWidth=DensityUtil.dp2px(mContext,4);

        //获取屏幕的宽高
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //数字的X,Y轴偏移量
        mOffsetY=DensityUtil.dp2px(mContext,20);
        mOffsetX=DensityUtil.dp2px(mContext,8);

        //秒针的长度及其初始的终点坐标
        mSecondLength = mWidth;
        mSecondEnd[0]= (float) (mSecondLength*Math.cos((Math.PI / 2)));
        mSecondEnd[1] = (float) (mSecondLength*Math.sin((-Math.PI / 2)));

        //分针的长度及其初始的终点坐标
        mMinuteLength = mWidth*0.8f;
        mMinuteCount=XmlActivity.INIT_MINUTE+1;
        mMinuteEnd[0]=(float) (mMinuteLength*Math.cos((Math.PI / 2 - 6 * mMinuteCount * Math.PI / 180)));
        mMinuteEnd[1]= (-1)*(float) (mMinuteLength*Math.sin((Math.PI / 2 - 6* mMinuteCount * Math.PI / 180)));

        //时针的长度及其初始的终点坐标
        mHourLength = mWidth*0.5f;
        mHourCount=XmlActivity.INIT_HOUR+1;
        mHourEnd[0]=(float) (mHourLength*Math.cos((Math.PI / 2 - 30 * mHourCount * Math.PI / 180)));
        mHourEnd[1]= (-1)*(float) (mHourLength*Math.sin((Math.PI / 2 - 30 * mHourCount * Math.PI / 180)));

        mHandler.sendEmptyMessageDelayed(1,1000);
    }

    public RunningClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RunningClock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽度的模式和大小
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);

        //获取高度的模式和大小
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        //设置View的宽高
        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    private int measureHeight(int heightMode, int height) {
        switch (heightMode){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mHeight=height;
                break;
        }
        return mHeight+mStrokeWidth*2;
    }

    private int measureWidth(int widthMode, int width) {
        switch (widthMode){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth=width;
                break;
        }
        return mWidth+mStrokeWidth*2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        Log.d("XDJ","onDraw(Canvas canvas)");

        //先画最外圈的大圆
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mContext.getResources().getColor(R.color.black));
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAntiAlias(true);
        mRadius= mWidth>mHeight?((mHeight-2*mStrokeWidth)/2):((mWidth-2*mStrokeWidth)/2);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mPaint);
        canvas.save();

        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mStrokeWidth/2);

        //画刻度
        for(int i=0;i<12;i++){
            lineLenght = 0.2f*mRadius;
            if(i%3==0){
                lineLenght =0.3f*mRadius;
                mPaint.setTextSize(DensityUtil.sp2px(mContext,20));
            }
            canvas.drawLine(mWidth/2,mStrokeWidth,mWidth/2, lineLenght,mPaint);
            canvas.rotate(30,mWidth/2,mHeight/2);
        }
        canvas.restore();

        //写文字,做了一些坐标值的微调
        canvas.drawText(String.valueOf(12),mWidth/2-1.5f*mOffsetX,
                mHeight/4+mOffsetY/2,mPaint);
        canvas.drawText(String.valueOf(3),3*mWidth/4-mOffsetX,
                mHeight/2+mOffsetY/2,mPaint);
        canvas.drawText(String.valueOf(6), mWidth / 2 - mOffsetX,
                3 * mHeight / 4 + mOffsetY / 4, mPaint);
        canvas.drawText(String.valueOf(9), mWidth / 4 - mOffsetX / 2,
                mHeight / 2 + mOffsetY / 2, mPaint);

        //画圆弧，做了一些圆弧角度的微调
        mPaint.reset();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth * 2);
        float intervals[]={5,5};
        PathEffect mPathEffect=new DashPathEffect(intervals,1.0f);
        mPaint.setPathEffect(mPathEffect);
        RectF rectF=new RectF(mWidth/4,mHeight/4,3*mWidth/4,3*mHeight/4);
        mPaint.setColor(mContext.getResources().getColor(R.color.blue));
        canvas.drawArc(rectF, -10.0f, -70.0f, false, mPaint);
        mPaint.setColor(mContext.getResources().getColor(R.color.purple));
        canvas.drawArc(rectF, 13.0f, 70.0f, false, mPaint);
        mPaint.setColor(mContext.getResources().getColor(R.color.red));
        canvas.drawArc(rectF, 100.0f, 67.0f, false, mPaint);
        mPaint.setColor(mContext.getResources().getColor(R.color.yellow));
        canvas.drawArc(rectF, 190.0f, 67.0f, false, mPaint);

        //画中心的爱心
        mPaint.reset();
        Bitmap bitmapSrc=BitmapFactory.decodeResource(getResources(), R.drawable.love_center);
        Bitmap bitmapSmall=scaleBitmap(bitmapSrc,DensityUtil.dp2px(mContext,50),
                DensityUtil.dp2px(mContext,50));
        canvas.drawBitmap(bitmapSmall, (mWidth - bitmapSmall.getWidth()) / 2,
                (mHeight - bitmapSmall.getHeight()) / 2, mPaint);

        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);//改变参考点的坐标，方便计算

        //画时针
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth * 2);
        mPaint.setColor(mContext.getResources().getColor(R.color.orange));
        canvas.drawLine(0, 0, mHourEnd[0], mHourEnd[1], mPaint);

        //注意：一开始onDraw()会调用两次，故不能在onDraw()中调用消息发送，
        //因为这样会导致发送了两次消息，进而出现秒针一次走2秒的现象
//        mHandler.sendEmptyMessageDelayed(1,1000);

        //画分针
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth * 1.4f);
        mPaint.setColor(mContext.getResources().getColor(R.color.light_blue));
        canvas.drawLine(0,0,mMinuteEnd[0],mMinuteEnd[1],mPaint);

        //画秒针
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mStrokeWidth / 2);
        mPaint.setColor(mContext.getResources().getColor(R.color.red));
//        canvas.drawLine(mWidth / 2, mHeight / 2, mWidth / 2, mWidth * 0.1f, mPaint);
//        Log.d("XDJ","mSecondEnd[0]="+mSecondEnd[0]+",mSecondEnd[1]="+mSecondEnd[1]);
        canvas.drawLine(0, 0, mSecondEnd[0], mSecondEnd[1], mPaint);

        canvas.restore();
    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    public void setTime(String setHour, String setMinute) {

        int hour = Integer.parseInt(setHour.substring(0, 2));
        int minute = Integer.parseInt(setMinute.substring(0, 2));

        if((mSetHour==hour) && (mSetMinute==minute)){
//            Log.d("XDJ","time no change!");
            return;
        }

        //先停止时钟的转动
//        Log.d("XDJ","time change!");
        mHandler.removeMessages(1);
        mSetHour=hour;
        mSetMinute=minute;
        SetTime setTime=new SetTime(mSetHour, mSetMinute);
        Message msg=mHandler.obtainMessage();
        msg.what=2;
        msg.obj=setTime;
        mHandler.sendMessage(msg);
    }

    //时间改变的监听器
    TimeChangeListener mTimeChangeListener;

    public interface TimeChangeListener{
        void timeChanged(int hour,int minute);
    }

    public void setTimeChangeListener(TimeChangeListener mTimeChangeListener){
        this.mTimeChangeListener=mTimeChangeListener;
    }
}

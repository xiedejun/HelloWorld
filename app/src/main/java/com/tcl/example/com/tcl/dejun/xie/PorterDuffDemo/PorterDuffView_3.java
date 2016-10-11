package com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/19.
 */
public class PorterDuffView_3 extends View {

    public static final float STROKE_WIDTH = 50;
    Context mContext;
    private Paint mPaint;
    private DisplayMetrics displayMetrics;
    private WindowManager windowManager;
    private int mWidth;
    private int mHeight;
    private Bitmap bitmapSmall;
    private Path mPath;
    private Bitmap mForeBitmap;
    private Canvas mCanvas;

    public PorterDuffView_3(Context context) {
        super(context);
        init(context, null, 0);
    }

    public PorterDuffView_3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PorterDuffView_3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContext=context;
        //设置该View的默认宽高
//        mWidth = DensityUtil.dp2px(mContext, 160);
//        mHeight = DensityUtil.dp2px(mContext, 200);

        Log.d("XDJ","PorterDuffView_3 defStyleAttr="+defStyleAttr);
        //获取屏幕宽高
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        //解析自定义属性,获得图片的原始宽高
        if(attrs!=null){

            TypedArray typedArray=mContext.obtainStyledAttributes(attrs, R.styleable.porterDuffView,defStyleAttr,0);

            int srcId=typedArray.getResourceId(R.styleable.porterDuffView_src, R.drawable.thank_you);

            //注意：match_parent=-1,wrap_content=-2
            int typedArrayWidth=typedArray.getLayoutDimension(R.styleable.porterDuffView_android_layout_width, ViewGroup.LayoutParams.WRAP_CONTENT);
            int typedArrayHeight=typedArray.getLayoutDimension(R.styleable.porterDuffView_android_layout_height, ViewGroup.LayoutParams.WRAP_CONTENT);
//            Log.d("XDJ","typedArrayWidth="+typedArrayWidth+",typedArrayHeight="+typedArrayHeight);

            typedArray.recycle();

            //只解析图片的宽高属性
            BitmapFactory.Options opt=new BitmapFactory.Options();
            opt.inJustDecodeBounds=true;
            BitmapFactory.decodeResource(mContext.getResources(), srcId, opt);

            //获取原始图片的原始宽高
            int srcWidth=opt.outWidth;
            int srcHeight=opt.outHeight;

            mWidth=getFinalSize(srcWidth, typedArrayWidth);
            mHeight=getFinalSize(srcHeight,typedArrayHeight);

            //计算缩放比例
            int scale=1;//默认缩放比例
            int scaleWidth=mWidth/displayMetrics.widthPixels;
            int scaleHeight=mHeight/displayMetrics.heightPixels;

            //无变形缩放
            if(scaleWidth>1 && scaleHeight>1){
                Log.d("XDJ", "PorterDuffView_3 scaleWidth>1 && scaleHeight>1");
                scale=(scaleWidth>scaleHeight?scaleWidth:scaleHeight);
                opt.inSampleSize=scale;
                opt.inJustDecodeBounds=false;
                bitmapSmall =BitmapFactory.decodeResource(mContext.getResources(), srcId, opt);
            }
            //变形缩放
            else if(scaleWidth>1){
                Log.d("XDJ","PorterDuffView_3 scaleWidth>1");
                opt.inSampleSize=scale;
                opt.inJustDecodeBounds=false;
                Bitmap originBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);
                int newHeight=0;
                if(srcHeight*displayMetrics.widthPixels/srcWidth==0){
                    newHeight=srcHeight;
                }else {
                    newHeight=srcHeight*displayMetrics.widthPixels/srcWidth;
                }
                bitmapSmall =scaleBitmap(originBitmap, displayMetrics.widthPixels,newHeight,true);
            }
            //变形缩放
            else if(scaleHeight>1){
                Log.d("XDJ","PorterDuffView_3 scaleHeight>1");
                opt.inSampleSize=scale;
                opt.inJustDecodeBounds=false;
                Bitmap originBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);
                int newWidth=0;
                if(srcWidth*displayMetrics.heightPixels/srcHeight==0){
                    newWidth=srcWidth;
                }else {
                    newWidth=srcWidth*displayMetrics.heightPixels/srcHeight;
                }
                bitmapSmall = scaleBitmap(originBitmap,newWidth,displayMetrics.heightPixels,true);
            }else {
                Log.d("XDJ","PorterDuffView_3 scaleHeight<1 && scaleWidth<1");
                opt.inSampleSize=scale;
                opt.inJustDecodeBounds=false;
                Bitmap originBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);
                bitmapSmall = scaleBitmap(originBitmap,mWidth,mHeight,true);
            }
        }

        //将图片的原始宽高设为默认的宽高
        mWidth=bitmapSmall.getWidth();
        mHeight=bitmapSmall.getHeight();
//        Log.d("XDJ","init() mWidth="+mWidth+",mHeight="+mHeight);

        initPaint();
    }

    private int getFinalSize(int optSize, int typedArraySize) {
        int mSize=0;
        //为match_parent时
        if(typedArraySize ==-1){
            mSize=displayMetrics.widthPixels;
        }
        //为wrap_parent时
        else if(typedArraySize ==-2){
            mSize = optSize;
        }
        //为具体的数值时
        else {
            mSize= typedArraySize;
        }
        return mSize;
    }

    private void initPaint() {
        mPaint=new Paint();
        mPaint.setAlpha(0);
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPath = new Path();
//        Log.d("XDJ", "initPaint() mWidth="+mWidth+",mHeight="+mHeight);
        mForeBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mForeBitmap);
        Bitmap srcBitmap=BitmapFactory.decodeResource(mContext.getResources(), R.drawable.girl_1);
        mCanvas.drawBitmap(scaleBitmap(srcBitmap,mWidth,mHeight,true),0,0,null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取宽度
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int width=MeasureSpec.getSize(widthMeasureSpec);

        //获取高度
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        //设置图片的宽高
        int setWidth=setMeasuredWidth(widthMode,width);
        int setHeight=setMeasuredHeight(heightMode, height);

        setMeasuredDimension(setWidth,setHeight);
    }

    //设置宽度
    private int setMeasuredWidth(int widthMode, int width) {
        switch (widthMode){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth=width;
                break;
        }
        return mWidth;
    }

    //设置高度
    private int setMeasuredHeight(int heightMode, int height) {
        switch (heightMode){
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mHeight=height;
                break;
        }
        return mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.drawBitmap(bitmapSmall, 0, 0, null);
        canvas.drawBitmap(mForeBitmap,0,0,null);

        //注意：尽可能避免在onDraw()方法中去缩放图片，因为缩放后图片的矩阵坐标发生了变化，要获取原坐标则需要矩阵求逆，
        //这是比较麻烦的操作，同理，new Rect(0, 0, mWidth, mHeight)也会产生类似的效果
//        canvas.drawBitmap(bitmapSmall, null, new Rect(0, 0, mWidth, mHeight), null);
//        canvas.drawBitmap(mForeBitmap,null,new Rect(0,0,mWidth,mHeight),null);
    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight,boolean needRecycled) {
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
        if (!origin.isRecycled() && needRecycled) {
            origin.recycle();
        }
        return newBM;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(),event.getY());
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mCanvas.drawPath(mPath,mPaint);
        invalidate();
        return true;
    }
}

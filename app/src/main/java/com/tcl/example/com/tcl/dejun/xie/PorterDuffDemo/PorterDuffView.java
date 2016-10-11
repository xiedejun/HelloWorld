package com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.tcl.example.xie.dejun.diyview.DensityUtil;
import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/17.
 */
public class PorterDuffView extends View {

    private int mWidth;
    private int mHeight;
    Context mContext;
    private Paint mPaint;
    private Bitmap bitmapSmall;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private PorterDuffXfermode duffXfermode;
    private Bitmap bitmapSmallerRect;
    private Bitmap bitmapSmallCircle;

    public PorterDuffView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mWidth = DensityUtil.dp2px(mContext, 100);
        mHeight = DensityUtil.dp2px(mContext, 80);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        duffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    public PorterDuffView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        scaleBitmap(context, attrs);
    }

    public PorterDuffView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        scaleBitmap(context, attrs);
    }

    private void scaleBitmap(Context context, AttributeSet attrs) {
        TypedArray array=context.obtainStyledAttributes(attrs, R.styleable.porterDuffView);
        Integer srcId=array.getResourceId(R.styleable.porterDuffView_src, R.drawable.src_img);
//        Bitmap srcBitmap = BitmapFactory.decodeResource(mContext.getResources(), srcId);

        BitmapFactory.Options opt=new BitmapFactory.Options();
        opt.inJustDecodeBounds=true;
        Bitmap srcBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);//图片未解析出来，但图片属性被解析到

        //原始图片的宽高
        int srcWidth=opt.outWidth;
        int srcHeight=opt.outHeight;
//        Log.d("XDJ","srcWidth="+srcWidth+",srcHeight="+srcHeight);

        //计算缩放比例
        int scale=1;//默认缩放比例
        int scaleWidth=srcWidth/displayMetrics.widthPixels;
        int scaleHeight=srcHeight/displayMetrics.heightPixels;

        //只有加载的图片比屏幕大时才进行缩放

        //无变形缩放
        if(scaleWidth>1 && scaleHeight>1){
            Log.d("XDJ","scaleWidth>1 && scaleHeight>1");
            scale=(scaleWidth>scaleHeight?scaleWidth:scaleHeight);
            opt.inSampleSize=scale;
            opt.inJustDecodeBounds=false;
            bitmapSmall=BitmapFactory.decodeResource(mContext.getResources(), srcId, opt);
        }
        //变形缩放
        else if(scaleWidth>1){
            Log.d("XDJ","scaleWidth>1");
            opt.inSampleSize=scale;
            opt.inJustDecodeBounds=false;
            Bitmap originBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);
            int newHeight=0;
            if(srcHeight*displayMetrics.widthPixels/srcWidth==0){
                newHeight=srcHeight;
            }else {
                newHeight=srcHeight*displayMetrics.widthPixels/srcWidth;
            }
            bitmapSmall=scaleBitmap(originBitmap, displayMetrics.widthPixels,newHeight,true);
        }
        //变形缩放
        else if(scaleHeight>1){
            Log.d("XDJ","scaleHeight>1");
            opt.inSampleSize=scale;
            opt.inJustDecodeBounds=false;
            Bitmap originBitmap=BitmapFactory.decodeResource(mContext.getResources(), srcId,opt);
            int newWidth=0;
            if(srcWidth*displayMetrics.heightPixels/srcHeight==0){
                newWidth=srcWidth;
            }else {
                newWidth=srcWidth*displayMetrics.heightPixels/srcHeight;
            }
            bitmapSmall=scaleBitmap(originBitmap,newWidth,displayMetrics.heightPixels,true);
        }else {
            Log.d("XDJ","scaleHeight<1 && scaleWidth<1");
            opt.inSampleSize=scale;
            opt.inJustDecodeBounds=false;
            bitmapSmall=BitmapFactory.decodeResource(mContext.getResources(), srcId, opt);
        }

        mWidth=bitmapSmall.getWidth();
        mHeight=bitmapSmall.getHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取宽度的模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        //获取高度的模式和大小
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        //设置View的宽高
        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    private int measureWidth(int widthMode, int width) {
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
//        Log.d("XDJ", "2---mWidth=" + mWidth);
        return mWidth;
    }

    private int measureHeight(int heightMode, int height) {
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
        }
//        Log.d("XDJ", "2---mHeight=" + mHeight);
        return mHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(R.color.white);
        canvas.drawBitmap(bitmapSmall, 0, 0, mPaint);
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

    //获取圆角矩形的图片
    public Bitmap getCornerBitmapSmall(){
        //再次缩小图片
        if(bitmapSmallerRect == null){
            Log.d("XDJ","bitmapSmallerRect == null");
            if(bitmapSmall.getWidth()==displayMetrics.widthPixels ||
                    bitmapSmall.getHeight()==displayMetrics.heightPixels){
                bitmapSmallerRect =scaleBitmap(bitmapSmall,bitmapSmall.getWidth()/2,bitmapSmall.getHeight()/2,false);
            }else {
                bitmapSmallerRect =scaleBitmap(bitmapSmall,bitmapSmall.getWidth(),bitmapSmall.getHeight(),false);
            }

            int width= bitmapSmallerRect.getWidth();
            int height= bitmapSmallerRect.getHeight();
            int conrner=DensityUtil.dp2px(mContext, 20);
            Bitmap outBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(outBitmap);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(mContext.getResources().getColor(R.color.gray));
            canvas.drawRoundRect(0, 0, width, height, conrner, conrner, mPaint);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setXfermode(duffXfermode);
            canvas.drawBitmap(bitmapSmallerRect, 0, 0, mPaint);
            bitmapSmallerRect=outBitmap;
        }

        return bitmapSmallerRect;
    }

    //获取圆形的图片
    public Bitmap getCircleBitmapSmall(){
        if(bitmapSmallCircle==null){
            Log.d("XDJ","bitmapSmallCircle == null");
            //再次缩小图片
            if(bitmapSmall.getWidth()==displayMetrics.widthPixels ||
                    bitmapSmall.getHeight()==displayMetrics.heightPixels){
                bitmapSmallCircle=scaleBitmap(bitmapSmall,bitmapSmall.getWidth()/2,bitmapSmall.getHeight()/2,false);
            }else {
                bitmapSmallCircle=scaleBitmap(bitmapSmall,bitmapSmall.getWidth(),bitmapSmall.getHeight(),false);
            }

            int width=bitmapSmallCircle.getWidth();
            int height=bitmapSmallCircle.getHeight();
            int radius=(width>height?height:width)/2;
            Bitmap outBitmap=Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas=new Canvas(outBitmap);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(mContext.getResources().getColor(R.color.gray));
            canvas.drawCircle(width / 2, height / 2, radius, mPaint);
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setXfermode(duffXfermode);
            canvas.drawBitmap(bitmapSmallCircle, 0, 0, mPaint);
            bitmapSmallCircle=outBitmap;
        }

        return bitmapSmallCircle;
    }
}

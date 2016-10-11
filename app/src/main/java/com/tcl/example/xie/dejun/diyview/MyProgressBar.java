package com.tcl.example.xie.dejun.diyview;

import android.app.VoiceInteractor;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

/**
 * Created by dejun.xie on 2016/3/25.
 */
public class MyProgressBar extends View{

    public int roundColor;
    public int roundProgressColor;
    public int backgroudColor;
    public int innerCircle;
    public int ringWidth;
    Paint paint;
    public int height;
    public int width;
    public int imgInside;
    public Bitmap bitmap;
    int center;
    BitmapFactory.Options options;
    RectF oval;
    public static final int START_ANGLE=270;
    public static final int ARC_RANGE=120;
    RotateAnimation ra;
//    int[] paintColors =new int[]{0x0000ff00,0x1100ff00,0x2200ff00,0x3300ff00,0x4400ff00,0x5500ff00,
//            0x6600ff00,0x7700ff00,0x8800ff00,0x9900ff00,0xaa00ff00,0xbb00ff00,0xcc00ff00,
//            0xdd00ff00,0xee00ff00};
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
//                    startAngle=(startAngle+ROTATE_SPACE)%360;
//                    invalidate();//该语句会去调用onDraw()方法
                    Log.d("XDJ", "startRotate()");
                    startRotate();
//                    handler.sendEmptyMessageDelayed(1,1000);
                    break;
                case 2:
                    ra.cancel();//注意动画的停止，需要自己选择合适的时机去停止
                default:
                    break;
            }
        }
    };

    public MyProgressBar(Context context) {
        super(context);
        Log.d("XDJ", "111");
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("XDJ", "222");
        init(context, attrs);
//        handler.sendEmptyMessageDelayed(1, 30);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("XDJ", "333");
        init(context, attrs);
    }

    void init(Context context,AttributeSet attrs){
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.myProgressBar);
        roundColor = typedArray.getColor(R.styleable.myProgressBar_roundColor, Color.RED);
        roundProgressColor = typedArray.getColor(R.styleable.myProgressBar_roundProgressColor, Color.GREEN);
        backgroudColor=typedArray.getColor(R.styleable.myProgressBar_backgroudColor,Color.GRAY);
        innerCircle= (int) typedArray.getDimension(R.styleable.myProgressBar_innerCircle,20);
        ringWidth= (int) typedArray.getDimension(R.styleable.myProgressBar_ringWidth,10);
        //中间图片的初始化
//        imgInside=typedArray.getResourceId(R.styleable.myProgressBar_imgInside, -1);
//        options=new BitmapFactory.Options();
//        options.inJustDecodeBounds=true;
//        bitmap=BitmapFactory.decodeResource(getResources(), imgInside, options);
//        scaleImgInside();

        typedArray.recycle();
        //初始化画笔
        paint=new Paint();
        paint.setAntiAlias(true);//防锯齿
        paint.setDither(true); //防抖动
//        paint.setStyle(Paint.Style.STROKE);//画出空心图
        handler.sendEmptyMessageDelayed(1, 100);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("XDJ","innerCircle="+innerCircle+",ringWidth="+ringWidth);
        width=DensityUtil.dp2px(getContext(), (innerCircle + ringWidth+ 2) * 2);
        height=DensityUtil.dp2px(getContext(), (innerCircle + ringWidth+ 2) * 2);
        setMeasuredDimension(width, height);
        Log.d("XDJ", "MyProgress onMeasure---width="+width+",height="+height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d("XDJ", "MyProgress onDraw");
//        Log.d("XDJ","Log.getStackTraceString(new Throwable())="+Log.getStackTraceString(new Throwable()));
//        this.canvas=canvas;
//        Log.d("XDJ", "getWidth()=" + getWidth());
//        Log.d("XDJ","getHeight()="+getHeight());
//        int center = getWidth()/2;
        center = width/2;
        int innerCircle = DensityUtil.dp2px(getContext(), this.innerCircle); //设置内圆半径
        int ringWidth = DensityUtil.dp2px(getContext(), this.ringWidth); //设置圆环宽度

        //绘制背景
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroudColor);
        canvas.drawCircle(center, center, innerCircle+ringWidth+2, paint);

        //绘制圆环
//        paint.setARGB(255, 0, 255, 0);
        paint.setStyle(Paint.Style.STROKE);//画出空心图
        paint.setColor(roundProgressColor);
        paint.setStrokeWidth(ringWidth-2);
//        canvas.drawCircle(center, center, innerCircle + ringWidth / 2 + 1, paint);
        oval=new RectF(center-innerCircle-ringWidth/2,center-innerCircle-ringWidth/2,
                center+innerCircle+ringWidth/2,center+innerCircle+ringWidth/2);

//        gradient=new LinearGradient(center-innerCircle-ringWidth,center-innerCircle-ringWidth,
//                center+innerCircle+ringWidth+2,center+innerCircle+ringWidth+2,
//                0x0000ff00,0xff00ff00,Shader.TileMode.CLAMP);

        LinearGradient gradient=new LinearGradient(center,0,width,height,
                0x0000ff00,0xff00ff00,Shader.TileMode.REPEAT);
        paint.setShader(gradient);

        canvas.drawArc(oval, START_ANGLE, ARC_RANGE, false, paint);
        paint.setShader(null);

        //绘制内圆
//        Log.d("XDJ", "paint=" + paint);
//        paint.setARGB(255, 255, 0, 0);
        paint.setColor(roundColor);
        paint.setStrokeWidth(2);
        canvas.drawCircle(center, center, innerCircle, paint);

        //绘制外圆
        paint.setStrokeWidth(2);
        canvas.drawCircle(center, center, innerCircle + ringWidth + 1, paint);

        paint.setColor(0x00000000);//设置画笔颜色透明

        //绘制中间的图片
//        canvas.drawBitmap(bitmap, center - innerCircle / 2, center - innerCircle / 2, null);

//        handler.sendEmptyMessageDelayed(1,100);
    }

    private void startRotate() {
        ra=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(1000);
        ra.setFillAfter(true);
        ra.setRepeatCount(-1);
//        ra.setRepeatMode(Animation.RESTART);
        this.startAnimation(ra);
//        handler.sendEmptyMessageDelayed(2,3000);
    }

    public void scaleImgInside(){
        int bitHeight=options.outHeight;
        int bitWidth=options.outWidth;
        int scale=1;
        int scaleWidth=bitWidth/(innerCircle);
        int scaleHeight=bitHeight/(innerCircle);
        if(scaleWidth>1 || scaleHeight>1){
            scale=scaleWidth>scaleHeight?scaleWidth:scaleHeight;
        }

        options.inSampleSize=scale;
        options.inJustDecodeBounds=false;
        bitmap=BitmapFactory.decodeResource(getResources(), imgInside, options);
//        Log.d("XDJ", "222---bitmap="+bitmap);
    }
}

package com.tcl.example.xie.dejun.diyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * Created by dejun.xie on 2016/6/15.
 */
public class MyCircle extends View {

    private static final int DEFAULT_RADIUS=20;//圆的半径
    private static final int DEFAULT_PAINTWIDTH=10;//画笔宽度

    Context mContext;
    private int width;
    private int height;
    private Paint paint;

    public Float getRotate() {
        return rotate;
    }

    public void setRotate(Float rotate) {
        this.rotate = rotate;
    }

    private Float rotate;

    //构造代码块
    {
        init();
    }

    private void init() {
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    public MyCircle(Context context) {
        super(context);
        mContext=context;
    }

    public MyCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        //获取自定义属性
        TypedArray typedArray=mContext.obtainStyledAttributes(attrs, R.styleable.myCircle);
        rotate= typedArray.getFloat(R.styleable.myCircle_rotation, 0.0f);
        typedArray.recycle();
    }

    public MyCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width=calculate(widthMeasureSpec);
        height=calculate(heightMeasureSpec);
        setMeasuredDimension(width, height);

        //设置画笔的为环形渲染效果
        initPaint();
        Log.d("XDJ","MyCircle onMeasure()");
    }

    private void initPaint() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(DensityUtil.dp2px(mContext, DEFAULT_PAINTWIDTH));
//        paint.setColor(getResources().getColor(R.color.red));

//        int[] colors=new int[]{Color.GREEN,
//                Color.RED, Color.BLUE, Color.WHITE,Color.YELLOW};
//        float[] positions=new float[]{0.4f,0.5f,0.6f,0.8f};
//        int[] colors=new int[]{Color.GREEN,Color.YELLOW,Color.RED};
        int[] colors=new int[]{Color.RED,Color.YELLOW,Color.GREEN};
//        Log.d("XDJ","width/2="+width/2+",height/2="+height/2);
        SweepGradient sweepGradient=new SweepGradient(width/2,height/2,colors,null);
        paint.setShader(sweepGradient);
    }

    private int calculate(int measureSpec) {
        //默认的宽高
        int result= 2*(DensityUtil.dp2px(mContext,DEFAULT_RADIUS)+DensityUtil.dp2px(mContext,DEFAULT_PAINTWIDTH));

        int specMode=MeasureSpec.getMode(measureSpec);
        int specSize=MeasureSpec.getSize(measureSpec);

        if(specMode==MeasureSpec.EXACTLY){
//            Log.d("XDJ","specMode==MeasureSpec.EXACTLY");
            result=specSize;
        }else if(specMode==MeasureSpec.AT_MOST){
//            Log.d("XDJ","specMode==MeasureSpec.AT_MOST");
            result=Math.min(result,specSize);
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(width / 2, height / 2, DensityUtil.dp2px(mContext, DEFAULT_RADIUS), paint);
        canvas.save();
        canvas.rotate(rotate,width / 2,height / 2);
        canvas.drawCircle(width / 2, height / 2, DensityUtil.dp2px(mContext, DEFAULT_RADIUS), paint);
//        postInvalidate();
//        Log.d("XDJ","MyCircle onDraw()");
        canvas.restore();
    }
}

package com.tcl.example.xie.dejun.diyview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by dejun.xie on 2016/7/19.
 */
public class RectTextView extends TextView {

    private final Paint paint_1 = new Paint();
    private final Paint paint_2 = new Paint();
    private Matrix translateMatrix = new Matrix();
    private int traslateDistance = 0;
    private LinearGradient linearGradient;

    public RectTextView(Context context) {
        super(context);
    }

    public RectTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        initPaint();
        drawRect(canvas);
        splashText();
        super.onDraw(canvas);
        canvas.restore();

//        splashText();
//        super.onDraw(canvas);
//        //渐变画笔，打造渐变闪烁的文本效果
//        traslateDistance+=getMeasuredWidth()/5;
//        if(traslateDistance>getMeasuredWidth()){
//            traslateDistance=0;
//        }
//        translateMatrix.setTranslate(traslateDistance,0);
//        linearGradient.setLocalMatrix(translateMatrix);
//        postInvalidateDelayed(100);
    }

    private void splashText() {
        int[] colors=new int[]{R.color.red,R.color.blue};
        linearGradient = new LinearGradient(0,0,getMeasuredWidth(),0,
                colors,null, Shader.TileMode.CLAMP);
        getPaint().setShader(linearGradient);
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint_1);
        canvas.drawRect(10,10,getMeasuredWidth()-10,getMeasuredHeight()-10,paint_2);
        canvas.save();
        canvas.translate(10,0);
    }

    private void initPaint() {
        paint_1.setColor(getResources().getColor(R.color.red));
        paint_1.setStyle(Paint.Style.FILL);
//        paint_1.setStrokeWidth(5.0f);

        paint_2.setColor(getResources().getColor(R.color.white));
        paint_2.setStyle(Paint.Style.FILL);
//        paint_2.setStrokeWidth(5.0f);
    }
}

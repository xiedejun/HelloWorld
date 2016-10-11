package com.tcl.example.com.tcl.dejun.xie.ListViewUsage;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/5.
 */
public class LeftWrapperItem extends FrameLayout {

    Context mContext;
    LinearLayout llContent;
    LinearLayout llDrag;
    private int llDragWidth;
    private int llDragHeight;
    private WindowManager windowManager;
    private DisplayMetrics displayMetrics;
    private TextView tvDelete;
    private TextView tvTop;
    private TextView tvTab;
    private LeftDragItemView parentView;
    private ImageView ivContent;
    private TextView tvContent;

    public LinearLayout getLlDrag() {
        return llDrag;
    }

    public LeftWrapperItem(Context context) {
        super(context);
        initView(context);
    }

    public LeftWrapperItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LeftWrapperItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context){
        mContext=context;
//        addView(View.inflate(mContext, R.layout.left_drag_content, null));
//        addView(View.inflate(mContext, R.layout.letf_drag_view, null));

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        llContent= (LinearLayout) findViewById(R.id.ll_content);
//        llDrag= (LinearLayout) findViewById(R.id.ll_drag);
        llContent= (LinearLayout) getChildAt(0);
        llDrag= (LinearLayout) getChildAt(1);
        findViews();
    }


    public ImageView getIvContent() {
        return ivContent;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    private void findViews() {
        //llContent的子View
        ivContent = (ImageView) findViewById(R.id.iv_right);
        tvContent = (TextView) findViewById(R.id.tv_left);

        //llDrag的子View
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvTop = (TextView) findViewById(R.id.tv_top);
        tvTab = (TextView) findViewById(R.id.tv_tab);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        llDrag.measure(0, 0);
        llDragWidth=llDrag.getMeasuredWidth();
        llDragHeight=llDrag.getMeasuredHeight();
//        Log.d("XDJ","llDragWidth="+llDragWidth+",llDragHeight="+llDragHeight);

        int totalWidth = 0;
        int totalHeight = 0;


        for(int i=0;i<getChildCount();i++){
            int width=0;
            int height=0;

            View child=getChildAt(i);

            measureChild(child,widthMeasureSpec,heightMeasureSpec);
            width=child.getMeasuredWidth();
            height=child.getMeasuredHeight();

            totalWidth +=width;

            if(totalHeight <height){
                totalHeight =height;
            }
        }

//        Log.d("XDJ","LeftWrapperItem onMeasure(),totalWidth="+totalWidth);
        setMeasuredDimension(totalWidth, totalHeight);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        Log.d("XDJ", "LeftWrapperItem onLayout right=" + right + ",bottom=" + bottom);
//        Log.d("XDJ","llDragWidth="+llDragWidth+",llDragHeight="+llDragHeight);
        llDrag.layout(0, 0, llDragWidth, bottom);
        llContent.layout(llDragWidth, 0, right + llDragWidth, bottom);
    }

    public void setParentView(LeftDragItemView parentView) {
        this.parentView = parentView;
    }
}

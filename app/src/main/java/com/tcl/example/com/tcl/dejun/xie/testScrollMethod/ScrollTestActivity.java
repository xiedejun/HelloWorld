package com.tcl.example.com.tcl.dejun.xie.testScrollMethod;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/7/21.
 */
public class ScrollTestActivity extends Activity implements View.OnClickListener {

    private TextView tv;
    private Button btUp;
    private Button btDown;
    private Button btLeft;
    private Button btRight;
    public final int TRANSFER_STEP=30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);
        findViews();
        initTextViewListener();
    }

    private void findViews() {
        tv = (TextView) findViewById(R.id.tv_center);
        btUp = (Button) findViewById(R.id.bt_up);
        btDown = (Button) findViewById(R.id.bt_down);
        btLeft = (Button) findViewById(R.id.bt_left);
        btRight = (Button) findViewById(R.id.bt_right);
    }

    private void initTextViewListener() {
        btUp.setOnClickListener(this);
        btDown.setOnClickListener(this);
        btLeft.setOnClickListener(this);
        btRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_up:
                tv.scrollBy(0, TRANSFER_STEP);
                Log.d("XDJ", "UP after,tv.getScrollX=" + tv.getScrollX() +
                        ",tv.getScrollY()=" + tv.getScrollY());
                break;
            case R.id.bt_down:
                tv.scrollBy(0,-TRANSFER_STEP);
                Log.d("XDJ", "DOWN after,tv.getScrollX=" + tv.getScrollX() +
                        ",tv.getScrollY()=" + tv.getScrollY());
                break;
            case R.id.bt_left:
                tv.scrollBy(TRANSFER_STEP,0);
                Log.d("XDJ", "LEFT after,tv.getScrollX=" + tv.getScrollX() +
                        ",tv.getScrollY()=" + tv.getScrollY());
                break;
            case R.id.bt_right:
                tv.scrollBy(-TRANSFER_STEP,0);
                Log.d("XDJ", "RIGHT after,tv.getScrollX=" + tv.getScrollX() +
                        ",tv.getScrollY()=" + tv.getScrollY());
                break;
            default:
                break;
        }
    }
}

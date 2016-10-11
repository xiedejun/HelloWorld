package com.tcl.example.com.tcl.dejun.xie.PorterDuffDemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.tcl.example.xie.dejun.diyview.R;

/**
 * Created by dejun.xie on 2016/8/17.
 */
public class PorterDuffActivity extends Activity implements View.OnClickListener {

    private ImageView iv;
    private PorterDuffView duffView;
    private Button btRoundRect;
    private Button btCirclr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_porter_duff);
        findViews();
        initViews();
        initListeners();
    }

    private void initListeners() {
        btRoundRect.setOnClickListener(this);
        btCirclr.setOnClickListener(this);
    }

    private void initViews() {
    }

    private void findViews() {
        iv = (ImageView) findViewById(R.id.iv_below);
        duffView = (PorterDuffView) findViewById(R.id.porter_duff_view);
        btRoundRect = (Button) findViewById(R.id.bt_round_rect);
        btCirclr = (Button) findViewById(R.id.bt_circle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_round_rect:
                iv.setImageBitmap(duffView.getCornerBitmapSmall());
                break;
            case R.id.bt_circle:
                iv.setImageBitmap(duffView.getCircleBitmapSmall());
                break;
        }
    }
}

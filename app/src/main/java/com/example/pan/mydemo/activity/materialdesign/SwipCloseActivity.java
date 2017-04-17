package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.view.SwipBackLayout;

public class SwipCloseActivity extends BaseActivity {

    Toolbar toolbar;
    SwipBackLayout swipBackLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip_close);
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("右滑关闭页面");
        setSupportActionBar(toolbar);

        dynamicAddSkinEnableView(toolbar,"background",R.color.colorPrimary);


        swipBackLayout = (SwipBackLayout)findViewById(R.id.swip_back_layout);
        swipBackLayout.setSwipBackListener(new SwipBackLayout.OnSwipListener() {
            @Override
            public void onEnd() {
                finish();
                overridePendingTransition(0,0);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch( keyCode ){
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.in_fade_and_big,R.anim.out_to_right);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}

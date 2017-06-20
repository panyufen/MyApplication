package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class FlexBoxLayoutActivity extends BaseActivity {


    /**
     * 文档地址：http://www.jianshu.com/p/b3a9c4a99053
     * 需要依赖：compile 'com.google.android:flexbox:0.2.6'
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flex_box_layout);
        setSupportActionBar(R.id.tool_bar);
    }
}

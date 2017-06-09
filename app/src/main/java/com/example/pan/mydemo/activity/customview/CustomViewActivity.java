package com.example.pan.mydemo.activity.customview;

import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class CustomViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        setSupportActionBar(R.id.tool_bar);
    }
}

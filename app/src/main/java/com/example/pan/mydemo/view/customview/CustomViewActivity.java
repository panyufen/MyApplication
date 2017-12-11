package com.example.pan.mydemo.view.customview;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.CalendarActivity;
import com.example.pan.mydemo.view.base.BaseActivity;

public class CustomViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        setSupportActionBar(R.id.tool_bar);
    }

    public void startCircleRectView(View v) {
        startActivity(CircleRectViewActivity.class);
    }

    public void startZoom(View v) {
        startActivity(ZoomActivity.class);
    }

    public void startCalendar(View v) {
        startActivity(CalendarActivity.class);
    }
}

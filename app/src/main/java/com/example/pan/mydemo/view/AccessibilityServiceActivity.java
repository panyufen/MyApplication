package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

public class AccessibilityServiceActivity extends BaseActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility_service);

        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("Accessibility Service");

    }
}

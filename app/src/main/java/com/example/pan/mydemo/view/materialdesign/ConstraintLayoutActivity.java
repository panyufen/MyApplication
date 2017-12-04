package com.example.pan.mydemo.view.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

public class ConstraintLayoutActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("ConstraintLayoutActivity Activity");
    }
}

package com.example.pan.mydemo.activity;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class OpenGLActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl);
        setSupportActionBar(R.id.tool_bar);
    }

    public void startOpenGLSample(View v) {
        startActivity(OpenGLSampleActivity.class);
    }

    public void startOpenGL20Sample(View v){
        startActivity(OpenGL20SampleActivity.class);
    }
}

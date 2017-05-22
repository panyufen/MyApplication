package com.example.pan.mydemo.activity.opengl;

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

    public void startOpenGL20Sample(View v) {
        startActivity(OpenGL20SampleActivity.class);
    }

    public void startProjection(View v) {
        startActivity(OpenGLProjectionActivity.class);
    }

    public void startOpenGLLight(View v) {
        startActivity(OpenGLLightActivity.class);
    }

    public void startOpenGLTexture(View view){
        startActivity(OpenGLTextureActivity.class);
    }
}

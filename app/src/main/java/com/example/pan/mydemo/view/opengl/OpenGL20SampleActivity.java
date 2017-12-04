package com.example.pan.mydemo.view.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.opengl.CubeRenderer;

public class OpenGL20SampleActivity extends BaseActivity {

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gl20_sample);
        mGLSurfaceView = (GLSurfaceView)findViewById(R.id.gl_surface_view);
        mGLSurfaceView.setRenderer(new CubeRenderer());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}

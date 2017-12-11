package com.example.pan.mydemo.view.opengl;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.opengl.CubeRenderer;

public class OpenGLSampleActivity extends BaseActivity {

    private GLSurfaceView mGLSurfaceView;

    private SeekBar mSeekBarY;
    private SeekBar mSeekBarZ;

    private CubeRenderer mCubeRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_glsample);
        initView();
    }

    private void initView() {
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
        mCubeRenderer = new CubeRenderer();
        mGLSurfaceView.setRenderer(mCubeRenderer);

        mSeekBarY = (SeekBar) findViewById(R.id.seek_bar_yindex);
        mSeekBarY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCubeRenderer.setYIndex(((float) progress - 50) / 15);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        mSeekBarZ = (SeekBar) findViewById(R.id.seek_bar_zindex);
        mSeekBarZ.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCubeRenderer.setZIndex((float) progress / 10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
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

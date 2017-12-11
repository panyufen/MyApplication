package com.example.pan.mydemo.view.opengl;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.opengl.LightSurfaceView;

public class OpenGLLightActivity extends BaseActivity {

    private ToggleButton mToggleButton;
    private LightSurfaceView mLightSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_gllight);

        init();

    }

    private void init() {
        mLightSurfaceView = (LightSurfaceView) findViewById(R.id.light_surface_view);
        mLightSurfaceView.setFocusableInTouchMode(true);
        mLightSurfaceView.setFocusable(true);

        mToggleButton = (ToggleButton) findViewById(R.id.light_toggle_btn);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLightSurfaceView.openLightFlag = !mLightSurfaceView.openLightFlag;
                mLightSurfaceView.requestRender();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLightSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLightSurfaceView.onPause();
    }
}

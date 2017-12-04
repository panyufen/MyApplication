package com.example.pan.mydemo.view.opengl;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.opengl.ProjectionSurfaceView;

public class OpenGLProjectionActivity extends BaseActivity {

    private ToggleButton mToggleButton;
    private ProjectionSurfaceView mSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projection);
        init();
    }


    private void init() {
        mSurfaceView = (ProjectionSurfaceView) findViewById(R.id.projection_surface_view);
        mSurfaceView.requestFocus();
        mSurfaceView.setFocusableInTouchMode(true);

        mToggleButton = (ToggleButton) findViewById(R.id.projection_toggle_btn);
        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSurfaceView.isPerspecttive = !mSurfaceView.isPerspecttive;
                mSurfaceView.requestRender();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }
}

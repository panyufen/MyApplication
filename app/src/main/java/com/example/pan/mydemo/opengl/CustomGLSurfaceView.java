package com.example.pan.mydemo.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by PAN on 2017/2/6.
 */

public class CustomGLSurfaceView extends GLSurfaceView {

    private float TOUCH_ANGLE_FACTOR = 1.0f;


    public CustomGLSurfaceView(Context context) {
        super(context);
    }

    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}

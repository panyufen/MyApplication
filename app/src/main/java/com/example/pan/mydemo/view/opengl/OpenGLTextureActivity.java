package com.example.pan.mydemo.view.opengl;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.opengl.SkySurfaceView;
import com.example.pan.mydemo.utils.Constant;

public class OpenGLTextureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为横屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_open_gltexture);

        //初始化GLSurfaceView
        mGLSurfaceView = (SkySurfaceView) findViewById(R.id.sky_surface_view);

        mGLSurfaceView = new SkySurfaceView(this);

        mGLSurfaceView.requestFocus();//获取焦点
        mGLSurfaceView.setFocusableInTouchMode(true);//设置为可触控
    }


    private SkySurfaceView mGLSurfaceView;

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
        Constant.threadFlag = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
        Constant.threadFlag = false;
    }
}

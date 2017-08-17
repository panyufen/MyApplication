package com.example.pan.mydemo.activity.customview;

import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.view.ZoomImageView;
import com.pan.skin.loader.config.SkinConfig;

public class ZoomActivity extends BaseActivity {

    private ZoomImageView zoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        setTranslucentStatus();
        setStatusTextColor(SkinConfig.isLightSkin(this));
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        zoomImageView.setImageDrawable(getResources().getDrawable(R.mipmap.s20150612_195215166));
    }
}

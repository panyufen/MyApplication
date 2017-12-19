package com.example.pan.mydemo.view.customview;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.widget.ZoomImageView;
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
        if (getIntent().getStringExtra("imageUrl") != null) {
            zoomImageView.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("imageUrl")));
        } else {
            zoomImageView.setImageDrawable(getResources().getDrawable(R.drawable.s20150612_195215166));
        }
    }
}

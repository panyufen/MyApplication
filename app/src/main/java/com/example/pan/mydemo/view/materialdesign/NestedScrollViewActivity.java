package com.example.pan.mydemo.view.materialdesign;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.widget.ImageView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.pan.skin.loader.config.SkinConfig;

public class NestedScrollViewActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener {

    private AppBarLayout mAppBarLayout;

    private NestedScrollView mScrollView;

    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus();
        setContentView(R.layout.activity_nest_scrollview);
        setSupportActionBar(R.id.tool_bar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangeListener(this);

        image_view = (ImageView) findViewById(R.id.image_view);
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        float height = image_view.getMeasuredHeight();
        mAppBarLayout.setAlpha((scrollY - mAppBarLayout.getTop()) / (height - mAppBarLayout.getTop()));
        if (scrollY + mAppBarLayout.getBottom() >= height) {
            setTranslucentStatus(false, R.color.colorPrimaryDark);
            setStatusTextColor(SkinConfig.isLightSkin(this));
            changeStatusColor();
        } else {
            setTranslucentStatus();
            mAppBarLayout.setAlpha(0f);
        }
    }
}

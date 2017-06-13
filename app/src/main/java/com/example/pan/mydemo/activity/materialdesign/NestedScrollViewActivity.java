package com.example.pan.mydemo.activity.materialdesign;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class NestedScrollViewActivity extends BaseActivity implements NestedScrollView.OnScrollChangeListener {

    private Toolbar mToolbar;

    private NestedScrollView mScrollView;

    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslucentStatus(true);
        setContentView(R.layout.activity_nest_scrolling2);
        mToolbar = setSupportActionBar(R.id.tool_bar);
        mScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        mScrollView.setOnScrollChangeListener(this);

        image_view = (ImageView) findViewById(R.id.image_view);
    }

    private void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (on) {
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        float height = image_view.getMeasuredHeight();
        mToolbar.setAlpha((scrollY - mToolbar.getTop()) / (height - mToolbar.getTop()));
        if (scrollY + mToolbar.getBottom() >= height) {
            setTranslucentStatus(false);
        } else {
            setTranslucentStatus(true);
            mToolbar.setAlpha(0f);
        }
    }
}

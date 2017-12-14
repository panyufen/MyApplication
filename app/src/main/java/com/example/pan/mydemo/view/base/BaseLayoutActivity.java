package com.example.pan.mydemo.view.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.pan.mydemo.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by PAN on 2017/12/11.
 * 初始公共头部
 */

public class BaseLayoutActivity extends BaseActivity {

    protected Toolbar mToolbar;
    protected FrameLayout mContentContainer;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_activity_layout,false);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mContentContainer = (FrameLayout) findViewById(R.id.content_container);
        setSupportActionBar(R.id.tool_bar);
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, mContentContainer);
        init();
    }

    @Override
    public void setContentView(View view) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentContainer.addView(view, lp);
        init();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentContainer.addView(view, params);
        init();
    }

    private void init() {
        try {
            unbinder = ButterKnife.bind(this);
            if( !EventBus.getDefault().isRegistered(this) ) {
                EventBus.getDefault().register(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

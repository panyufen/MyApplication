package com.example.pan.mydemo.view.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.http.event.ResEvent;
import com.pan.skin.loader.base.SkinBaseActivity;
import com.pan.skin.loader.config.SkinConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * Created by PAN on 2016/9/23.
 */
public class BaseActivity extends SkinBaseActivity {
    String TAG = this.getClass().getName();
    private Toolbar toolbar;

    private boolean isShowToolbarNavIcon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void setToolbarNavIconVisible(ActionBar actionbar, boolean visible) {
        isShowToolbarNavIcon = visible;
        actionbar.setDisplayHomeAsUpEnabled(visible);
    }

    @Override
    public void setSupportActionBar(@Nullable android.support.v7.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
    }

    @Override
    public void onThemeUpdate() {
        super.onThemeUpdate();
        if (isShowToolbarNavIcon) {
            setToolbarIconStyle();
        }
    }

    protected Toolbar setSupportActionBar(int res_toolbar) {
        toolbar = (Toolbar) findViewById(res_toolbar);
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
        dynamicAddSkinEnableView(toolbar, "titleTextColor", R.color.normal_text_color);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && isShowToolbarNavIcon) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setToolbarIconStyle();
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
        return toolbar;
    }

    private void setToolbarIconStyle() {
        if (toolbar == null) return;
        if (SkinConfig.isLightSkin(this)) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.nav_back_dark));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.mipmap.nav_back_light));
        }
    }

    protected Toolbar setSupportActionBar(int res_toolbar, String title) {
        Toolbar toolbar = (Toolbar) findViewById(res_toolbar);
        toolbar.setTitle(title);
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
        return toolbar;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ResEvent event) {

    }

}

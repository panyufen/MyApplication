package com.example.pan.mydemo.activity.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import com.example.pan.mydemo.R;
import com.pan.skin.loader.base.SkinBaseActivity;

/**
 * Created by PAN on 2016/9/23.
 */
public class BaseActivity extends SkinBaseActivity {
    String TAG = this.getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void setSupportActionBar(@Nullable android.support.v7.widget.Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
    }

    protected Toolbar setSupportActionBar(int res_toolbar) {
        Toolbar toolbar = (Toolbar) findViewById(res_toolbar);
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
        return toolbar;
    }

    protected Toolbar setSupportActionBar(int res_toolbar,String title) {
        Toolbar toolbar = (Toolbar) findViewById(res_toolbar);
        toolbar.setTitle(title);
        super.setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);
        return toolbar;
    }
}

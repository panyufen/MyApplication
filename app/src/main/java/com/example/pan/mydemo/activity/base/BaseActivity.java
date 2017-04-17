package com.example.pan.mydemo.activity.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

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

//        int fullFlag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        LogUtils.i("flat " + getWindow().getAttributes().flags + " " + fullFlag + " " + (getWindow().getAttributes().flags & fullFlag));

//        if ((getWindow().getAttributes().flags & fullFlag) != fullFlag) {
//            //解决ActiionBar上的黑条 暂不知是什么原因导致
//            setTranslucentStatus();
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            int color = SkinManager.getInstance().getColorPrimaryDark();
//            if (color == -1) {
//                color = getResources().getColor(R.color.colorPrimary);
//            }
//            tintManager.setStatusBarTintColor(color);
//        }
    }


    @TargetApi(19)
    private void setTranslucentStatus() {
        android.util.Log.i("flags ", "class name " + getClass().getSimpleName() + " setTranslucent");
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        getWindow().setAttributes(winParams);

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

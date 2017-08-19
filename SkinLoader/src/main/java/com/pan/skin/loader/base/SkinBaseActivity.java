package com.pan.skin.loader.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.pan.skin.loader.attr.DynamicAttr;
import com.pan.skin.loader.config.SkinConfig;
import com.pan.skin.loader.listener.IDynamicNewView;
import com.pan.skin.loader.listener.ISkinUpdate;
import com.pan.skin.loader.load.SkinInflaterFactory;
import com.pan.skin.loader.load.SkinManager;

import java.util.List;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:24
 * <p>
 * 需要实现换肤功能的Activity就需要继承于这个Activity
 */
public class SkinBaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    public boolean isLight;

    // 当前Activity是否需要响应皮肤更改需求
    private boolean isResponseOnSkinChanging = true;
    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSkinInflaterFactory = new SkinInflaterFactory();
        try {
            LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        } catch (Exception e) {

        }
        super.onCreate(savedInstanceState);
        changeStatusColor();
        setStatusTextColor(SkinConfig.isLightSkin(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    @Override
    public void onThemeUpdate() {
        Log.i("SkinBaseActivity", "onThemeUpdate");
        if (!isResponseOnSkinChanging) {
            return;
        }
        mSkinInflaterFactory.applySkin();
        changeStatusColor();
        setStatusTextColor(SkinConfig.isLightSkin(this));
    }

    public void changeStatusColor() {
        //如果当前的Android系统版本大于4.4则更改状态栏颜色
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.i("SkinBaseActivity", "changeStatus");
//            int color = SkinManager.getInstance().getColorPrimaryDark();
//            StatusBarBackground statusBarBackground = new StatusBarBackground(this, color);
//            if (color != -1)
//                statusBarBackground.setStatusBarbackColor();
//        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = SkinManager.getInstance().getColorPrimaryDark();
            Log.i("SkinBaseActivity", "color " + color);
            Window window = getWindow();
            if (color != -1) {
                window.setStatusBarColor(color);
            }
        }
    }

    public void setTranslucentStatus() {
        setTranslucentStatus(true, 0);
    }

    public void setTranslucentStatus(boolean on, int rescolor) {
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
                window.setStatusBarColor(getResources().getColor(rescolor));
            }
        }
    }

    public void setStatusTextColor(boolean light) {
        Log.i("setStatusTextColor ", light + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (light) {
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
        }
    }

    //DrawerLayout 专用设置透明状态栏
    public void setTranslucaentStatusBarForDrawer(int color) {
        Log.i("statusBarColor ", color + " ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            ViewGroup decorView = (ViewGroup) window.getDecorView();
            decorView.addView(createStatusBarView(color), 0);
        }
    }

    private View createStatusBarView(int color) {
        View mStatusBarTintView = new View(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
        params.gravity = Gravity.TOP;
        mStatusBarTintView.setLayoutParams(params);
        mStatusBarTintView.setBackgroundColor(color);
        return mStatusBarTintView;

    }

    private int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }


    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    final protected void enableResponseOnSkinChanging(boolean enable) {
        isResponseOnSkinChanging = enable;
    }


}

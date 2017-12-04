package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cus.pan.library.utils.FileUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.pan.skin.loader.config.SkinConfig;
import com.pan.skin.loader.listener.ILoaderListener;
import com.pan.skin.loader.load.SkinManager;
import com.pan.skin.loader.util.L;

import java.io.File;

public class SkinLoaderActivity extends BaseActivity {
    private static final String SKIN_DIR = "skin/";
    private static final String SKIN_NAME = "WhiteSkinPackage.skin";
    private static String SKIN_EXTERNAL_DIR;

    private Toolbar mToolBar;

    private Button defaultThemeBtn;

    private Button darkThemeBtn;

    private boolean isOfficalSelected = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_loader);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        defaultThemeBtn = (Button) findViewById(R.id.set_default_skin);
        darkThemeBtn = (Button) findViewById(R.id.set_night_skin);
        SKIN_EXTERNAL_DIR = FileUtils.getSkinDirPath(this);
        init();
    }


    private void init() {
        mToolBar.setTitle("切换主题");

        isOfficalSelected = !SkinManager.getInstance().isExternalSkin();

        if (isOfficalSelected) {
            defaultThemeBtn.setText("官方默认(当前)");
            darkThemeBtn.setText("白色主题");
        } else {
            defaultThemeBtn.setText("官方默认");
            darkThemeBtn.setText("白色主题(当前)");
        }
    }

    public void setDefaultTheme(View v) {
        if (!isOfficalSelected) {
            SkinConfig.setIsLightSkin(SkinLoaderActivity.this, false);
            SkinManager.getInstance().restoreDefaultTheme();
            Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
            defaultThemeBtn.setText("官方默认(当前)");
            darkThemeBtn.setText("白色主题");
            isOfficalSelected = true;
        }
    }

    public void setDarkTheme(View v) {
        if (!isOfficalSelected) return;
        String skinFullName = SKIN_EXTERNAL_DIR + File.separator + SKIN_NAME;
        FileUtils.moveAssetsToDir(this, SKIN_DIR + SKIN_NAME, skinFullName, true);
        File skin = new File(skinFullName);
        Log.i("skinFilePath ", skinFullName);
        if (!skin.exists()) {
            Toast.makeText(getApplicationContext(), "请检查" + SKIN_EXTERNAL_DIR + "是否存在", Toast.LENGTH_SHORT).show();
            return;
        }

        SkinManager.getInstance().load(skin.getAbsolutePath(),
                new ILoaderListener() {
                    @Override
                    public void onStart() {
                        L.e("startloadSkin");
                    }

                    @Override
                    public void onSuccess() {
                        L.i("loadSkinSuccess");
                        Toast.makeText(getApplicationContext(), "切换成功", Toast.LENGTH_SHORT).show();
                        defaultThemeBtn.setText("官方默认");
                        darkThemeBtn.setText("白色主题(当前)");
                        SkinConfig.setIsLightSkin(SkinLoaderActivity.this, true);
                        isOfficalSelected = false;

                    }

                    @Override
                    public void onFailed() {
                        L.e("loadSkinFail");
                        Toast.makeText(getApplicationContext(), "切换失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package com.example.pan.mydemo.view;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebSettings;

import com.cus.pan.library.utils.BitmapUtils;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.jsbridge.BridgeImpl;
import com.example.pan.mydemo.jsbridge.JSBridge;
import com.example.pan.mydemo.jsbridge.JSBridgeWebChromeClient;

public class WebJsBridgeActivity extends WebActivity {

    @Override
    public void initExternal() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            LogUtils.i("addApptask start");
            Bitmap bitmap = BitmapUtils.drawableToBitmap(getResources().getDrawable(R.drawable.device_2017_01_09_140107));
            ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            int result = manager.addAppTask(this, getIntent(), null, bitmap);
            LogUtils.i("addApptask "+result);
        }

        super.initExternal();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new JSBridgeWebChromeClient());
        JSBridge.register("bridge", BridgeImpl.class);
    }
}

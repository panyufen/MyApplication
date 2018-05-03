package com.example.pan.mydemo.view;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

import butterknife.BindView;

public class WebActivity extends BaseActivity {

    public static String URL_KEY = "url_key";
    public static String TITLE_KEY = "title_key";
    private Toolbar mToolBar;
    protected WebView mWebView;
    @BindView(R.id.frame_layout)
    public FrameLayout mFrameLayout;

    private MyWebChromeClient mMyWebChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mToolBar = setSupportActionBar(R.id.tool_bar);
        mWebView = (WebView) findViewById(R.id.webview);
        init();
        //"http://blog.csdn.net/angel1hao/article/details/52094475"
        mWebView.loadUrl("http://m.tv.sohu.com");
    }

    public void initExternal() {}

    private void init() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setAllowFileAccess(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(mMyWebChromeClient);
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private class MyWebChromeClient extends WebChromeClient {
        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            getSupportActionBar().hide();
            mCustomView = view;
            mFrameLayout.addView(mCustomView);
            mCustomViewCallback = callback;
            mWebView.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        public void onHideCustomView() {
            mWebView.setVisibility(View.VISIBLE);
            if (mCustomView == null) {
                return;
            }
            getSupportActionBar().show();
            mCustomView.setVisibility(View.GONE);
            mFrameLayout.removeView(mCustomView);
            mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            super.onHideCustomView();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        switch (config.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }
}

package com.example.pan.mydemo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class WebActivity extends BaseActivity {

    public static String URL_KEY = "url_key";
    public static String TITLE_KEY = "title_key";
    private Toolbar mToolBar;
    protected WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        mToolBar = setSupportActionBar(R.id.tool_bar);
        webView = (WebView)findViewById(R.id.webview);
        init();
        //"http://blog.csdn.net/angel1hao/article/details/52094475"
        webView.loadUrl(getIntent().getStringExtra(URL_KEY));
    }

    public void initExternal(){

    }

    private void init() {
        WebSettings wSet = webView.getSettings();
        wSet.setRenderPriority(WebSettings.RenderPriority.HIGH);
        wSet.setCacheMode(WebSettings.LOAD_DEFAULT);
        wSet.setJavaScriptEnabled(true);
        wSet.setDomStorageEnabled(true);
        wSet.setAppCacheEnabled(true);
        wSet.setSupportZoom(true);
        wSet.setLoadWithOverviewMode(true);
        wSet.setUseWideViewPort(true);
        wSet.setBuiltInZoomControls(true);
        wSet.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        initExternal();

    }

}

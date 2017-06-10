package com.example.pan.mydemo.activity.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cus.pan.library.utils.FileUtils;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import java.io.File;

public class WebIntentActivity extends BaseActivity {

    private final String FILENAME = "webintent.html";

    private EditText mWebIntentUrlEt;
    private TextView mWebIntentContentTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_intent);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        LogUtils.i("filepath " + FileUtils.getCacheDir(this) + FILENAME);
        FileUtils.copyFileFromAssets(getApplicationContext(), FILENAME, FileUtils.getCacheDir(this) + File.separator + FILENAME);
        FileUtils.setPermission(FileUtils.getCacheDir(this) + File.separator + FILENAME);
        File file = new File(getExternalCacheDir() + File.separator + FILENAME);
        LogUtils.i("file " + file.exists());
        final Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileProvider", file);

        mWebIntentUrlEt = (EditText) findViewById(R.id.web_intent_path_et);
        mWebIntentUrlEt.setText(uri.toString());

        mWebIntentUrlEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                finish();
            }
        });


        mWebIntentContentTv = (TextView) findViewById(R.id.web_intent_content_tv);
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri data = intent.getData();
            String host = data.getHost();
            int port = data.getPort();
            String path = data.getPath();
            String query = data.getQuery();
            String id = data.getQueryParameter("id");
            String name = data.getQueryParameter("name");
            mWebIntentContentTv.setText("从浏览器得到的数据：url=\n" + data.toString());
        }
    }
}

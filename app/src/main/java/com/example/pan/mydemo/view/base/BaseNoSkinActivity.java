package com.example.pan.mydemo.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by PAN on 2017/2/18.
 */
public class BaseNoSkinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    protected Toolbar setSupportActionBar(int res_toolbar, String title) {
        Toolbar toolbar = (Toolbar) findViewById(res_toolbar);
        toolbar.setTitle(title);
        super.setSupportActionBar(toolbar);
        return toolbar;
    }
}

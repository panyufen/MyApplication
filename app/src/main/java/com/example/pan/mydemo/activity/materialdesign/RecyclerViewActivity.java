package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        setSupportActionBar(R.id.tool_bar);
    }

    public void startItemDecoration(View v) {
        startActivity(ItemDecorationActivity.class);
    }
}

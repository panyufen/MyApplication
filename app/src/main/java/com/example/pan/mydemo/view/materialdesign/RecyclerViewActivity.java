package com.example.pan.mydemo.view.materialdesign;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

public class RecyclerViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
    }

    public void startItemDecoration(View v) {
        startActivity(RecyclerViewItemDecorationActivity.class);
    }

    public void startItemDecoration2(View v) {
        startActivity(RecyclerViewItemDecoration2Activity.class);
    }

    public void startRecyclerViewRelated(View v) {
        startActivity(RecyclerViewRelatedActivity.class);
    }

    public void startSwipRecyclerView(View v) {
        startActivity(DragRecyclerViewActivity.class);
    }

}

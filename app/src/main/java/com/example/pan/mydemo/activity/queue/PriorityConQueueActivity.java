package com.example.pan.mydemo.activity.queue;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class PriorityConQueueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_con_queue);
        setSupportActionBar(R.id.tool_bar);
    }

    public void startSimpleQueue(View v) {
        startActivity(SimpleQueueActivity.class);
    }

    public void startPriorityQueue(View v) {
        startActivity(PriorityQueueActivity.class);
    }

}

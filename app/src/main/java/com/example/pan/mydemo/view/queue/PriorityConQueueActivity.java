package com.example.pan.mydemo.view.queue;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

public class PriorityConQueueActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_con_queue);
        setSupportActionBar(R.id.tool_bar);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return super.onRetainCustomNonConfigurationInstance();
    }

    @Nullable
    @Override
    public Object getLastNonConfigurationInstance() {
        return super.getLastNonConfigurationInstance();
    }

    @Override
    public Object getLastCustomNonConfigurationInstance() {
        return super.getLastCustomNonConfigurationInstance();
    }

    public void startSimpleQueue(View v) {
        startActivity(SimpleQueueActivity.class);
    }

    public void startPriorityQueue(View v) {
        startActivity(PriorityQueueActivity.class);
    }

}

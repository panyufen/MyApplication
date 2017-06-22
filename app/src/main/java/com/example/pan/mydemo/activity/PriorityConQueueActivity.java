package com.example.pan.mydemo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.queue.SimpleTask;
import com.example.pan.mydemo.queue.TaskSimpleQueue;

public class PriorityConQueueActivity extends BaseActivity {

    private TaskSimpleQueue mTaskSimpleQueue;

    private LinearLayout mTaskContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_con_queue);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        mTaskSimpleQueue = new TaskSimpleQueue(3);
        mTaskContainer = (LinearLayout) findViewById(R.id.task_container);
    }


    public void start(View v) {
        mTaskContainer.removeAllViews();
        mTaskSimpleQueue.start();
        Toast.makeText(this, "queue started", Toast.LENGTH_SHORT).show();
    }

    public void addTask2(View v) {
        createTaskView(2);
    }

    public void addTask5(View v) {
        createTaskView(5);
    }

    public void stop(View v) {
        mTaskContainer.removeAllViews();
        mTaskSimpleQueue.stop();
        Toast.makeText(this, "queue stoped", Toast.LENGTH_SHORT).show();
    }

    private void createTaskView(int sp) {
        if (mTaskSimpleQueue.getState()) {
            ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.progress_bar_height));
            progressBar.setLayoutParams(params);
            progressBar.setProgress(0);
            int padding = (int) getResources().getDimension(R.dimen.nav_header_vertical_spacing);
            progressBar.setPadding(padding, padding, padding, padding);
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.seek_bar_progress_drawable));
            SimpleTask simpleTask = new SimpleTask(progressBar, sp);
            mTaskContainer.addView(progressBar);
            mTaskSimpleQueue.add(simpleTask);
        } else {
            Toast.makeText(this, "click start first", Toast.LENGTH_SHORT).show();
        }
    }
}

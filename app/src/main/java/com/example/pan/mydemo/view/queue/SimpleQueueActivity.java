package com.example.pan.mydemo.view.queue;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.queue.simple.IQueueListener;
import com.example.pan.mydemo.queue.simple.ITaskListener;
import com.example.pan.mydemo.queue.simple.SimpleTask;
import com.example.pan.mydemo.queue.simple.TaskSimpleQueue;
import com.facebook.stetho.common.LogUtil;

public class SimpleQueueActivity extends BaseActivity {

    private TaskSimpleQueue mTaskSimpleQueue;

    private LinearLayout mTaskContainer;

    private TextView mTaskCountTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_queue);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        mTaskContainer = (LinearLayout) findViewById(R.id.task_container);
        mTaskCountTv = (TextView) findViewById(R.id.task_count_tv);
        mTaskCountTv.setText(getString(R.string.task_count, 0));
        mTaskSimpleQueue = new TaskSimpleQueue(3, new IQueueListener() {
            @Override
            public void onQueueChanged(final int count) {
                LogUtils.i("onQueueChanged " + count);
                SimpleQueueActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTaskCountTv.setText(getString(R.string.task_count, count));
                    }
                });
            }
        });
    }


    public void start(View v) {
        mTaskContainer.removeAllViews();
        mTaskSimpleQueue.start();
        Toast.makeText(this, "queue started", Toast.LENGTH_SHORT).show();
    }

    public void addTask1(View v) {
        createTaskView(1);
    }

    public void addTask2(View v) {
        createTaskView(2);
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
            SimpleTask simpleTask = new SimpleTask(mTaskContainer, progressBar, sp, new ITaskListener() {
                @Override
                public void onStart() {
                    LogUtil.i("task onStart");
                }

                @Override
                public void onPause() {
                    LogUtil.i("task onPause");
                }

                @Override
                public void onResume() {
                    LogUtil.i("task onResume");
                }

                @Override
                public void onEnd() {
                    LogUtil.i("task onEnd");
                }
            });
            int size = mTaskSimpleQueue.add(simpleTask);
            LogUtils.i("return size = " + size);
        } else {
            Toast.makeText(this, "click start first", Toast.LENGTH_SHORT).show();
        }
    }
}

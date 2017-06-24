package com.example.pan.mydemo.activity.queue;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.queue.priority.ITask;
import com.example.pan.mydemo.queue.priority.Priority;
import com.example.pan.mydemo.queue.priority.SimpleTask;
import com.example.pan.mydemo.queue.priority.TaskQueue;

public class PriorityQueueActivity extends BaseActivity {

    private LinearLayout mTaskContainer;
    private TaskQueue mTaskQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priority_queue);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        mTaskContainer = (LinearLayout) findViewById(R.id.task_container);
    }

    public void start(View v) {
        mTaskQueue = new TaskQueue(1);
        mTaskQueue.start();
        Toast.makeText(this, "queue started", Toast.LENGTH_SHORT).show();
    }

    public void stop(View v) {
        if (mTaskQueue != null) {
            mTaskQueue.stop();
        }
        mTaskContainer.removeAllViews();
        Toast.makeText(this, "queue stoped", Toast.LENGTH_SHORT).show();
    }

    public void addTaskLow(View v) {
        ITask simpleTask = createTask(Priority.Low);
        mTaskQueue.add(simpleTask);
    }

    public void addTaskNormal(View v) {
        ITask simpleTask = createTask(Priority.Normal);
        mTaskQueue.add(simpleTask);
    }

    public void addTaskHigh(View v) {
        ITask simpleTask = createTask(Priority.High);
        mTaskQueue.add(simpleTask);
    }

    public void addTaskImme(View v) {
        ITask simpleTask = createTask(Priority.Immediately);
        mTaskQueue.add(simpleTask);
    }

    private ITask createTask(Priority priority) {
        if (mTaskQueue.getState()) {
            View layout = View.inflate(this, R.layout.priority_queue_item_layout, null);
            SimpleTask simpleTask = new SimpleTask(mTaskContainer, layout);
            simpleTask.setPriority(priority);
            return simpleTask;
        }
        Toast.makeText(this, "click start first", Toast.LENGTH_SHORT).show();
        return null;
    }

}

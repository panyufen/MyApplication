package com.example.pan.mydemo.queue;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;

import com.example.pan.mydemo.activity.base.BaseActivity;

/**
 * Created by PAN on 2017/6/21.
 */

public class SimpleTask implements ITask {

    private ITaskListener mITaskListener;

    private ViewGroup parent;
    private ProgressBar progressBar;

    private int progress = 0;
    private int speed = 1;

    private long defaultDelay = 1000;

    public SimpleTask(ViewGroup p, ProgressBar progressBar, int sp, ITaskListener il) {
        this.parent = p;
        this.progressBar = progressBar;
        this.speed = sp;
        this.mITaskListener = il;
    }

    @Override
    public void constructor() {
        ((BaseActivity) (progressBar.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                parent.addView(progressBar);
            }
        });
    }

    @Override
    public void run() {

        if (mITaskListener != null) {
            mITaskListener.onStart();
        }
        while (progress < progressBar.getMax()) {
            progress += speed;
            runOnUiThread(progress);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void destructor() {
        if (progress == progressBar.getMax()) {
            removeTaskViewDelay(defaultDelay);
        }
    }

    private void runOnUiThread(final int index) {
        ((BaseActivity) (progressBar.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(index);
            }
        });
    }

    private void removeTaskViewDelay(final long delay) {
        try {
            Thread.sleep(delay);
            mITaskListener.onEnd();
            ((BaseActivity) (progressBar.getContext())).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewParent parent = progressBar.getParent();
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(progressBar);
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.example.pan.mydemo.queue;

import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;

import com.example.pan.mydemo.activity.base.BaseActivity;

/**
 * Created by PAN on 2017/6/21.
 */

public class SimpleTask implements ITask {

    private ProgressBar progressBar;

    private int progress = 0;
    private int speed = 1;

    private long defaultDelay = 1000;

    public SimpleTask(ProgressBar progressBar, int sp) {
        this.progressBar = progressBar;
        this.speed = sp;
    }

    @Override
    public void run() {
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

    private void runOnUiThread(final int index) {
        ((BaseActivity) (progressBar.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(index);
                if (index == progressBar.getMax()) {
                    removeTaskViewDelay(defaultDelay);
                }
            }
        });
    }

    private void removeTaskViewDelay(final long delay) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ((BaseActivity) (progressBar.getContext())).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewParent parent = progressBar.getParent();
                        if (parent != null) {
                            ((ViewGroup) parent).removeView(progressBar);
                        }
                    }
                });
            }
        }).start();

    }
}

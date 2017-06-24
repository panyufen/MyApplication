package com.example.pan.mydemo.queue.priority;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

/**
 * Created by PAN on 2017/6/23.
 */
public class SimpleTask extends BaseTask {
    private ViewGroup mParent;
    private View layout;
    private TextView mNameTv, mValueTv;
    private ProgressBar mProgressBar;
    private int progress;
    private long DEFAULT_DELAY = 1000;
    private String[] priorityStrs = {"低", "正常", "高", "最高"};


    public SimpleTask(ViewGroup p, View l) {
        this.layout = l;
        this.mParent = p;
    }

    @Override
    public void constructor() {
        mNameTv = (TextView) layout.findViewById(R.id.priority_name_tv);
        mValueTv = (TextView) layout.findViewById(R.id.priority_value_tv);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.priority_progress_bar);
        mNameTv.setText("任务" + getSequence() + "   优先级：" + priorityStrs[getPriority().ordinal()]);

        ((BaseActivity) (layout.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mParent.addView(layout);
            }
        });
    }

    @Override
    public void run() {
        while (progress < mProgressBar.getMax()) {
            progress += 1;
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
        if (progress == mProgressBar.getMax()) {
            removeTaskViewDelay(DEFAULT_DELAY);
        }
    }

    private void runOnUiThread(final int index) {
        ((BaseActivity) (layout.getContext())).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(index);
                mValueTv.setText(index + "%");
            }
        });
    }

    private void removeTaskViewDelay(final long delay) {
        try {
            Thread.sleep(delay);
            ((BaseActivity) (layout.getContext())).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewParent parent = layout.getParent();
                    if (parent != null) {
                        ((ViewGroup) parent).removeView(layout);
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

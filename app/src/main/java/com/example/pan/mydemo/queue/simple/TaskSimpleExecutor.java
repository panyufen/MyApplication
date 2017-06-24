package com.example.pan.mydemo.queue.simple;

import com.cus.pan.library.utils.LogUtils;

import java.util.concurrent.BlockingQueue;

/**
 * Created by PAN on 2017/6/21.
 */

public class TaskSimpleExecutor extends Thread {

    private ITaskListener mITaskListener;

    private BlockingQueue<ITask> mTaskQueue;

    private boolean isRunning = true;

    public TaskSimpleExecutor(BlockingQueue<ITask> tq, ITaskListener it) {
        mTaskQueue = tq;
        mITaskListener = it;
    }

    public void quit() {
        isRunning = false;
        interrupt();
    }

    @Override
    public void run() {
        while (isRunning) {
            ITask iTask;
            try {
                iTask = mTaskQueue.take();
            } catch (InterruptedException e) {
                if (!isRunning) {
                    interrupt();
                    break;
                }
                continue;
            }
            iTask.run();
            iTask.destructor();
            if (mITaskListener != null) {
                LogUtils.i("TaskSimpleExecutor run End task");
                mITaskListener.onEnd();
            }
        }
    }
}

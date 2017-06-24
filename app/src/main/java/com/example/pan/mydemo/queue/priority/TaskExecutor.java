package com.example.pan.mydemo.queue.priority;

import java.util.concurrent.BlockingQueue;

/**
 * Created by PAN on 2017/6/23.
 */

public class TaskExecutor extends Thread {

    private BlockingQueue<ITask> mTaskQueue;

    private boolean isRunning;

    public TaskExecutor(BlockingQueue<ITask> mTaskQueue) {
        this.mTaskQueue = mTaskQueue;
        isRunning = true;
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
        }
    }
}

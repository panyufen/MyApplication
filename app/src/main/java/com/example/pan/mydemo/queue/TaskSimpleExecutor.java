package com.example.pan.mydemo.queue;

import java.util.concurrent.BlockingQueue;

/**
 * Created by PAN on 2017/6/21.
 */

public class TaskSimpleExecutor extends Thread {

    private BlockingQueue<ITask> taskQueue;

    private boolean isRunning = true;

    public TaskSimpleExecutor(BlockingQueue<ITask> tq) {
        taskQueue = tq;
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
                iTask = taskQueue.take();
            } catch (InterruptedException e) {
                if (!isRunning) {
                    interrupt();
                    break;
                }
                continue;
            }
            iTask.run();
        }
    }
}

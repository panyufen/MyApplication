package com.example.pan.mydemo.queue.priority;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by PAN on 2017/6/23.
 * 任务列队 会提供 启动和关闭列队以及添加任务的功能
 */
public class TaskQueue {

    private AtomicInteger mAtomicInteger = new AtomicInteger();

    private BlockingQueue<ITask> mTaskQueue;
    private TaskExecutor[] mTaskExecutors;

    private boolean isStart = false;


    public TaskQueue(int size) {
        mTaskQueue = new PriorityBlockingQueue<>();
        mTaskExecutors = new TaskExecutor[size];
    }

    public void start() {
        for (int i = 0; i < mTaskExecutors.length; i++) {
            mTaskExecutors[i] = new TaskExecutor(mTaskQueue);
            mTaskExecutors[i].start();
        }
        isStart = true;
    }

    public void stop() {
        if (mTaskExecutors != null) {
            for (TaskExecutor taskExecutor : mTaskExecutors) {
                taskExecutor.quit();
            }
        }
        isStart = false;
    }

    public <T extends ITask> int add(T task) {
        if (task != null && !mTaskQueue.contains(task)) {
            task.setSequence(mAtomicInteger.incrementAndGet());
            task.constructor();
            mTaskQueue.add(task);
        }
        return mTaskQueue.size();
    }

    public boolean getState() {
        return isStart;
    }

}

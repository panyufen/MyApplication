package com.example.pan.mydemo.queue.simple;

import com.cus.pan.library.utils.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by PAN on 2017/6/21.
 */

public class TaskSimpleQueue {

    private int taskMaxCount = 20;

    private int taskSize = 0;

    private IQueueListener mIQueueListener;

    //排队队列
    private BlockingQueue<ITask> mTaskQueue;
    //各个执行器
    private TaskSimpleExecutor[] taskSimpleExecutors;

    private boolean isStart = false;

    //初始化列对需要指定执行器数量
    public TaskSimpleQueue(int size, IQueueListener iq) {
        mTaskQueue = new LinkedBlockingQueue<>();
        taskSimpleExecutors = new TaskSimpleExecutor[size];
        mIQueueListener = iq;
    }

    public void start() {
        if (!isStart) {
            for (int i = 0; i < taskSimpleExecutors.length; i++) {
                taskSimpleExecutors[i] = new TaskSimpleExecutor(mTaskQueue, new ITaskListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onResume() {

                    }

                    @Override
                    public void onEnd() {
                        if (taskSize > 0) {
                            taskSize--;
                            mIQueueListener.onQueueChanged(taskSize);
                        }
                    }
                });
                taskSimpleExecutors[i].start();
            }
            LogUtils.i("queue started");
            isStart = true;
        }
    }

    public void stop() {
        if (taskSimpleExecutors != null) {
            for (TaskSimpleExecutor taskExecutor : taskSimpleExecutors) {
                if (taskExecutor != null) {
                    taskExecutor.quit();
                }
            }
            LogUtils.i("queue stoped");
            isStart = false;
            taskSize = 0;
            mIQueueListener.onQueueChanged(taskSize);
        }
    }

    public boolean getState() {
        return isStart;
    }


    //往列队里添加任务
    public <T extends ITask> int add(T task) {
        if (taskSize < taskMaxCount) {
            task.constructor();
            taskSize++;
            mTaskQueue.add(task);
            LogUtils.i("TaskSimpleQueue add Task");
            mIQueueListener.onQueueChanged(taskSize);
        }
        return taskSize;
    }
}

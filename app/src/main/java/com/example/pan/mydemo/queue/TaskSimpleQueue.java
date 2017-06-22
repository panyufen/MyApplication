package com.example.pan.mydemo.queue;

import com.cus.pan.library.utils.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by PAN on 2017/6/21.
 */

public class TaskSimpleQueue {
    //排队队列
    private BlockingQueue<ITask> mTaskQueue;
    //各个执行器
    private TaskSimpleExecutor[] taskSimpleExecutors;

    private boolean isStart = false;

    //初始化列对需要指定执行器数量
    public TaskSimpleQueue(int size) {
        mTaskQueue = new LinkedBlockingQueue<>();
        taskSimpleExecutors = new TaskSimpleExecutor[size];
    }

    public void start() {
        if (!isStart) {
            for (int i = 0; i < taskSimpleExecutors.length; i++) {
                taskSimpleExecutors[i] = new TaskSimpleExecutor(mTaskQueue);
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
        }
    }

    public boolean getState() {
        return isStart;
    }


    //往列队里添加任务
    public <T extends ITask> int add(T task) {
        if (!mTaskQueue.contains(task)) {
            mTaskQueue.add(task);
        }
        return mTaskQueue.size();
    }

}

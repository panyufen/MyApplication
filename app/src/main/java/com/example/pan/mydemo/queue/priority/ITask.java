package com.example.pan.mydemo.queue.priority;

/**
 * Created by PAN on 2017/6/23.
 */

public interface ITask extends Comparable<ITask> {
    void constructor();

    void destructor();

    void run();

    void setPriority(Priority priority);

    Priority getPriority();

    void setSequence(int mSequence);

    int getSequence();

}

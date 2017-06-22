package com.example.pan.mydemo.queue;

/**
 * Created by PAN on 2017/6/21.
 */
public interface ITask {
    void constructor();

    void run();

    void destructor();
}

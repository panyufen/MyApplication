package com.example.pan.mydemo.queue;

/**
 * Created by PAN on 2017/6/22.
 * Task任务状态监听接口
 */
public interface ITaskListener {

    void onStart();

    void onPause();

    void onResume();

    void onEnd();

}

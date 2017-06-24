package com.example.pan.mydemo.queue.priority;

import android.support.annotation.NonNull;

/**
 * Created by PAN on 2017/6/23.
 */
public abstract class BaseTask implements ITask {

    private Priority mPriority = Priority.Normal;
    private int mSequence;

    @Override
    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    @Override
    public Priority getPriority() {
        return mPriority;
    }

    @Override
    public void setSequence(int sequence) {
        this.mSequence = sequence;
    }

    @Override
    public int getSequence() {
        return mSequence;
    }


    @Override
    public int compareTo(@NonNull ITask o) {
        Priority me = this.getPriority();
        Priority other = o.getPriority();
        return me == other ? getSequence() - o.getSequence() : other.ordinal() - me.ordinal();
    }


}

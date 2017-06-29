package com.example.pan.mydemo.activity.rxjava;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import rx.Observable;
import rx.functions.Action1;

public class RxJavaActivity extends BaseActivity {
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        printString();
    }


    public void startRxJavaDemo1(View v) {
        startActivity(RxJava1Activity.class);
    }

    public void startRxJavaDemo2(View v) {
        startActivity(RxJava2Activity.class);
    }

    public void startRxJavaDemo3(View v) {
        startActivity(RxJava3Activity.class);
    }


    private void printString() {
        String[] names = {"aaa", "bbb", "ccc", "ddd"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String name) {
                        LogUtils.e(name);
                    }
                });
    }


}

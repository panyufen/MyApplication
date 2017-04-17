package com.example.pan.mydemo.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class RxJavaActivity extends BaseActivity {
    private Toolbar mToolBar;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        mToolBar = setSupportActionBar(R.id.tool_bar);
        imageView = (ImageView)findViewById(R.id.image_view);

        printString();

        ShowImage();
    }

    private void printString() {
        String[] names = {"aaa","bbb","ccc","ddd"};
        Observable.from(names)
            .subscribe(new Action1<String>() {
                @Override
                public void call(String name) {
                    LogUtils.e(name);
                }
            });
    }


    private void ShowImage(){
        final int drawableRes = R.drawable.aaa;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Subscriber<Drawable>() {

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(RxJavaActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

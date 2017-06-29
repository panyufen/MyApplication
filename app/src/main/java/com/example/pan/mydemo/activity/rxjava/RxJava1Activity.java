package com.example.pan.mydemo.activity.rxjava;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import rx.Observable;
import rx.Subscriber;

public class RxJava1Activity extends BaseActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java1);
        setSupportActionBar(R.id.tool_bar);
        imageView = (ImageView) findViewById(R.id.image_view);
        showImage();
    }

    private void showImage() {
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
                Toast.makeText(RxJava1Activity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

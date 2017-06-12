package com.example.pan.mydemo.activity.customview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.view.CircleRectView;

public class CircleRectViewActivity extends BaseActivity {
    private ValueAnimator valueAnimator = null;
    private CircleRectView circleRectView;
    private boolean toBig = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_rect);
        setSupportActionBar(R.id.tool_bar);

        circleRectView = (CircleRectView) findViewById(R.id.circle_rect_view);
        circleRectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toBig) {
                    runAnim(v, 400);
                    toBig = false;
                } else {
                    runAnim(v, 0);
                    toBig = true;
                }
            }
        });
    }

    public void runAnim(View v, int val) {
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            valueAnimator = ValueAnimator.ofInt(((CircleRectView) v).getRectWidth(), val);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    LogUtils.i("value " + value);
                    circleRectView.setRectWidth(value);
                }
            });
            valueAnimator.start();
        }
    }
}

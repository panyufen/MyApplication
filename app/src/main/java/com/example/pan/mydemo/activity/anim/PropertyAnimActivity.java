package com.example.pan.mydemo.activity.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class PropertyAnimActivity extends BaseActivity {

    private RelativeLayout ballContainer;
    private ImageView mGreenBall;
    private ImageView animatorSetMoveBall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_anim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("PropertyAnim");
        setSupportActionBar(toolbar);
        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);


        ballContainer = (RelativeLayout) findViewById(R.id.ball_container);
        mGreenBall = (ImageView) findViewById(R.id.id_move_ball);

        animatorSetMoveBall = (ImageView) findViewById(R.id.animator_set_move_ball);


    }

    public void rotateAnimRun(View view) {
        ObjectAnimator
                .ofFloat(view, "rotationX", 0.0F, 360.0F)//
                .setDuration(1000)//
                .start();
    }


    public void rotateAnimRunListener(final View view) {
        ValueAnimator anim;
        if (view.getTag() != "1") {
            anim = ObjectAnimator.ofFloat(1.0F, 0.3F).setDuration(1000);//
            view.setTag("1");
        } else {
            anim = ObjectAnimator.ofFloat(0.3F, 1F).setDuration(1000);//
            view.setTag("0");
        }

        anim.setInterpolator(new BounceInterpolator());
        anim.start();
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                view.setAlpha(cVal);
                view.setScaleX(cVal);
                view.setScaleY(cVal);
            }
        });
    }

    public void doBounceAnim(final View view) {
        final float x = ballContainer.getWidth() - mGreenBall.getWidth(), y = ballContainer.getHeight() - mGreenBall.getHeight();
        ValueAnimator xAnimator = ValueAnimator.ofFloat(0f, 1f);
        xAnimator.setDuration(2000);
        xAnimator.setInterpolator(new LinearInterpolator());
        xAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenBall.setX(x * (float) animation.getAnimatedValue());
            }
        });
        ValueAnimator yAnimator = ValueAnimator.ofFloat(0f, 1f);
        yAnimator.setDuration(2000);
        yAnimator.setInterpolator(new BounceInterpolator());
        yAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGreenBall.setTranslationY(y * (float) animation.getAnimatedValue());
            }
        });

        xAnimator.start();
        yAnimator.start();

    }


    public void paowuxian(View v) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue) {
                LogUtils.e("fraction " + fraction * 3 + "");
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 130 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                mGreenBall.setX(point.x);
                mGreenBall.setY(point.y);
            }
        });
    }


    public void doTogetherAnim(View v) {
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleX", 1.0f, 2f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleY", 1.0f, 2f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(2000);
        animSet.setInterpolator(new LinearInterpolator());
        //两个动画同时执行
        animSet.playTogether(anim1, anim2);
        animSet.start();
    }

    public void doByOrderAnim(View v) {
        float cx = animatorSetMoveBall.getX();

        ObjectAnimator anim1 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleX", 1.0f, 2f);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleY", 1.0f, 2f);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(animatorSetMoveBall, "x", cx, 0f);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(animatorSetMoveBall, "x", 2 * cx);
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(animatorSetMoveBall, "x", cx);
        ObjectAnimator anim6 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleX", 1.0f);
        ObjectAnimator anim7 = ObjectAnimator.ofFloat(animatorSetMoveBall, "scaleY", 1.0f);
        /**
         * anim1，anim2,同时执行
         * anim3 anim4 anim5 anim6接着执行
         */
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(anim1).with(anim2); //with: 一起执行
        animSet.play(anim3).after(anim2); //after: 在之后执行
        animSet.play(anim4).after(anim3);
        animSet.play(anim5).after(anim4);
        animSet.play(anim6).after(anim5);
        animSet.play(anim6).with(anim7);
        animSet.setDuration(1000);
        animSet.start();
    }

}

package com.example.pan.mydemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAN on 2016/9/9.
 */
public class InterpolatorView extends View {

    private Paint mPaint;
    private Path mPath;
    private PathMeasure pathMeasure;

    private int x = 50;

    private List<Point> points = new ArrayList<>();


    public InterpolatorView(Context context) {
        super(context);
        init();
    }

    public InterpolatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterpolatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        mPaint.setColor(Color.RED);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        mPath.reset();
        for (int i = 0; i < points.size(); i++) {
            if (i == 0) {
                mPath.moveTo(points.get(i).x, points.get(i).y);
            } else if (i < points.size()) {
                mPath.quadTo(points.get(i - 1).x, points.get(i - 1).y, points.get(i).x, points.get(i).y);
            }
        }


        if (points.size() > 0) {
            mPath.rLineTo(0, 0);
            canvas.drawPath(mPath, mPaint);
        }

    }

    public void startAnim(int s) {
        x = 50;
        points.clear();
        ValueAnimator anim1 = ValueAnimator.ofFloat(0, getMeasuredHeight() * 0.6f);
        anim1.setInterpolator(new AccelerateInterpolator(4f));
        anim1.setDuration(s);
        anim1.start();


        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                x += 2;
                float y = (float) animation.getAnimatedValue();
                points.add(new Point(x, (int) (getMeasuredHeight() * 0.8) - (int) y));
                postInvalidate();
            }
        });

    }


}

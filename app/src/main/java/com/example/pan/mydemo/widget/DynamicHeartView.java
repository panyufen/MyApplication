package com.example.pan.mydemo.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by PAN on 2016/9/9.
 */
public class DynamicHeartView extends View {

    private static final String TAG = "DynamicHeartView";
    private static final int PATH_WIDTH = 2;
    // 起始点
    private static final int[] START_POINT = new int[] {
            300, 270
    };
    // 爱心下端点
    private static final int[] BOTTOM_POINT = new int[] {
            300, 400
    };
    // 左侧控制点
    private static final int[] LEFT_CONTROL_POINT = new int[] {
            200, 200
    };
    // 右侧控制点
    private static final int[] RIGHT_CONTROL_POINT = new int[] {
            450, 200
    };

    private PathMeasure mPathMeasure;
    private Paint mPaint;
    private Path mPath;
    private float[] mCurrentPosition = new float[2];

    public DynamicHeartView(Context context) {
        super(context);
        init();
    }
    public DynamicHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DynamicHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(PATH_WIDTH);
        mPaint.setColor(Color.RED);

        mPath = new Path();
        mPath.moveTo(START_POINT[0], START_POINT[1]);
        mPath.quadTo(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], BOTTOM_POINT[0],
                BOTTOM_POINT[1]);
        mPath.quadTo(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], START_POINT[0], START_POINT[1]);

        mPathMeasure = new PathMeasure(mPath, true);
        mCurrentPosition = new float[2];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.drawPath(mPath, mPaint);

        canvas.drawCircle(RIGHT_CONTROL_POINT[0], RIGHT_CONTROL_POINT[1], 5, mPaint);
        canvas.drawCircle(LEFT_CONTROL_POINT[0], LEFT_CONTROL_POINT[1], 5, mPaint);

        // 绘制对应目标
        canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], 10, mPaint);
    }

    // 开启路径动画
    public void startPathAnim(long duration) {
        // 0 － getLength()
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        Log.i(TAG, "measure length = " + mPathMeasure.getLength());
        valueAnimator.setDuration(duration);
        // 减速插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();
                // 获取当前点坐标封装到mCurrentPosition
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                postInvalidate();
            }
        });
        valueAnimator.start();

    }
}

package com.example.pan.mydemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;

/**
 * Created by PAN on 2016/9/12.
 */
public class SetPolyToPolyView extends View {
    private static final String TAG = "SetPolyToPoly";

    private int testPoint = 4;
    private int triggerRadius = 50;    // 触发半径为100px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix

    private float[] src = new float[8];
    private float[] dst = new float[8];

    private Paint pointPaint;
    private int paintWidth = 50;

    public SetPolyToPolyView(Context context) {
        this(context, null);
    }

    public SetPolyToPolyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetPolyToPolyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.poly);

        float[] temp = {
                0, 0,                                    // 左上
                mBitmap.getWidth(), 0,                          // 右上
                mBitmap.getWidth(), mBitmap.getHeight(),        // 右下
                0, mBitmap.getHeight()};                        // 左下
        src = temp.clone();

        mPolyMatrix = new Matrix();

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(paintWidth);
        pointPaint.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        dst[0] = 0;
        dst[1] = 0;
        dst[2] = getMeasuredWidth() - 200;
        dst[3] = 0;
        dst[4] = getMeasuredWidth() - 200;
        dst[5] = getMeasuredHeight() - 200;
        dst[6] = 0;
        dst[7] = getMeasuredHeight() - 200;

        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, 4);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                LogUtils.i("checkedge " + event.getX() + " " + event.getY() + " " + getMeasuredWidth() + " " + getMeasuredHeight() + " " + getTop() + " " + getBottom() + " ");
                if (event.getX() < paintWidth / 2 || event.getX() > getMeasuredWidth() - paintWidth / 2)
                    return true;
                if (event.getY() < paintWidth / 2 || event.getY() > getMeasuredHeight() - paintWidth / 2)
                    return true;

                float tempX = event.getX() - 100;
                float tempY = event.getY() - 100;
                // 根据触控位置改变dst
                for (int i = 0; i < testPoint * 2; i += 2) {
                    LogUtils.i("move ", tempX + " " + tempY + " " + dst[i] + " " + dst[i + 1]);
                    if (Math.abs(tempX - dst[i]) <= triggerRadius && Math.abs(tempY - dst[i + 1]) <= triggerRadius) {
                        dst[i] = tempX;
                        dst[i + 1] = tempY;
                        break;  // 防止两个点的位置重合
                    }
                }
                resetPolyMatrix(testPoint);
                invalidate();
                break;
        }

        return true;
    }

    public void resetPolyMatrix(int pointCount) {
        mPolyMatrix.reset();
        // 核心要点
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(100, 100);

        // 根据Matrix绘制一个变换后的图片
        canvas.drawBitmap(mBitmap, mPolyMatrix, null);

        float[] dst = new float[8];
        mPolyMatrix.mapPoints(dst, src);

        // 绘制触控点
        for (int i = 0; i < testPoint * 2; i += 2) {
            canvas.drawPoint(dst[i], dst[i + 1], pointPaint);
        }
    }

    public void setTestPoint(int testPoint) {
        this.testPoint = testPoint > 4 || testPoint < 0 ? 4 : testPoint;
        dst = src.clone();
        resetPolyMatrix(this.testPoint);
        invalidate();
    }
}

package com.example.pan.mydemo.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;

/**
 * Created by PAN on 2016/9/20.
 */
public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements OnScaleGestureListener, View.OnTouchListener {
    private static final String TAG = ZoomImageView.class.getSimpleName();

    public static final float SCALE_MAX = 4.0f;
    private static float SCALE_MID = 2.0f;
    private static float SCALE_ORG = 1.0f;

    private RectF orgRectF = new RectF();

    private RectF curRectF = new RectF();

    /**
     * 初始化时的缩放比例，如果图片宽或高大于屏幕，此值将小于1
     */
    private float initScale = 1.0f;

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValues = new float[9];

    private boolean once = true;

    /**
     * 缩放的手势检测
     */
    private ScaleGestureDetector mScaleGestureDetector = null;

    private GestureDetector mGestureDetector;

    private final Matrix mScaleMatrix = new Matrix();

    private boolean isAutoScale = false;

    private int mTouchSlop;

    private float mLastX;
    private float mLastY;

    private float mPreAvgAngle;

    private float rationsTotal;


    private boolean isCanDrag;
    private int lastPointerCount;

    private boolean isCheckTopAndBottom = true;
    private boolean isCheckLeftAndRight = true;


    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(this);

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        Log.i("onDoubleTap", "onDoubleTap");

                        if (isAutoScale)
                            return true;

                        float x = e.getX();
                        float y = e.getY();
                        Log.e("DoubleTap", getScale() + " , " + initScale);

                        if (getScale() < SCALE_MID-0.1f) {
//                            if (getScale() < 1) {
//                                SCALE_MID = 1;
//                            }
                            ZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(SCALE_MID, x, y), 16);
                            isAutoScale = true;
                        } else if (getScale() >= SCALE_MID-0.1f && getScale() < SCALE_MAX-0.1f) {
                            ZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(SCALE_MAX, x, y), 16);
                            isAutoScale = true;
                        } else {
                            ZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(initScale, x, y), 16);
                            isAutoScale = true;
                        }

                        return true;
                    }
                });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event))
            return true;

        mScaleGestureDetector.onTouchEvent(event);
        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值

        StringBuffer buffer = new StringBuffer();

        buffer.append(pointerCount + ": ");

        for (int i = 0; i < pointerCount; i++) {
            buffer.append(i + "(" + event.getX(i) + "," + event.getY(i) + ") | ");

            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        Log.i("multiPoints ", buffer.toString());
        lastPointerCount = pointerCount;
        rationsTotal = 0;
        Log.i("MotionEvent", " " + event.getAction());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:

                Log.i("MotionEvent", "ACTION_POINTER_DOWN ");

                break;
            case MotionEvent.ACTION_DOWN:
                Log.i("MotionEvent", "ACTION_DOWN");


                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("MotionEvent", "ACTION_MOVE");

                float tempAngle = 0;
                if (pointerCount >= 2) {
                    float angle = 0;

                    StringBuffer buffer1 = new StringBuffer();

                    buffer1.append(event.getX(0) + " " + event.getY(0) + " ");
                    for (int j = 1; j < pointerCount; j++) {
                        double x1 = event.getX(0);
                        double y1 = event.getY(0);

                        double x2 = event.getX(j);
                        double y2 = event.getY(j);
                        buffer1.append(x2 + " " + y2 + " ");

                        double rations = Math.atan2(x2 - x1, y1 - y2);
                        angle = (float) Math.toDegrees(rations);
                        rationsTotal += angle;
                    }
                    Log.i("xy ", buffer1.toString());
                    tempAngle = rationsTotal / (pointerCount - 1);

                    Log.i("angle ", tempAngle + " " + mPreAvgAngle + " " + (tempAngle - mPreAvgAngle) + " " + rationsTotal / (pointerCount - 1));
                    if (mPreAvgAngle != 0 && (Math.abs(tempAngle - mPreAvgAngle) < 10)) {
                        mScaleMatrix.postRotate(tempAngle - mPreAvgAngle, x, y);
                    }
                    mPreAvgAngle = tempAngle;
                }

                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {
                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() < getWidth()) {
                            dx = 0;
                            isCheckLeftAndRight = false;
                        }
                        // 如果高度小雨屏幕高度，则禁止上下移动
                        if (rectF.height() < getHeight()) {
                            dy = 0;
                            isCheckTopAndBottom = false;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkMatrixBounds();
                        curRectF.setEmpty();
                        curRectF.set(orgRectF);
                        mScaleMatrix.mapRect(curRectF);
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
                Log.i("MotionEvent", "ACTION_UP");
                mPreAvgAngle = 0;
                rationsTotal = 0;
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
        }
        return true;
    }

    /**
     * 自动缩放的任务
     *
     * @author zhy
     */
    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {

            // 进行缩放
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            curRectF.set(orgRectF);
            mScaleMatrix.mapRect(curRectF);
            setImageMatrix(mScaleMatrix);

            final float currentScale = getScale();
            // 如果值在合法范围内，继续缩放
            Log.i("currentScale", currentScale + " "+tmpScale+" "+mTargetScale);
            if (tmpScale > 1f && currentScale < mTargetScale || tmpScale < 1f && currentScale > mTargetScale) {
                ZoomImageView.this.postDelayed(this, 16);
            } else { // 设置为目标的缩放比例
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorderAndCenterWhenScale();
                curRectF.set(orgRectF);
                mScaleMatrix.mapRect(curRectF);
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }

        }
    }


    /**
     * 移动时，进行边界判断，主要判断宽或高大于屏幕的
     */
    private void checkMatrixBounds() {
        RectF rect = getMatrixRectF();

        float deltaX = 0, deltaY = 0;
        final float viewWidth = getWidth();
        final float viewHeight = getHeight();
        // 判断移动或缩放后，图片显示是否超出屏幕边界
        if (rect.top > 0 && isCheckTopAndBottom) {
            deltaY = -rect.top;
        }
        if (rect.bottom < viewHeight && isCheckTopAndBottom) {
            deltaY = viewHeight - rect.bottom;
        }
        if (rect.left > 0 && isCheckLeftAndRight) {
            deltaX = -rect.left;
        }
        if (rect.right < viewWidth && isCheckLeftAndRight) {
            deltaX = viewWidth - rect.right;
        }
        mScaleMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 是否是推动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
//        LogUtils.i(TAG, "onScale");

        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null)
            return true;

//        LogUtils.i(TAG, "c F I  M " + scale + " " + scaleFactor + " " + initScale + " " + SCALE_MAX);

        //缩放范围控制


        //最大值最小值判断
//            if (scaleFactor * scale < initScale) {
//                scaleFactor = initScale / scale;
//            }
//            if (scaleFactor * scale > SCALE_MAX) {
//                scaleFactor = SCALE_MAX / scale;
//            }

        //设置缩放比例
        mScaleMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
        curRectF.setEmpty();
        curRectF.set(orgRectF);
        mScaleMatrix.mapRect(curRectF);
//            checkBorderAndCenterWhenScale();
        setImageMatrix(mScaleMatrix);
        Log.i("scaleFactor ", scaleFactor + " " + scale + " " + SCALE_MAX + " " + initScale);
        return true;

    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        Log.i(TAG, "onScaleEnd");
    }


    public final float getScale() {
        Matrix matrixCalculate = new Matrix();
        float[] src = new float[]{orgRectF.left, orgRectF.top, orgRectF.right, orgRectF.top, orgRectF.left, orgRectF.bottom, orgRectF.right, orgRectF.bottom};
        float[] cur = new float[]{curRectF.left, curRectF.top, curRectF.right, curRectF.top, curRectF.left, curRectF.bottom, curRectF.right, curRectF.bottom};
        matrixCalculate.setPolyToPoly(src, 0, cur, 0, 4);
        Log.i("custom Matrix", " " + orgRectF.toString() + " " + curRectF.toString());
        Log.i("custom Matrix1", " " + matrixCalculate.toString());

        matrixCalculate.getValues(matrixValues);

        return matrixValues[Matrix.MSCALE_X];
    }


    /**
     * 在缩放时，进行图片显示范围的控制
     */
    private void checkBorderAndCenterWhenScale() {

        RectF rect = getMatrixRectF();

//        LogUtils.i("rect ", rect.left + " " + rect.top + " " + rect.right + " " + rect.bottom);
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围
        if (rect.width() >= width) {
            if (rect.left > 0) {
                deltaX = -rect.left;
            }
            if (rect.right < width) {
                deltaX = width - rect.right;
            }
        }
        if (rect.height() >= height) {
            if (rect.top > 0) {
                deltaY = -rect.top;
            }
            if (rect.bottom < height) {
                deltaY = height - rect.bottom;
            }
        }
        // 如果宽或高小于屏幕，则让其居中
        if (rect.width() < width) {
            deltaX = width * 0.5f - rect.right + 0.5f * rect.width();
        }
        if (rect.height() < height) {
            deltaY = height * 0.5f - rect.bottom + 0.5f * rect.height();
        }
//        LogUtils.e(TAG, "deltaX = " + deltaX + " , deltaY = " + deltaY);

        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout");
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;
            int width = getWidth();
            int height = getHeight();
            // 拿到图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            Log.i(TAG, "drawable W H | Screen W H " + d.getIntrinsicWidth() + " , " + d.getIntrinsicHeight() + " | " + width + " " + height);
            float scale = 1.0f;
            // 如果图片的宽或者高大于屏幕，则缩放至屏幕的宽或者高
            if (dw > width && dh <= height) {
                scale = 1.0f * width / dw;
            }
            if (dh > height && dw <= width) {
                scale = 1.0f * height / dh;
            }
            // 如果宽和高都大于屏幕，则让其按按比例适应屏幕大小
            if (dw > width && dh > height) {
                scale = Math.min(width * 1.0f / dw, height * 1.0f / dh);
            }
            initScale = scale;
            // 图片移动至屏幕中心
            mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            mScaleMatrix.postScale(scale, scale, width / 2, height / 2);

            orgRectF.set(0, 0, dw, dh);
            mScaleMatrix.mapRect(orgRectF);
            curRectF.set(orgRectF);
            mScaleMatrix.mapRect(curRectF);

            setImageMatrix(mScaleMatrix);
            Log.i("matrixValues", mScaleMatrix.toString());

            once = false;
        }
    }

}
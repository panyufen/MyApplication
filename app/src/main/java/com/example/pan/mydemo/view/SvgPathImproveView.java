package com.example.pan.mydemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cus.pan.library.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by PAN on 2017/3/27.
 */

public class SvgPathImproveView extends View {

    private int screenWidth, screenHeight;

    private ValueAnimator mValueAnimator;
    private float mStartValue = 0f, mEndValue = 1f;
    private long mDuring = 1000L;
    private float mAnimatorValue = 0f;
    private final int DEFAULT_DURING = 50;
    private StatusListener statusListener;

    // 画笔
    private Paint mPaint;
    // 测量Path 并截取部分的工具
    private PathMeasure mMeasure;
    private RectF imgCalRect = new RectF();
    private float scale = 1f;

    private Path mPath;
    private ArrayList<Path> mPaths = new ArrayList<>();
    private Path mDst = new Path();
    private float mPathSumLen = 0;
    private float mPathCurrentDrawedLen = 0;

    private enum Status {
        NORMAL, PLAYING, END
    }

    private Status mCurrentStatus = Status.NORMAL;

    private boolean mAnimFinish = false;

    public SvgPathImproveView(Context context) {
        this(context, null);
    }

    public SvgPathImproveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SvgPathImproveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        screenHeight = getResources().getDisplayMetrics().heightPixels;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(1);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mMeasure = new PathMeasure();
    }

    private ValueAnimator initAnimator(float s, float e, long during, TimeInterpolator interpolator) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(s, e);
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.setDuration(during);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                LogUtils.i("valueUpdate " + mAnimatorValue + " " + mValueAnimator.getDuration());
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!mAnimFinish) {
                    mPaths.add(mDst);
                    mPathCurrentDrawedLen += mMeasure.getLength();
                    if (mMeasure.nextContour()) {
                        LogUtils.i("mMeasure nextContour true");
                        int during = (int) mMeasure.getLength() * 2;
                        mValueAnimator = initAnimator(mStartValue, mEndValue, during > DEFAULT_DURING ? during : DEFAULT_DURING, new LinearInterpolator());
                        mValueAnimator.start();
                    } else {
                        if (statusListener != null) {
                            mCurrentStatus = Status.END;
                            statusListener.svgPathEnd();
                        }
                    }
                }
            }
        });
        return valueAnimator;
    }

    public void setPath(Path path) {
        mPath = path;
    }

    public void setStatusListener(StatusListener sl) {
        this.statusListener = sl;
    }

    private void dealPathToMeasure() {
        mMeasure.setPath(mPath, false);
        mPathSumLen = 0f;
        mPathCurrentDrawedLen = 0f;
        do {
            mPathSumLen += mMeasure.getLength();
        } while (mMeasure.nextContour());
        mMeasure.setPath(mPath, false);
    }

    private void calculatePadding() {
        RectF rectF = new RectF();
        mPath.computeBounds(rectF, false);
        scale = screenWidth / rectF.width();
        float calHeight = rectF.height() * scale;
        imgCalRect.top = imgCalRect.bottom = screenHeight / 2 - calHeight / 2;
        imgCalRect.left = 0;
        imgCalRect.right = screenWidth;
    }

    public void start() {
        mAnimFinish = false;
        if (mValueAnimator == null || !mValueAnimator.isRunning()) {
            mCurrentStatus = Status.PLAYING;
            dealPathToMeasure();
            calculatePadding();
            mPaths.clear();
            int during = (int) mMeasure.getLength() * 2;
            mValueAnimator = initAnimator(mStartValue, mEndValue, during, new LinearInterpolator());
            mValueAnimator.start();
            if (statusListener != null) {
                statusListener.svgPathStart();
            }
        }
    }

    public void end() {
        mAnimFinish = true;
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
            mValueAnimator.end();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(0, imgCalRect.top);
        canvas.scale(scale, scale);

        for (Path p : mPaths) {
            canvas.drawPath(p, mPaint);
        }
        mDst = new Path();
        mMeasure.getSegment(0, mMeasure.getLength() * mAnimatorValue, mDst, true);
        float currentProgress = mPathSumLen == 0f ? 0f : (mPathCurrentDrawedLen + mMeasure.getLength() * mAnimatorValue) * 100 / mPathSumLen;
        dealSvgPathChangeParam(currentProgress);
        mDst.rLineTo(0, 0);
        canvas.drawPath(mDst, mPaint);
    }

    private void dealSvgPathChangeParam(float p) {
        if (mCurrentStatus == Status.PLAYING) {
            statusListener.svgPathChange(p);
        }
    }


    public interface StatusListener {
        void svgPathStart();

        void svgPathChange(float progress);

        void svgPathEnd();
    }
}

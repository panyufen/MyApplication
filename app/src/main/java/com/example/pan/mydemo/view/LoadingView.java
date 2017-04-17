package com.example.pan.mydemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.pan.mydemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 作者： 巴掌 on 16/6/14 13:06
 */
public class LoadingView extends View {

    private final int DEFAULT_DURATION = 15;
    private final int DEFAULT_EXTERNAL_RADIUS = dp2px(82);
    private final int DEFAULT_INTERNAL_RADIUS = dp2px(8);
    private final int DEFAULT_RADIAN = 45;

    private int mWidth;
    private int mHeight;
    private Subscription mTimer;
    private Paint mPaint;
    private Path mPath = new Path();

    //内圆色
    int mColors[];

    //外圆角度
    int mAngle = 0;

    //圈数
    int mCyclic = 0;

    //变大动画圆半径
    private float mGetBiggerCircleRadius;

    //移动圆半径
    private float mGetSmallerCircleRadius;

    //外圆
    private List<PointF> mPoints;

    //属性动画集
    private List<ValueAnimator> mAnimators;

    //外圆圆点
    private float x0, y0;

    //点间的弧度
    private int mRadian = DEFAULT_RADIAN;

    //时间间隔
    private int mDuration;
    private final static int MAX_DURATION = 120;
    private final static int MIN_DURATION = 1;

    //内圆半径
    private float mInternalR;
    private final int MAX_INTERNAL_R = dp2px(18);
    private final int MIN_INTERNAL_R = dp2px(2);

    //外圆半径
    private float mExternalR;
    private final int MAX_EXTERNAL_R = dp2px(150);
    private final int MIN_EXTERNAL_R = dp2px(25);

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mAnimators = new ArrayList<>();
        mPoints = new ArrayList<>();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        TypedArray typeArray = getContext().obtainStyledAttributes(attrs,
                R.styleable.BezierLoadingView);
        mDuration = typeArray.getInt(R.styleable.BezierLoadingView_lv_duration, DEFAULT_DURATION);
        mInternalR = typeArray.getDimension(R.styleable.BezierLoadingView_lv_internal_radius, DEFAULT_INTERNAL_RADIUS);
        mExternalR = typeArray.getDimension(R.styleable.BezierLoadingView_lv_external_radius, DEFAULT_EXTERNAL_RADIUS);
        int defaultColor = 999999;
        int startColor = typeArray.getColor(R.styleable.BezierLoadingView_lv_start_color, defaultColor);
        int endColor = typeArray.getColor(R.styleable.BezierLoadingView_lv_end_color, defaultColor);
        List<Integer> colorList = new ArrayList<>();
        if (startColor != defaultColor) {
            colorList.add(startColor);
        }
        if (endColor != defaultColor) {
            colorList.add(endColor);
        }
        //needs >= 2 number of colors
        if (colorList.size() == 1) {
            colorList.add(colorList.get(0));
        }

        if (colorList.size() == 0) {
            mColors = new int[]{ContextCompat.getColor(getContext(), R.color.loading_yellow), ContextCompat.getColor(getContext(), R.color.loading_pink)};
        } else {
            mColors = new int[colorList.size() + 1];
            for (int i = 0; i < colorList.size(); i++) {
                mColors[i] = colorList.get(i);
            }
            mColors[2] = Color.RED;
        }
        typeArray.recycle();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        setShader();
        resetPoint();
    }

    /**
     * 设置渐变色
     */
    private void setShader() {
        Shader mLinearGradient = new LinearGradient(mWidth / 2, mHeight / 2 - mExternalR, mWidth / 2, mHeight / 2, mColors, null,
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
    }

    /**
     * 启动定时器
     */
    public void start() {
        if (mTimer == null || mTimer.isUnsubscribed()) {
            mTimer = Observable.interval(mDuration, TimeUnit.MILLISECONDS)
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            dealTimerBusiness();
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {

                        }
                    });
        }
        this.setVisibility(VISIBLE);
    }

    public void stop() {
        if (mTimer != null) {
            mTimer.unsubscribe();
        }
        this.setVisibility(GONE);
    }

    /**
     * 定时任务
     */
    private void dealTimerBusiness() {

        setOffset((mAngle % mRadian) / (float) mRadian);

        mAngle++;
        if (mAngle > 360) {
            mAngle = mAngle - 360;
            mCyclic++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawBezier(canvas);
    }

    /**
     * 绘制各点圆
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
//        LogUtils.i(" % ",mAngle+" "+mRadian+" "+mAngle%mRadian+" "+mGetSmallerCircleRadius+" "+mGetBiggerCircleRadius);
//        if( mAngle % mRadian > 0 ) {
//            canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetSmallerCircleRadius < mInternalR ? mInternalR : mGetSmallerCircleRadius, mPaint);
//        }else{
//            canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetBiggerCircleRadius < mInternalR ? mInternalR : mGetBiggerCircleRadius, mPaint);
//        }


        for (int i = 0; i < mPoints.size(); i++) {

            int index = mAngle / mRadian;
            if (isEvenCyclic()) {
//                if (i == index) {
//                    if (mAngle % mRadian == 0) {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), getMaxInternalRadius(), mPaint);
//                    } else if (mAngle % mRadian > 0) {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetSmallerCircleRadius < mInternalR ? mInternalR : mGetSmallerCircleRadius, mPaint);
//                    }
//                } else if (i == index + 1) {
//                    if (mAngle % mRadian == 0) {
//                        canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mInternalR, mPaint);
//                    } else {
//                        canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mGetBiggerCircleRadius < mInternalR ? mInternalR : mGetBiggerCircleRadius, mPaint);
//                    }
//                } else if (i > index + 1) {
//                    canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mInternalR, mPaint);
//                }
                if( i > index ){
                    canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mInternalR, mPaint);

                    if( mAngle%mRadian > 0 && mAngle % mRadian < mRadian/2 ){
                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetSmallerCircleRadius < mInternalR ? mInternalR : mGetSmallerCircleRadius, mPaint);
                    }else{
                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetBiggerCircleRadius < mInternalR ? mInternalR : mGetBiggerCircleRadius, mPaint);
                    }

                }
            } else {
//                if (i < index) {
//                    canvas.drawCircle(mPoints.get(i + 1).x, mPoints.get(i + 1).y, mInternalR, mPaint);
//                } else if (i == index) {
//                    if (mAngle % mRadian == 0) {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), getMaxInternalRadius(), mPaint);
//                    } else {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetSmallerCircleRadius < mInternalR ? mInternalR : mGetSmallerCircleRadius, mPaint);
//                    }
//                } else if (i == index + 1) {
//                    if (mAngle % mRadian == 0) {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), getMinInternalRadius(), mPaint);
//                    } else if (mAngle % mRadian > 0) {
//                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetBiggerCircleRadius < mInternalR ? mInternalR : mGetBiggerCircleRadius, mPaint);
//                    }
//                }
                if (i <= index ) {
                    canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, mInternalR, mPaint);
                    Paint paint = new Paint();
                    paint.setColor(Color.GREEN);
                    canvas.drawText("" + i + " " + mPoints.size(), mPoints.get(i).x, mPoints.get(i).y, paint);
                    if( mAngle%mRadian > 0 && mAngle % mRadian < mRadian/2 ){
                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetSmallerCircleRadius < mInternalR ? mInternalR : mGetSmallerCircleRadius, mPaint);
                    }else{
                        canvas.drawCircle(getCircleX(mAngle), getCircleY(mAngle), mGetBiggerCircleRadius < mInternalR ? mInternalR : mGetBiggerCircleRadius, mPaint);
                    }

                }
            }
        }
    }

    /**
     * 绘制贝赛尔曲线
     *
     * @param canvas
     */
    private void drawBezier(Canvas canvas) {

        mPath.reset();

        int circleIndex = mAngle / mRadian;

        float rightX = getCircleX(mAngle);
        float rightY = getCircleY(mAngle);

        float leftX, leftY;
        if (isEvenCyclic()) {
            int index = circleIndex+1;
            leftX = mPoints.get(index >= mPoints.size() ? mPoints.size() - 1 : index).x;
            leftY = mPoints.get(index >= mPoints.size() ? mPoints.size() - 1 : index).y;
        } else {
            int index = circleIndex;
            leftX = mPoints.get(index < 0 ? 0 : index).x;
            leftY = mPoints.get(index < 0 ? 0 : index).y;
        }

        double theta = getTheta(new PointF(leftX, leftY), new PointF(rightX, rightY));
        float sinTheta = (float) Math.sin(theta);
        float cosTheta = (float) Math.cos(theta);

        PointF pointF1 = new PointF(leftX - mInternalR * sinTheta, leftY + mInternalR * cosTheta);
        PointF pointF2 = new PointF(rightX - mInternalR * sinTheta, rightY + mInternalR * cosTheta);
        PointF pointF3 = new PointF(rightX + mInternalR * sinTheta, rightY - mInternalR * cosTheta);
        PointF pointF4 = new PointF(leftX + mInternalR * sinTheta, leftY - mInternalR * cosTheta);

        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5);
        canvas.drawCircle(pointF1.x,pointF1.y,2,paint);
        canvas.drawText("1",pointF1.x,pointF1.y,paint);
        canvas.drawCircle(pointF2.x,pointF2.y,2,paint);
        canvas.drawText("2",pointF2.x,pointF2.y,paint);
        canvas.drawCircle(pointF3.x,pointF3.y,2,paint);
        canvas.drawText("3",pointF3.x,pointF3.y,paint);
        canvas.drawCircle(pointF4.x,pointF4.y,2,paint);
        canvas.drawText("4",pointF4.x,pointF4.y,paint);


        if (isEvenCyclic()) {//融合
            if (mAngle % mRadian < mRadian / 2) {

                mPath.moveTo(pointF3.x, pointF3.y);
                mPath.quadTo(
                        rightX + (leftX - rightX) / (mRadian / 2) * (mAngle % mRadian),
                        rightY + (leftY - rightY) / (mRadian / 2) * (mAngle % mRadian),
                        pointF2.x, pointF2.y);

//                canvas.drawPoint(
//                        rightX + (leftX - rightX) / (mRadian / 2) * (mAngle % mRadian),
//                        rightY + (leftY - rightY) / (mRadian / 2) * (mAngle % mRadian),paint);
//
//                canvas.drawText(
//                        "5",
//                        rightX + (leftX - rightX) / (mRadian / 2) * (mAngle % mRadian),
//                        rightY + (leftY - rightY) / (mRadian / 2) * (mAngle % mRadian),paint);

                mPath.lineTo(pointF3.x, pointF3.y);

                mPath.moveTo(pointF4.x, pointF4.y);
                mPath.quadTo(leftX + (rightX - leftX) / (mRadian / 2) * ( mAngle % mRadian),
                        leftY + (rightY - leftY) / (mRadian / 2) * ( mAngle % mRadian), pointF1.x, pointF1.y);

//                canvas.drawPoint(leftX + (rightX - leftX) / (mRadian / 2) * ( mAngle % mRadian),
//                        leftY + (rightY - leftY) / (mRadian / 2) * ( mAngle % mRadian),paint);
//
//                canvas.drawText("6",leftX + (rightX - leftX) / (mRadian / 2) * ( mAngle % mRadian),
//                        leftY + (rightY - leftY) / (mRadian / 2) * ( mAngle % mRadian),paint);

                mPath.lineTo(pointF4.x, pointF4.y);

                mPath.close();
                canvas.drawPath(mPath, mPaint);
                return;
            }
        } else {//分离
            if (circleIndex > 0 && mAngle % mRadian > mRadian / 2) {

                mPath.moveTo(pointF3.x, pointF3.y);
                mPath.quadTo(
                        rightX +(leftX - rightX) / (mRadian / 2) * (mRadian - mAngle % mRadian),
                        rightY +(leftY - rightY) / (mRadian / 2) * (mRadian - mAngle % mRadian),
                        pointF2.x, pointF2.y);
                mPath.lineTo(pointF3.x, pointF3.y);

                mPath.moveTo(pointF4.x, pointF4.y);
                mPath.quadTo(
                        leftX + (rightX - leftX) / (mRadian / 2) * (mRadian - mAngle % mRadian),
                        leftY + (rightY - leftY) / (mRadian / 2) * (mRadian - mAngle % mRadian),
                        pointF1.x, pointF1.y);
                mPath.lineTo(pointF4.x, pointF4.y);

                mPath.close();
                canvas.drawPath(mPath, mPaint);
                return;
            }
        }

        if (circleIndex == 0 && !isEvenCyclic()) return;

        mPath.moveTo(pointF1.x, pointF1.y);
        mPath.quadTo((leftX + rightX) / 2, (leftY + rightY) / 2, pointF2.x, pointF2.y);

        canvas.drawPoint(
                (leftX + rightX) / 2,
                (leftY + rightY) / 2,paint);

        canvas.drawText(
                "7",
                (leftX + rightX) / 2,
                (leftY + rightY) / 2,paint);

        mPath.lineTo(pointF3.x, pointF3.y);
        mPath.quadTo((leftX + rightX) / 2, (leftY + rightY) / 2, pointF4.x, pointF4.y);

        canvas.drawPoint((leftX + rightX) / 2,
                (leftY + rightY) / 2,paint);

        canvas.drawText("8",(leftX + rightX) / 2,
                (leftY + rightY) / 2,paint);

        mPath.lineTo(pointF1.x, pointF1.y);

        mPath.close();

        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 创建属性动画
     */
    private void createAnimator() {

        if (mPoints.isEmpty()) {
            return;
        }
        mAnimators.clear();

        ValueAnimator circleGetSmallerAnimator = ValueAnimator.ofFloat(getMaxInternalRadius(), getMinInternalRadius());
        circleGetSmallerAnimator.setDuration(5000L);
        circleGetSmallerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGetSmallerCircleRadius = (float) animation.getAnimatedValue();
            }
        });
        mAnimators.add(circleGetSmallerAnimator);

        ValueAnimator circleGetBiggerAnimator = ValueAnimator.ofFloat(getMinInternalRadius(), getMaxInternalRadius());
        circleGetBiggerAnimator.setDuration(5000L);
        circleGetBiggerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGetBiggerCircleRadius = (float) animation.getAnimatedValue();
            }
        });
        mAnimators.add(circleGetBiggerAnimator);

    }

    private void seekAnimator(float offset) {
        for (ValueAnimator animator : mAnimators) {
            animator.setCurrentPlayTime((long) (5000.0F * offset));
        }

    }

    public void setOffset(float offSet) {
        createAnimator();
        seekAnimator(offSet);
        postInvalidate();
    }

    /**
     * 重置圆点
     */
    private void resetPoint() {

        x0 = mWidth / 2;
        y0 = mHeight / 2;

        createPoints();

        if (!mPoints.isEmpty()) {
            mGetBiggerCircleRadius = getMaxInternalRadius();
            mGetSmallerCircleRadius = getMinInternalRadius();
            postInvalidate();
        }
    }

    /**
     * 创建圆点
     * <p>
     * 圆点坐标：(x0,y0)
     * 半径：r
     * 角度：a0
     * <p>
     * 则圆上任一点为：（x1,y1）
     * x1 = x0 + r * cos(ao * 3.14 /180 )
     * y1 = y0 + r * sin(ao * 3.14 /180 )
     */
    private void createPoints() {
        mPoints.clear();
        for (int i = 0; i <= 360; i++) {
            if (i % mRadian == 0) {
                float x1 = getCircleX(i);
                float y1 = getCircleY(i);
                mPoints.add(new PointF(x1, y1));
            }
        }
    }

    /**
     * 是否是偶数圈
     *
     * @return
     */
    private boolean isEvenCyclic() {
        return mCyclic % 2 == 0;
    }

    /**
     * 获得以(x0,y0)为圆心的圆y坐标
     *
     * @param angle 角度
     * @return
     */
    private float getCircleY(int angle) {
        return y0 + mExternalR * (float) Math.sin(angle * 3.14 / 180);
    }

    /**
     * 获得以(x0,y0)为圆心的圆x坐标
     *
     * @param angle 角度
     * @return
     */
    private float getCircleX(int angle) {
        return x0 + mExternalR * (float) Math.cos(angle * 3.14 / 180);
    }

    /**
     * 获取theta值
     *
     * @param pointCenterLeft
     * @param pointCenterRight
     * @return
     */
    private double getTheta(PointF pointCenterLeft, PointF pointCenterRight) {
        double theta = Math.atan((pointCenterRight.y - pointCenterLeft.y) / (pointCenterRight.x - pointCenterLeft.x));
        return theta;
    }

    /**
     * 设置外圆半径
     *
     * @param progress
     */
    public void setExternalRadius(int progress) {
        int R = (int) ((progress / 100f) * MAX_EXTERNAL_R);
        mExternalR = R < MIN_EXTERNAL_R ? MIN_EXTERNAL_R : R;
        setShader();
        createPoints();
    }

    /**
     * 设置内圆半径
     *
     * @param progress
     */
    public void setInternalRadius(int progress) {
        int r = (int) ((progress / 100f) * MAX_INTERNAL_R);
        mInternalR = r < MIN_INTERNAL_R ? MIN_INTERNAL_R : r;
    }

    /**
     * 设置时间间隔
     *
     * @param progress
     */
    public void setDuration(int progress) {
        stop();
        int duration = (int) ((1 - progress / 100f) * MAX_DURATION);
        mDuration = duration < MIN_DURATION ? MIN_DURATION : duration;
        start();
    }

    /**
     * 最大内圆半径
     *
     * @return
     */
    private float getMaxInternalRadius() {
        return mInternalR / 10f * 14f;
    }

    /**
     * 最小内圆半径
     *
     * @return
     */
    private float getMinInternalRadius() {
        return mInternalR / 10f;
    }

    private int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
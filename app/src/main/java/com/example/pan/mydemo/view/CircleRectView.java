package com.example.pan.mydemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.example.pan.mydemo.R;

/**
 * 圆角矩形View (可转变为圆形)
 * Created by PAN on 2017/6/8.
 */
public class CircleRectView extends View {

    private final int DEFAULT_RADIU = 40;
    private final int DEFAULT_TEXT_SIZE = 24;
    private Paint mPaint, mTextPaint;
    private int mTextSize;
    private int mStrokeColor;
    private int mTextColor;
    private int mBackgroundColor;
    private String mText;
    private int mRectWidth = 0;
    private int mRadiu;
    private int mTopBottomPadding = 0;
    private int mLeftRightPadding = 0;
    private int mTextMeasureWidth = 0;

    private Path mPath;
    private RectF leftRect;
    private RectF rightRect;
    private RectF contentRect;


    public CircleRectView(Context context) {
        this(context, null);
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleRectView);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleRectView_android_textSize, DEFAULT_TEXT_SIZE);
        mStrokeColor = typedArray.getColor(R.styleable.CircleRectView_stroke_color, Color.RED);
        mTextColor = typedArray.getColor(R.styleable.CircleRectView_android_textColor, Color.WHITE);
        mText = typedArray.getString(R.styleable.CircleRectView_android_text);
        mRadiu = typedArray.getDimensionPixelOffset(R.styleable.CircleRectView_radiu, DEFAULT_RADIU);
        mTopBottomPadding = typedArray.getDimensionPixelOffset(R.styleable.CircleRectView_contentPaddingTB, 10);
        mLeftRightPadding = typedArray.getDimensionPixelOffset(R.styleable.CircleRectView_contentPaddingLR, 10);
        mBackgroundColor = typedArray.getColor(R.styleable.CircleRectView_backgroundColor, Color.WHITE);
        typedArray.recycle();

        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mStrokeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);

        leftRect = new RectF();
        rightRect = new RectF();
        contentRect = new RectF();
    }

    /**
     * 最小可超出控件最小宽度，最大可超出控件最大宽度（控件已做边界处理）
     * @param width
     */
    public void setRectWidth(int width) {
        mRectWidth = width;
        requestLayout();
    }

    public int getRectWidth(){
        return mRectWidth;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int resultW = widthSize;
        int resultH = heightSize;

        int contentW = 0, contentH = 0;

        if (widthMode == MeasureSpec.AT_MOST) {
            if (!TextUtils.isEmpty(mText)) {
                mTextMeasureWidth = (int) mTextPaint.measureText(mText);
                contentW = mTextMeasureWidth + mLeftRightPadding * 2;
            }
            mRectWidth = Math.max(mRectWidth, contentW);
            resultW = mRectWidth + mRadiu * 2 < widthSize ? mRectWidth + mRadiu * 2 : widthSize;
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            contentH = mTopBottomPadding * 2 + mTextSize;
            resultH = contentH < heightSize ? contentH : heightSize;
        }

        resultW = resultW < 2 * mRadiu ? 2 * mRadiu : resultW;
        resultH = resultH < 2 * mRadiu ? 2 * mRadiu : resultH;
        setMeasuredDimension(resultW, resultH);

        if (widthMode == MeasureSpec.EXACTLY) {
            mRectWidth = resultW - mRadiu * 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        if (mPath == null) {
            mPath = new Path();
        }

        mPath.reset();

        int left = cx - mRectWidth / 2 - mRadiu;
        int top = 0;
        int right = cx + mRectWidth / 2 + mRadiu;
        int bottom = getHeight();

        leftRect.set(left, top, left + mRadiu * 2, bottom);
        rightRect.set(right - mRadiu * 2, top, right, bottom);
        contentRect.set(cx - mRectWidth / 2, top, cx + mRectWidth / 2, bottom);
        //path 起始位置
        mPath.moveTo(cx - mRectWidth / 2, bottom);
        // 左边半圆
        mPath.arcTo(leftRect,
                90.0f, 180f);
        //连接到右边半圆
        mPath.lineTo(cx + mRectWidth / 2, top);
        // 右边半圆
        mPath.arcTo(rightRect,
                270.0f, 180f);
        // path 闭合
        mPath.close();

        // 以填充的方向将图形填充为指定的背景色
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mBackgroundColor);
        canvas.drawPath(mPath, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mStrokeColor);

        if (!TextUtils.isEmpty(mText)) {
            int textDescent = (int) mTextPaint.getFontMetrics().descent;
            int textAscent = (int) mTextPaint.getFontMetrics().ascent;
            int delta = Math.abs(textAscent) - textDescent;
            canvas.drawText(mText, cx, cy + delta / 2, mTextPaint);
        }
    }
}

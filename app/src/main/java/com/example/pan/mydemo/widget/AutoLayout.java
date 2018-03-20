package com.example.pan.mydemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cus.pan.library.utils.Logger;
import com.example.pan.mydemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by PAN on 2018/1/9.
 * 自动画分割线Layout
 * 可设置参数：
 * is_square：是否设置子View为正方形
 * colums：一行有几个view
 * border_width:分割线宽度
 * border_color:分割线颜色
 */
public class AutoLayout extends ViewGroup {

    private boolean isSquare;
    private int columns;
    private int splitBorderWidth;
    private int splitBorderColor;

    private Paint paint = new Paint();

    private int childWidth = 0;
    private List<Rect> rects = new ArrayList<>();

    public AutoLayout(Context context) {
        this(context, null);
    }

    AutoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoLayout);
        isSquare = array.getBoolean(R.styleable.AutoLayout_is_square, false);
        columns = array.getInt(R.styleable.AutoLayout_colums, 0);
        splitBorderWidth = array.getDimensionPixelOffset(R.styleable.AutoLayout_split_border_width, 0);
        splitBorderColor = array.getColor(R.styleable.AutoLayout_split_border_color, 0);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        Logger.i("width " + splitBorderWidth + " " + splitBorderColor);
        paint.setColor(splitBorderColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);//测量子View
        int childCount = getChildCount();

        if (columns > 0) {
            childWidth = widthSize / columns;
        }

        int currentHeight = 0, currentWidth = widthSize;
        int currentLineMaxHeight = 0;
        int currentLineWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int cWidth = 0, cHeight = 0;
            if (columns > 0) {
                cWidth = childWidth;
            } else {
                cWidth = child.getMeasuredWidth();
            }
            if (isSquare) {
                cHeight = cWidth;
            } else {
                cHeight = child.getMeasuredHeight();
            }

//            if (heightMode == MeasureSpec.AT_MOST) { //根据内容来计算父布局高度
            if (currentLineWidth + cWidth <= widthSize - getPaddingLeft() - getPaddingRight()) {
                currentLineWidth += cWidth;
                currentLineMaxHeight = Math.max(currentLineMaxHeight, cHeight);
            } else { //換行
                currentHeight += currentLineMaxHeight;
                currentLineMaxHeight = cHeight;
                currentLineWidth = cWidth;
            }
//            }

        }
        if (currentLineWidth > 0) {
            currentHeight += currentLineMaxHeight;
        }
        int resultW = 0, resultH = 0;

        resultW = widthSize;
//        if (heightMode == MeasureSpec.AT_MOST) {
        if (currentHeight > 0) {
            resultH = getPaddingTop() + getPaddingBottom() + currentHeight;
        }
//        }
        setMeasuredDimension(resultW, resultH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int widthSize = r - l;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int startL = paddingLeft;
        int startT = paddingTop;

        int childCount = getChildCount();
        int currentLineWidth = 0;
        int currentLineMaxHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int cWidth = 0, cHeight = 0;
            if (columns > 0) {
                cWidth = widthSize / columns;
            } else {
                cWidth = child.getMeasuredWidth();
            }
            if (isSquare) {
                cHeight = cWidth;
            } else {
                cHeight = child.getMeasuredHeight();
            }

            if (currentLineWidth > 0 && currentLineWidth < widthSize - getPaddingLeft() - getPaddingRight()) {
                Rect rectColumns = new Rect();
                rectColumns.left = startL;
                startL += splitBorderWidth;
                rectColumns.right = startL;
                rectColumns.top = startT;
                rectColumns.bottom = startT + cHeight;
                rects.add(rectColumns);
            }
            if (currentLineWidth + cWidth <= widthSize - getPaddingLeft() - getPaddingRight()) {
                currentLineWidth += cWidth;
                currentLineMaxHeight = Math.max(currentLineMaxHeight, cHeight);
            } else {
                startT += currentLineMaxHeight + splitBorderWidth;
                startL = paddingLeft;
                currentLineWidth = cWidth;
            }
            if (currentLineWidth > 0 && startT > getPaddingTop() && startT < b - getPaddingBottom()) {
                Rect rectRows = new Rect();
                rectRows.left = startL;
                rectRows.right = startL + cWidth + splitBorderWidth;
                rectRows.top = startT - splitBorderWidth;
                rectRows.bottom = startT;
                rects.add(rectRows);
            }
            child.layout(startL, startT, startL + cWidth, startT + cHeight);
            startL += cWidth;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Logger.i("draw " + splitBorderColor + " " + rects.size() + " ");
        if (splitBorderColor != 0) {
            for (int i = 0; i < rects.size(); i++) {
                canvas.drawRect(rects.get(i), paint);
//                Logger.i("draw " + rects.get(i).toString());
            }
        }
    }
}

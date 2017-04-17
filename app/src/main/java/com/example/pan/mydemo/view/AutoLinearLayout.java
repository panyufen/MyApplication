package com.example.pan.mydemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by PAN on 2017/3/15.
 * 自动换行的LineLayout
 */
public class AutoLinearLayout extends ViewGroup {
    private int mChildWidth = 0;
    private int mChildHeight = 0;

    private int mColumns = 1;
    private int mSpace = 0;

    public AutoLinearLayout(Context context) {
        this(context, null);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setColumns(int count, int space) {
        mColumns = count;
        mSpace = space;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mChildWidth = (r - l) / mColumns - mSpace * 2;
        mChildHeight = mChildWidth;

        int childWidth = mChildWidth;
        int childHeight = mChildHeight;
        if (childWidth <= 0 || childHeight <= 0) return;
        int columns = (r - l) / childWidth;
        if (columns < 0) {
            columns = 1;
        } else {
            columns = mColumns;
        }
        int x = mSpace;
        int y = mSpace;
        int i = 0;
        int count = getChildCount();
        for (int j = 0; j < count; j++) {
            final View childView = getChildAt(j);
            int w = childView.getMeasuredWidth();
            int h = childView.getMeasuredHeight();

            int left = x + ((childWidth - w) / 2);
            int top = y + ((childHeight - h) / 2);

            childView.layout(left, top, left + w, top + h);

            if (i >= (columns - 1)) {
                i = 0;
                x = mSpace;
                y += childHeight + mSpace * 2;
            } else {
                i++;
                x += childWidth + mSpace * 2;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mChildWidth = mChildWidth > 0 ? mChildWidth : getMeasuredWidth() / mColumns - mSpace * 2;
        mChildHeight = mChildWidth;

        int cellWidthSpec = MeasureSpec.makeMeasureSpec(mChildWidth, MeasureSpec.EXACTLY);
        int cellHeightSpec = MeasureSpec.makeMeasureSpec(mChildHeight, MeasureSpec.EXACTLY);

        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            childView.measure(cellWidthSpec, cellHeightSpec);
        }
//        setMeasuredDimension(resolveSize(mChildWidth * count, widthMeasureSpec),resolveSize(mChildHeight * count, heightMeasureSpec));
//         //setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        int parentHeight = (int) Math.ceil(count / (float) mColumns) * (mChildHeight + mSpace * 2);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }
}

package com.example.pan.mydemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cus.pan.library.utils.LogUtils;

/**
 * 自动换行LinearLayout
 * Created by PAN on 2017/6/8.
 */
public class AutoLinearLayoutV2 extends ViewGroup {

    public AutoLinearLayoutV2(Context context) {
        super(context);
    }

    public AutoLinearLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLinearLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

        LogUtils.i("onMeasure ");
        int childCount = getChildCount();
        MarginLayoutParams childParams = null;
        int currentHeight = 0, currentWidth = widthSize;
        int currentLineMaxHeight = 0;
        int currentLineWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            childParams = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
            int cHeight = child.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;

            boolean isfold = true;

            if (heightMode == MeasureSpec.AT_MOST) { //根据内容来计算父布局高度
                if (currentLineWidth + cWidth <= widthSize - getPaddingLeft() - getPaddingRight()) {
                    isfold = true;
                    currentLineWidth += cWidth;
                    currentLineMaxHeight = Math.max(currentLineMaxHeight, cHeight);
                } else { //換行
                    isfold = false;
                    currentHeight += currentLineMaxHeight;
                    currentLineMaxHeight = cHeight;
                    currentLineWidth = cWidth;
                }
            }

            LogUtils.i("measureHeight " + currentHeight + " " + currentLineMaxHeight + " " + i + " " + isfold);
        }
        if (currentLineWidth > 0) {
            currentHeight += currentLineMaxHeight;
        }
        LogUtils.i("measureHeight " + currentHeight);
        int resultW = 0, resultH = 0;

        resultW = widthSize;
        if (heightMode == MeasureSpec.AT_MOST) {
            if (currentHeight > 0) {
                resultH = getPaddingTop() + getPaddingBottom() + currentHeight;
            }
        }
        LogUtils.i("height " + getPaddingTop() + " " + getPaddingBottom() + " " + currentHeight + " " + resultH);
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
        MarginLayoutParams params = null;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            params = (MarginLayoutParams) child.getLayoutParams();
            int cWidth = child.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int cHeight = child.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            if (currentLineWidth + cWidth <= widthSize - getPaddingLeft() - getPaddingRight()) {
                currentLineWidth += cWidth;
                currentLineMaxHeight = Math.max(currentLineMaxHeight, cHeight);
            } else {
                startT += currentLineMaxHeight;
                startL = paddingLeft;
                currentLineWidth = cWidth;
            }
            child.layout(
                    startL + params.leftMargin,
                    startT + params.topMargin,
                    startL + params.leftMargin + child.getMeasuredWidth(),
                    startT + params.topMargin + child.getMeasuredHeight());
            startL += cWidth;
        }
    }
}

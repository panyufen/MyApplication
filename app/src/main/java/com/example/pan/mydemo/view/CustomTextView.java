package com.example.pan.mydemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by PAN on 2016/7/15.
 */
public class CustomTextView extends TextView {

    int orgWidth = 0, orgHeight = 0;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        orgWidth = orgWidth > 0 ? orgWidth : getMeasuredWidth();
        orgHeight = orgHeight > 0 ? orgHeight : getMeasuredHeight();
        Log.i("width height", orgWidth + " " + orgHeight);
//        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        setMeasuredDimension(orgWidth / 2, orgWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.rotate(90f, orgHeight / 2f, orgHeight / 2f);
        super.onDraw(canvas);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }
}

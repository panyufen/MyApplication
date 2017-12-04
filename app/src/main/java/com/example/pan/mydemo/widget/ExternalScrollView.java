package com.example.pan.mydemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by PAN on 2017/4/1.
 */

public class ExternalScrollView extends ScrollView {

    private float xDistance, yDistance, xLast = 0, yLast = 0;

    public ExternalScrollView(Context context) {
        this(context, null);
    }

    public ExternalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExternalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 按下时 滑动为纵向，ScrollView才处理事件，否则分给子View ，
     * 到下次按下时，重新分发
     */
    boolean isFirstTouch = false;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                isFirstTouch = true;
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = ev.getX();
                float curY = ev.getY();
                if (isFirstTouch) {
                    xDistance = Math.abs(curX - xLast);
                    yDistance = Math.abs(curY - yLast);
                }
                isFirstTouch = false;
                if (xDistance >= yDistance) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}

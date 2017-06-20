package com.example.pan.mydemo.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.cus.pan.library.utils.LogUtils;

/**
 * Created by PAN on 2017/6/19.
 */

public class BottomSheetHelperBehavior extends CoordinatorLayout.Behavior<View> {

    private BottomSheetViewStateChangeListener mListener;

    public BottomSheetHelperBehavior(Context context) {
        this(context, null);
    }

    public BottomSheetHelperBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnStateChangeListener(BottomSheetViewStateChangeListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if (dy > 0) { //向上滑动
            mListener.onChange(true);
        } else {
            mListener.onChange(false);
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        LogUtils.i("other " + child + " " + target + " " + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed);
    }

    public static <V extends View> BottomSheetHelperBehavior from(V view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (!(params instanceof CoordinatorLayout.LayoutParams)) {
            throw new IllegalArgumentException("这个View不是CoordinatorLayout的子View");
        }
        CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) params).getBehavior();
        if (!(behavior instanceof BottomSheetHelperBehavior)) {
            throw new IllegalArgumentException("这个View的Behaviro不是BottomSheetBehaviorHelper");
        }
        return (BottomSheetHelperBehavior) behavior;
    }


    public interface BottomSheetViewStateChangeListener {

        void onChange(boolean state);
    }
}

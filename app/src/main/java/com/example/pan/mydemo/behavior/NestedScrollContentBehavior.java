package com.example.pan.mydemo.behavior;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;

/**
 * Created by PAN on 2017/6/6.
 */

public class NestedScrollContentBehavior extends CoordinatorLayout.Behavior<View> {
    private int targetMinTop;
    private int targetMaxTop;
    private View mDependency;

    public NestedScrollContentBehavior() {
    }

    public NestedScrollContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency != null && dependency.getId() == R.id.imageview_bg;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        LogUtils.i("onDependentViewChanged");
        mDependency = dependency;
        offsetChildAsNeeded(parent, child, dependency);
        return super.onDependentViewChanged(parent, child, dependency);
    }


    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        targetMinTop = dependency.getTop();
        targetMaxTop = dependency.getBottom();
        child.setTranslationY(targetMaxTop);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        LogUtils.i("onStartNestedScroll ");
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
//        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        int ty = (int) target.getTranslationY();
        LogUtils.i("onNestedPreScroll " + ty + " " + dx + " " + dy + " " + targetMinTop + " " + targetMaxTop);
        if (dy > 0) {  //列表上滑
            if (ty > targetMinTop && ty <= targetMaxTop) {
                if (ty - dy < targetMinTop) {
                    target.setTranslationY(targetMinTop);
                    consumed[1] = ty;
                } else {
                    target.setTranslationY(ty - dy);
                    consumed[1] = dy;
                }
            } else {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
            }
        } else {
            if (ty >= targetMinTop && ty < targetMaxTop && target.getScrollY() == 0) {
                if (ty - dy > targetMaxTop) {
                    target.setTranslationY(targetMaxTop);
                    consumed[1] = targetMaxTop - ty;
                } else {
                    target.setTranslationY(ty - dy);
                    consumed[1] = dy;
                }
            } else {
                super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
            }
        }


    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        LogUtils.i("onNestedScroll " + dxConsumed + " " + dyConsumed + " " + dxUnconsumed + " " + dyUnconsumed + " " + target + " " + child);
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        int y = (int) target.getTranslationY();
        LogUtils.i("onStopNestedScroll " + y + " " + targetMinTop + " " + targetMaxTop);
        if (y > targetMinTop && y < targetMaxTop) {
            if ((y - targetMinTop) * 2 < targetMaxTop - targetMinTop) {
                startMoveAnimator(target, y, targetMinTop);
            } else {
                startMoveAnimator(target, y, targetMaxTop);
            }
        } else {
            super.onStopNestedScroll(coordinatorLayout, child, target);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY) {
        int targetTop = (int) target.getTranslationY();
        if (targetTop != targetMaxTop && targetTop != targetMinTop) { //代表不在底部 不在頂部
            return true;
        }
        boolean b = super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
        LogUtils.i("onNestedPreFling " + velocityX + " " + velocityY + " " + b);
        return b;
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    private void startMoveAnimator(final View v, final int start, final int end) {
        LogUtils.i("startMoveAnimator " + start + " " + end);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start, end);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                v.setTranslationY(value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        valueAnimator.start();
    }

}

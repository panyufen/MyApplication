package com.example.pan.mydemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;

/**
 * Created by PAN on 2017/3/28.
 * //右侧滑出菜单(仿锤子通讯录)
 */
public class RightSwipMenuLayout extends RelativeLayout {

    private String TAG = "RightSwipMenu ";

    LinearLayout mContentLayout;
    View markBg;
    LinearLayout mMenuLayout;

    private int mMenuDefaultLeft = 0;
    private int mMenuEndLeft = 0;
    private int mMenuLastLeft = mMenuDefaultLeft; //默认是初始未知 (也用于判断菜单状态)
    private int mSwipLeft = 0;

    private boolean isMenuClosed = true;

    private final int RANGE = 4; //滑动到菜单4分之1的位置时触发打开或关闭菜单动画

    private float xDistance, yDistance, xLast = 0, yLast = 0;
    private ViewDragHelper mViewDragHelper;

    private OnSwipMenuListener mOnSwipMenuListener;

    public RightSwipMenuLayout(Context context) {
        this(context, null);
    }

    public RightSwipMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightSwipMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child == mMenuLayout) {
                    return true;
                }
                return false;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                LogUtils.i(TAG + "onViewReleased " + (releasedChild == mMenuLayout) + " " + xvel + " " + yvel + " " + mSwipLeft);
                super.onViewReleased(releasedChild, xvel, yvel);
                if (releasedChild == mMenuLayout) {
                    LogUtils.i(TAG + "onViewReleased " + mMenuLastLeft + " " + mMenuDefaultLeft + " " + mMenuEndLeft);
                    if (mMenuLastLeft == mMenuDefaultLeft) {  //如果是初始位置
                        if ((mSwipLeft - mMenuEndLeft) < mMenuLayout.getWidth() * (RANGE - 1) / RANGE) {
                            mMenuLastLeft = mMenuEndLeft;
                            markBg.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if ((mSwipLeft - mMenuEndLeft) > mMenuLayout.getWidth() / RANGE) {
                            mMenuLastLeft = mMenuDefaultLeft;
                            markBg.setVisibility(View.GONE);
                        }
                    }
                    mViewDragHelper.settleCapturedViewAt(mMenuLastLeft, releasedChild.getTop());
//                    LogUtils.i(TAG + "onViewReleased1 " + mMenuLastLeft);
                    invalidate();
                }
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                mSwipLeft = left < getMeasuredWidth() - mMenuLayout.getChildAt(0).getWidth() ? left : getMeasuredWidth() - mMenuLayout.getChildAt(0).getWidth();
                mSwipLeft = mSwipLeft > mMenuEndLeft ? mSwipLeft : mMenuEndLeft; //只能滑动菜单宽度大小的距离
                LogUtils.i(TAG + "clampViewPositionHorizontal " + " " + left + " " + dx + " " + mSwipLeft);
                return mSwipLeft;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (mOnSwipMenuListener != null) {
                    mOnSwipMenuListener.onChanged();
                }
                if (left == mMenuDefaultLeft) { //菜单已关闭
                    isMenuClosed = true;
                    if (mOnSwipMenuListener != null) {
                        mOnSwipMenuListener.onHide();
                    }
                } else {
                    isMenuClosed = false;
                    if (left == mMenuEndLeft) {//菜单已开启
                        if (mOnSwipMenuListener != null) {
                            mOnSwipMenuListener.onShow();
                        }
                    }
                }

            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }

        });
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.DIRECTION_HORIZONTAL);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if (xDistance < yDistance) {
                    return false;
                }
        }
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mViewDragHelper.processTouchEvent(ev);
        return true;
    }

    public void hideMenu() {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mMenuLayout.getLayoutParams();
        final int margin = -mMenuLayout.getChildAt(mMenuLayout.getChildCount() - 1).getWidth();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                params.rightMargin = (int) (margin * value);
                mMenuLayout.setLayoutParams(params);
                LogUtils.i("onAnimationUpdate " + value);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMenuLastLeft = mMenuDefaultLeft;
                markBg.setVisibility(View.GONE);
                mOnSwipMenuListener.onHide();
            }
        });
        valueAnimator.start();
    }

    public boolean getMenuIsClosed() {
        return isMenuClosed;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentLayout = (LinearLayout) findViewById(R.id.content_layout);
        mMenuLayout = (LinearLayout) findViewById(R.id.menu_layout);
        mMenuLayout.setFocusable(true);
        mMenuLayout.setClickable(true);
        markBg = new View(getContext());
        markBg.setVisibility(View.GONE);
        addView(markBg, 1);
        markBg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("content onclick ");
                if (mMenuLastLeft == mMenuEndLeft) {
                    hideMenu();
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.i("onMeasure " + mMenuLayout.getLeft());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mMenuDefaultLeft == 0) { //只在第一次进行赋值
            mMenuDefaultLeft = mMenuLayout.getLeft();
            mMenuLastLeft = mMenuDefaultLeft;
            mMenuEndLeft = getMeasuredWidth() - mMenuLayout.getWidth();
        }

        LogUtils.i("onlayout " + mMenuLastLeft + " " + mMenuDefaultLeft + " " + mMenuEndLeft + " " + changed + " " + l + " " + t + " " + r + " " + b);
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            invalidate();
        } else {
            LogUtils.i(TAG + "computeScroll end");
        }
    }

    public void setOnSwipMenuListener(OnSwipMenuListener listener) {
        this.mOnSwipMenuListener = listener;
    }

    public interface OnSwipMenuListener {

        void onShow();

        void onChanged();

        void onHide();
    }


}
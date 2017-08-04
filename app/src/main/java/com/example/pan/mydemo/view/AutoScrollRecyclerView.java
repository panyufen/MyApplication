package com.example.pan.mydemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.cus.pan.library.utils.LogUtils;

/**
 * Created by PAN on 2017/8/3.
 * 实现滚动到指定Position
 */
public class AutoScrollRecyclerView extends RecyclerView {

    private int mTargetPos = 0;
    private boolean mNeedMove = false;
    private boolean mIsAnim = false;

    public AutoScrollRecyclerView(Context context) {
        this(context, null);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new RecyclerViewScrollListener());
    }

    public boolean isScrollByAnim() {
        return mIsAnim;
    }

    /**
     * 解决原始方法移动不到指定position的问题
     *
     * @param pos
     */
    @Override
    public void scrollToPosition(int pos) {
        if (pos < 0 && pos >= getAdapter().getItemCount())
            return;
        mTargetPos = pos;
        View firstView = getChildAt(0);
        int firstPos = getChildLayoutPosition(firstView);
        View lastView = getChildAt(getChildCount() - 1);
        int lastPos = getChildLayoutPosition(lastView);
        stopScroll();
        if (pos < firstPos) { //如果目标pos在屏幕上方
            super.scrollToPosition(pos);
        } else if (pos > firstPos && pos < lastPos) {
            int targetPos = pos - firstPos;
            if (targetPos < 0 || targetPos > lastPos)
                return;
            View targetView = getChildAt(targetPos);
            int firstTop = firstView.getTop();
            int targetTop = targetView.getTop();
            scrollBy(0, targetTop - firstTop);
        } else if (pos > lastPos) {
            super.scrollToPosition(pos);
            mNeedMove = true;
        }
    }

    /**
     * 解决原始方法移动不到指定position的问题
     *
     * @param pos
     */
    @Override
    public void smoothScrollToPosition(int pos) {
        if (pos < 0 && pos >= getAdapter().getItemCount())
            return;
        mTargetPos = pos;
        View firstView = getChildAt(0);
        int firstPos = getChildLayoutPosition(firstView);
        View lastView = getChildAt(getChildCount() - 1);
        int lastPos = getChildLayoutPosition(lastView);
        stopScroll();
        if (pos < firstPos) { //如果目标pos在屏幕上方
            super.smoothScrollToPosition(pos);
        } else if (pos > firstPos && pos < lastPos) {
            int targetPos = pos - firstPos;
            if (targetPos < 0 || targetPos > lastPos)
                return;
            View targetView = getChildAt(targetPos);
            int firstTop = firstView.getTop();
            int targetTop = targetView.getTop();
            smoothScrollBy(0, targetTop - firstTop);
        } else if (pos > lastPos) {
            mNeedMove = true;
            mIsAnim = true;
            super.smoothScrollToPosition(pos);
        }
    }


    /**
     * 将pos项滚动到列表中央
     *
     * @param pos
     */
    public void moveCenterByPosition(int pos) {
        View firstView = getChildAt(0);
        int firstPos = getChildLayoutPosition(firstView);
        int lastPos = firstPos + getChildCount() - 1;
//        LogUtils.i("moveCenterByPosition over " + firstPos + " " + pos + " " + " " + lastPos + " ");

        //如果在屏幕外面则先滚动到屏幕里
        if (pos > lastPos) {
            super.scrollToPosition(pos);
            firstPos = pos - getChildCount() + 1;
            lastPos = pos;
//            LogUtils.i("moveCenterByPosition over deal > " + firstPos + " " + pos + " ");
        } else if (pos < firstPos) {
            super.scrollToPosition(pos);
            firstPos = pos;
            lastPos = firstPos + getChildCount() - 1;
//            LogUtils.i("moveCenterByPosition over deal < " + firstPos + " " + pos + " ");
        }

        if (pos >= firstPos && pos <= lastPos) { //如果在屏幕里 则动画滚动
            stopScroll();
            int movePosDis = pos - firstPos;
            View curView = getChildAt(movePosDis);
            int firstTop = firstView.getTop();
            View lastView = getChildAt(getChildCount() - 1);
            int lastTop = lastView.getTop();
            int curTop = curView.getTop();
//            LogUtils.i("moveCenterByPosition " + curTop + " " + firstTop + " " + lastTop);
            if (curTop <= firstTop) {
                scrollBy(0, getTop() - curTop);
                firstView = getChildAt(0);
            } else if (curTop >= lastTop) {
//                LogUtils.i("moveCenterByPosition last " + (lastView.getBottom() - getBottom()));
                scrollBy(0, lastView.getBottom() - getBottom());
                firstView = getChildAt(0);
            }

            int halfHeight = (getBottom() - getTop()) / 2;
            int moveDistence = curTop - firstView.getTop() - halfHeight;
//            LogUtils.i("moveCenterByPosition " + moveDistence);
            smoothScrollBy(0, moveDistence, new DecelerateInterpolator());
        }
    }


    private class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LogUtils.i("onScrollStateChanged " + newState);
            View firstView = recyclerView.getChildAt(0);
            int firstPos = recyclerView.getChildLayoutPosition(firstView);
            if (mIsAnim && mNeedMove && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                mNeedMove = false;
                int movePos = mTargetPos - firstPos;
                if (movePos > 0 && movePos < recyclerView.getChildCount()) {
                    int moveTop = recyclerView.getChildAt(movePos).getTop();
                    if (firstPos == movePos) {
                        return;
                    }
                    recyclerView.scrollBy(0, moveTop);
                    return;
                }
            }
            if (mIsAnim && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                mIsAnim = false;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View firstView = recyclerView.getChildAt(0);
            int firstPos = recyclerView.getChildLayoutPosition(firstView);
            LogUtils.i("onScrolled onScrolled " + mNeedMove + " " + recyclerView.getScrollState());
            if (mNeedMove && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                mNeedMove = false;
                int movePos = mTargetPos - firstPos;
                if (movePos > 0 && movePos < recyclerView.getChildCount()) {
                    int moveTop = recyclerView.getChildAt(movePos).getTop();
                    if (firstPos == movePos) {
                        return;
                    }
                    recyclerView.scrollBy(0, moveTop);
                    return;
                }
            }
        }
    }
}

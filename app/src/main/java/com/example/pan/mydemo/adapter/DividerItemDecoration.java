package com.example.pan.mydemo.adapter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by PAN on 2016/9/19.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private final int DIVIDER_HEIGHT = 1;

    private Paint mDividerPaint = new Paint();

    private int mOrientation;


    public DividerItemDecoration(int orientation) {
        this(orientation, 0);
    }

    public DividerItemDecoration(int orientation, int color) {
        Log.i("DividerItemDecoration ", color + "");
        if (color != 0) {
            mDividerPaint.setAntiAlias(true);
            mDividerPaint.setColor(color);
        }
        setOrientation(orientation);
    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft();
            final int right = child.getRight() + DIVIDER_HEIGHT;
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + DIVIDER_HEIGHT;
            c.drawRect(left, top, right, bottom, mDividerPaint);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getTop();
            final int bottom = child.getBottom() + DIVIDER_HEIGHT;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + DIVIDER_HEIGHT;
            c.drawRect(left, top, right, bottom, mDividerPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);

        if (mOrientation == VERTICAL_LIST) {
            if (position != parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = DIVIDER_HEIGHT;
            }
        } else {
            outRect.right = DIVIDER_HEIGHT;
        }
    }
}

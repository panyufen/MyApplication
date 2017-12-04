package com.example.pan.mydemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;

public class SideBar extends View {
    private RightSwipMenuLayout rightSwipMenuLayout;
    private char[] l;
    private SectionIndexer sectionIndexter = null;
    private AutoScrollRecyclerView list;
    private TextView mDialogText;
    private int m_nItemHeight = 27;
    private float dx, dy;
    private float xDistance, yDistance;
    private Paint mPaint = new Paint();

    private boolean mCanTouchAble = true;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        l = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(25);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }


    public void setListIndexData(char[] tl) {
        this.l = tl;
    }

    public void setListView(AutoScrollRecyclerView _list) {
        list = _list;
        sectionIndexter = (SectionIndexer) _list.getAdapter();
    }

    public void setRightSwipMenuLayout(RightSwipMenuLayout rightSwipMenuLayout) {
        this.rightSwipMenuLayout = rightSwipMenuLayout;
    }

    public void setTextView(TextView mDialogText) {
        this.mDialogText = mDialogText;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        LogUtils.i("side touch " + mCanTouchAble);
        if (!mCanTouchAble) return true;
        if (l == null) throw new IllegalStateException("setListIndexData first");
        int i = (int) event.getY();
        int idx = i / m_nItemHeight;
        if (idx >= l.length) {
            idx = l.length - 1;
        } else if (idx < 0) {
            idx = 0;
        }
        if (list == null) throw new IllegalStateException("setListView first");

        if (sectionIndexter == null) {
            sectionIndexter = (SectionIndexer) list.getAdapter();
            if (sectionIndexter == null) throw new IllegalStateException("listView must setAdapter first");
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dx = event.getX();
            dy = event.getY();
            yDistance = 0;
            xDistance = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final float curX = event.getX();
            final float curY = event.getY();
            xDistance += Math.abs(curX - dx);
            yDistance += Math.abs(curY - dy);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mDialogText != null) {
                mDialogText.setVisibility(View.VISIBLE);
                mDialogText.setText(String.valueOf(l[idx]));
            }
            this.setBackgroundColor(Color.parseColor("#44000000"));
        } else {
            this.setBackgroundColor(Color.parseColor("#00ffffff"));
            if (mDialogText != null) {
                mDialogText.setVisibility(View.INVISIBLE);
            }
        }
        if (rightSwipMenuLayout != null) {
            rightSwipMenuLayout.setCanLayout(true);
        }
        LogUtils.i("x y " + xDistance + " " + yDistance);
        if (event.getAction() == MotionEvent.ACTION_UP
                || (event.getAction() == MotionEvent.ACTION_MOVE && xDistance < yDistance)) {
            //如果是 “垂直滑动” 或者 “点击” 才可以触发滚动list
            int position = sectionIndexter.getPositionForSection(l[idx]);
            if (position == -1) {
                return true;
            }
            LogUtils.i("side update list");
            list.scrollToPosition(position);
            rightSwipMenuLayout.requestLayout();
        }

        return true;
    }

    public void setTouchEnable(boolean touchAble) {
        mCanTouchAble = touchAble;
    }

    protected void onDraw(Canvas canvas) {
        float widthCenter = getMeasuredWidth() / 2;
        m_nItemHeight = getMeasuredHeight() / l.length;
        for (int i = 0; i < l.length; i++) {
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight / 2 + (i * m_nItemHeight), mPaint);
        }
        super.onDraw(canvas);
    }
}

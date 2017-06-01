package com.example.pan.mydemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;

public class SideBar extends View {
    private char[] l;
    private SectionIndexer sectionIndexter = null;
    private RecyclerView list;
    private TextView mDialogText;
    private int m_nItemHeight = 27;
    private float dx, dy;
    private float xDistance, yDistance;

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
    }


    public void setListIndexData(char[] tl) {
        this.l = tl;
    }

    public void setListView(RecyclerView _list) {
        list = _list;
        sectionIndexter = (SectionIndexer) _list.getAdapter();
    }

    public void setTextView(TextView mDialogText) {
        this.mDialogText = mDialogText;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (true) {
            return false;
        }
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
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final float curX = event.getX();
            final float curY = event.getY();
            xDistance += Math.abs(curX - dx);
            yDistance += Math.abs(curY - dx);
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

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_MOVE) {
            int position = sectionIndexter.getPositionForSection(l[idx]);
            if (position == -1) {
                return true;
            }
            LogUtils.i("x y " + xDistance + " " + yDistance);
            if (xDistance < yDistance) { //如果是垂直滑动才可以触发滚动list
                list.scrollToPosition(position);
            }
        }

        return true;
    }

    public void setTouchEnable(boolean touchAble) {
        mCanTouchAble = touchAble;
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(25);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextAlign(Paint.Align.CENTER);
        float widthCenter = getMeasuredWidth() / 2;
        m_nItemHeight = getMeasuredHeight() / l.length;
        for (int i = 0; i < l.length; i++) {
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight / 2 + (i * m_nItemHeight), paint);
        }
        super.onDraw(canvas);
    }
}

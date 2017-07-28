package com.example.pan.mydemo.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.cus.pan.library.utils.DensityUtils;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.pojo.DataGroupItem;

/**
 * Created by PAN on 2017/7/27.
 */
public class ItemDecoraionDivider extends RecyclerView.ItemDecoration {

    private final int DIVIDER_HEIGHT = 1;
    private final int TEXT_SIZE = 15;
    private final int TITLE_HEIGHT_DEFALUT = 20;
    private final int TITLE_CONTENT_PADDING_LEFT = 15;
    private int mTitleHeight = 40;

    private Context mContext;
    private Paint mPaint, mPaintText;
    private float fontHeight = 0;
    private Paint.FontMetrics fontMetrics;

    private GroupTitleCallBack groupTitleCallBack;

    public ItemDecoraionDivider(Context context, GroupTitleCallBack callBack) {
        this.mContext = context;
        this.groupTitleCallBack = callBack;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mContext.getResources().getColor(R.color.default_blue_royal));

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.WHITE);
        mPaintText.setStrokeWidth(2);
        mPaintText.setTextSize(DensityUtils.dip2px(context, TITLE_HEIGHT_DEFALUT));
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setStyle(Paint.Style.FILL);

        mTitleHeight = DensityUtils.dip2px(context, TITLE_HEIGHT_DEFALUT);
        fontMetrics = mPaintText.getFontMetrics();
        // 计算文字高度
        fontHeight = fontMetrics.bottom - fontMetrics.top;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int itemTop = view.getTop();
            int spaceLineTop;
            int pos = parent.getChildAdapterPosition(view);
            DataGroupItem groupItem = groupTitleCallBack.getGroupItem(pos);
            if (groupItem.isTitle) {
                spaceLineTop = itemTop - mTitleHeight;
            } else {
                spaceLineTop = itemTop - DIVIDER_HEIGHT;
            }
            c.drawRect(0, spaceLineTop, view.getRight(), itemTop, mPaint);
            if (groupItem.isTitle) {
                float fontY = itemTop - (mTitleHeight - fontHeight) / 2 - fontMetrics.bottom;
                c.drawText(groupItem.title, DensityUtils.dip2px(mContext, TITLE_CONTENT_PADDING_LEFT), fontY, mPaintText);
            }
            LogUtils.i(childCount + "onDraw " + i + ":" + pos + ":" + parent.getChildAt(i).getTop() + " " + groupItem.isTitle);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        View topView = parent.getChildAt(0);
        int itemBottom = topView.getBottom();
        int pos = parent.getChildAdapterPosition(topView);
        DataGroupItem groupItem = groupTitleCallBack.getGroupItem(pos);
        DataGroupItem groupItemNext = groupTitleCallBack.getGroupItem(pos + 1);
        int drawTop = 0;
        if (groupItemNext != null && groupItemNext.isTitle && itemBottom <= mTitleHeight) {
            drawTop = itemBottom - mTitleHeight;
        }
        c.drawRect(0, drawTop, topView.getRight(), drawTop + mTitleHeight, mPaint);
        float fontY = drawTop + mTitleHeight - (mTitleHeight - fontHeight) / 2 - fontMetrics.bottom;
        if (!TextUtils.isEmpty(groupItem.title)) {
            c.drawText(groupItem.title, DensityUtils.dip2px(mContext, TITLE_CONTENT_PADDING_LEFT), fontY, mPaintText);
        }
        LogUtils.i("onDrawOver:" + pos + ":" + itemBottom + " " + groupItem.isTitle);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        if (position != 0) {
            outRect.top = DIVIDER_HEIGHT;
        }
        DataGroupItem groupItem = groupTitleCallBack.getGroupItem(position);
        if (groupItem.isTitle) {
            outRect.top = mTitleHeight;
        }
    }

    public interface GroupTitleCallBack {
        DataGroupItem getGroupItem(int pos);
    }
}

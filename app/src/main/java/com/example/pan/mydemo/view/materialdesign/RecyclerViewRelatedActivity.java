package com.example.pan.mydemo.view.materialdesign;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cus.pan.library.utils.DensityUtils;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.pojo.DataGroupItem;
import com.example.pan.mydemo.widget.AutoScrollRecyclerView;
import com.pan.skin.loader.load.SkinManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecyclerViewRelatedActivity extends BaseActivity {

    @BindView(R.id.recycler_view_left)
    AutoScrollRecyclerView mRecyclerViewLeft;
    @BindView(R.id.recycler_view_right)
    AutoScrollRecyclerView mRecyclerViewRight;


    private final int TITLE_COUNT = 100, CONTENT_COUNT = 10;
    private List<DataGroupItem> dataGroupItems = new ArrayList<>();
    private List<DataGroupItem> dataGroupItemsLeft = new ArrayList<>();
    private int checkPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_related);
        setSupportActionBar(R.id.tool_bar);
        initView();
        initData();
    }

    private void initView() {
        mRecyclerViewLeft.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewLeft.setAdapter(new LeftRecyclerAdapter(new OnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(RecyclerViewRelatedActivity.this, "click " + pos + " " + dataGroupItemsLeft.get(pos).text, Toast.LENGTH_SHORT).show();
                //右侧列表滚动到相应位置
                dataGroupItemsLeft.get(checkPos).isChecked = false;
                dataGroupItemsLeft.get(pos).isChecked = true;
                checkPos = pos;
                mRecyclerViewLeft.getAdapter().notifyDataSetChanged();
                moveToPos(pos, false);
            }
        }));
        mRecyclerViewLeft.addItemDecoration(new LeftItemDecoratoin());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                DataGroupItem groupItem = dataGroupItems.get(position);
                return groupItem.isTitle ? 3 : 1;
            }
        });
        mRecyclerViewRight.setLayoutManager(gridLayoutManager);
        mRecyclerViewRight.setAdapter(new RightRecyclerViewAdapter(new OnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(RecyclerViewRelatedActivity.this, "click " + pos + " " + dataGroupItems.get(pos).text, Toast.LENGTH_SHORT).show();
                //左侧列表滚动到相应位置
            }
        }));
        mRecyclerViewRight.addItemDecoration(new RightItemDecoratoin());
        mRecyclerViewRight.addOnScrollListener(new RightRecyclerViewScrollListener());
    }

    private void moveToPos(int pos, boolean isTitle) {
        LogUtils.i("moveToPos " + pos + " " + isTitle);
        if (isTitle) { //选中左侧标题
            int leftPos = pos / (CONTENT_COUNT + 1);
            dataGroupItemsLeft.get(checkPos).isChecked = false;
            dataGroupItemsLeft.get(leftPos).isChecked = true;
            checkPos = leftPos;
            mRecyclerViewLeft.getAdapter().notifyDataSetChanged();
            mRecyclerViewLeft.moveCenterByPosition(checkPos);

        } else {//移动右侧列表
            int position = pos * (CONTENT_COUNT + 1);
            mRecyclerViewRight.scrollToPosition(position);
        }
    }

    private class RightRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            View firstView = recyclerView.getChildAt(0);
            int firstPos = recyclerView.getChildLayoutPosition(firstView);
            int currPos = checkPos * (CONTENT_COUNT + 1);
            if (dataGroupItems.get(firstPos).isTitle && currPos != firstPos) {
                if (!mRecyclerViewRight.isScrollByAnim()) {
                    moveToPos(firstPos, true);
                }
            }
        }
    }

    private void initData() {
        for (int i = 0; i < TITLE_COUNT; i++) {
            DataGroupItem titleItem = new DataGroupItem();
            titleItem.isTitle = true;
            titleItem.title = "条目：" + i;
            titleItem.text = "条目：" + i + " >>";
            titleItem.isChecked = i == 0;
            dataGroupItemsLeft.add(titleItem);
            dataGroupItems.add(titleItem);
            for (int j = 0; j < CONTENT_COUNT; j++) {
                DataGroupItem item = new DataGroupItem();
                item.isTitle = false;
                item.title = "条目：" + i;
                item.text = "内容：" + String.valueOf(i * CONTENT_COUNT + j);
                dataGroupItems.add(item);
            }
        }
//        LogUtils.i(new Gson().toJson(dataGroupItemsLeft));
//        LogUtils.i(new Gson().toJson(dataGroupItems));
    }


    private class LeftRecyclerAdapter extends RecyclerView.Adapter<LeftRecyclerAdapter.MyViewHolder> {

        private OnClickListener mListener;

        public LeftRecyclerAdapter(OnClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RecyclerViewRelatedActivity.this)
                    .inflate(R.layout.item_recycler_view_layout, parent, false);
            return new LeftRecyclerAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DataGroupItem groupItem = dataGroupItemsLeft.get(position);
            holder.textView.setText(groupItem.title);

            if (groupItem.isChecked) {
                holder.textView.setBackgroundColor(SkinManager.getInstance().getColor(R.color.btn_pressed));
            } else {
                holder.textView.setBackground(SkinManager.getInstance().getDrawable(R.drawable.item_selector));
            }

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataGroupItemsLeft.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_tv);
                dynamicAddSkinEnableView(textView, "background", R.drawable.item_selector);
                dynamicAddSkinEnableView(textView, "textColor", R.color.normal_text_color);
            }
        }
    }

    private class LeftItemDecoratoin extends RecyclerView.ItemDecoration {
        private Paint mPaint;

        public LeftItemDecoratoin() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(RecyclerViewRelatedActivity.this.getResources().getColor(R.color.default_blue_royal));
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int size = parent.getChildCount();
            for (int i = 1; i < size; i++) {
                View view = parent.getChildAt(i);
                int top = view.getTop();
                c.drawRect(0, top - 1, view.getRight(), top, mPaint);
            }
            c.drawRect(parent.getRight() - 1, 0, parent.getRight(), parent.getBottom(), mPaint);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildLayoutPosition(view);
            if (pos > 0) {
                outRect.top = 1;
            }
        }
    }


    private class RightRecyclerViewAdapter extends RecyclerView.Adapter<RightRecyclerViewAdapter.MyViewAdapter> {

        private OnClickListener mListener;

        public RightRecyclerViewAdapter(OnClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public MyViewAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(RecyclerViewRelatedActivity.this).inflate(R.layout.item_multi_recycler_view_layout, parent, false);
            return new MyViewAdapter(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewAdapter holder, final int position) {
            DataGroupItem groupItem = dataGroupItems.get(position);
            if (groupItem.isTitle) {
                holder.textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                holder.textView.setPadding(
                        DensityUtils.dip2px(RecyclerViewRelatedActivity.this, 15),
                        DensityUtils.dip2px(RecyclerViewRelatedActivity.this, 15),
                        0,
                        DensityUtils.dip2px(RecyclerViewRelatedActivity.this, 15));
                holder.imageView.setVisibility(View.GONE);
            } else {
                holder.textView.setGravity(Gravity.CENTER);
                holder.textView.setPadding(
                        0,
                        DensityUtils.dip2px(RecyclerViewRelatedActivity.this, 15),
                        0,
                        DensityUtils.dip2px(RecyclerViewRelatedActivity.this, 15));
                holder.imageView.setVisibility(View.VISIBLE);
            }
            holder.textView.setText(groupItem.text);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataGroupItems.size();
        }

        class MyViewAdapter extends RecyclerView.ViewHolder {
            public LinearLayout linearLayout;
            public TextView textView;
            public ImageView imageView;

            public MyViewAdapter(View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.item_multi_layout);
                dynamicAddSkinEnableView(linearLayout, "background", R.drawable.item_selector);

                textView = (TextView) itemView.findViewById(R.id.item_multi_tv);
                dynamicAddSkinEnableView(textView, "textColor", R.color.normal_text_color);

                imageView = (ImageView) itemView.findViewById(R.id.item_multi_img);

            }
        }
    }


    private class RightItemDecoratoin extends RecyclerView.ItemDecoration {
        private Paint mPaint;

        public RightItemDecoratoin() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(RecyclerViewRelatedActivity.this.getResources().getColor(R.color.default_blue_royal));
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            int size = parent.getChildCount();
            for (int i = 0; i < size; i++) {
                View view = parent.getChildAt(i);
                int pos = parent.getChildLayoutPosition(view);
                DataGroupItem groupItem = dataGroupItems.get(pos);
                if (groupItem.isTitle) {
                    int top = view.getTop();
                    int bottom = view.getBottom();
                    c.drawRect(0, top - 1, view.getRight(), top, mPaint);
                    c.drawRect(0, bottom - 1, view.getRight(), bottom, mPaint);
                }
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int pos = parent.getChildLayoutPosition(view);
            DataGroupItem groupItem = dataGroupItems.get(pos);
            if (groupItem.isTitle) {
                outRect.top = 1;
                outRect.bottom = 1;
            } else {
                outRect.top = 0;
                outRect.bottom = 0;
            }
        }
    }

    public interface OnClickListener {
        void onClick(View v, int pos);
    }
}

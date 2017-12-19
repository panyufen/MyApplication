package com.example.pan.mydemo.view.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.adapter.DividerGridItemDecoration;
import com.example.pan.mydemo.pojo.DataGroupItem;
import com.example.pan.mydemo.view.base.BaseLayoutActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

public class DragRecyclerViewActivity extends BaseLayoutActivity {

    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.switch_compat)
    SwitchCompat switchCompat;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ItemTouchHelper itemTouchHelper;
    private ItemTouchHelper.Callback callback;

    private boolean animType = false;
    private AnimRecyclerAdapter adapter;
    private List<DataGroupItem> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swip_recycler_view);

        initView();
    }

    @OnCheckedChanged({R.id.switch_compat})
    public void onViewCheckChanged(CompoundButton v, boolean checked) {
        if (checked) { // greendao
            animType = true;
            tvType.setText("item move");
        } else {      //native
            animType = false;
            tvType.setText("item swap");
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {
        if (lists.size() == 0) {
            for (int i = 0; i < 12; i++) {
                DataGroupItem groupItem = new DataGroupItem();
                groupItem.isTitle = i % 10 == 0;
                groupItem.title = String.valueOf(i / 10);
                groupItem.text = "item " + i;
                lists.add(groupItem);
            }
        }
        adapter = new AnimRecyclerAdapter();
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(adapter);

        callback = new SimpleCallback();
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private class SimpleCallback extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            LogUtils.i("SimpleCallback onMove " + viewHolder.getAdapterPosition() + " " + target.getAdapterPosition());
            int sIndex = viewHolder.getAdapterPosition();
            int tIndex = target.getAdapterPosition();
            if (sIndex < tIndex) {
                for (int i = sIndex; i < tIndex; i++) {
                    Collections.swap(lists, i, i + 1);
                }
            } else {
                for (int j = sIndex; j > tIndex; j--) {
                    Collections.swap(lists, j, j - 1);
                }
            }
            LogUtils.i("List " + new Gson().toJson(lists));
            adapter.notifyItemMoved(sIndex, tIndex);
            clearClear(viewHolder);
            return true;
        }


        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            LogUtils.i("SimpleCallback onSwiped");
        }


        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setScaleX(1.1f);
                viewHolder.itemView.setScaleY(1.1f);
            }
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (!recyclerView.isComputingLayout()) {
                clearClear(viewHolder);
            }
        }

        private void clearClear(RecyclerView.ViewHolder viewHolder) {
            viewHolder.itemView.setScaleX(1.0f);
            viewHolder.itemView.setScaleY(1.0f);
        }
    }


    class AnimRecyclerAdapter extends RecyclerView.Adapter<AnimRecyclerAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(DragRecyclerViewActivity.this)
                    .inflate(R.layout.item_recycler_view_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            DataGroupItem groupItem = lists.get(position);
            holder.tv.setText(groupItem.text);
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DragRecyclerViewActivity.this, "position:" + position, Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.item_tv);
                dynamicAddSkinEnableView(tv, "textColor", R.color.normal_text_color);
                dynamicAddSkinEnableView(tv, "background", R.drawable.item_selector);
            }
        }
    }
}

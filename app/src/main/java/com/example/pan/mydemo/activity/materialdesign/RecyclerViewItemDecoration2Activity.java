package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.adapter.ItemDecoraionDivider;
import com.example.pan.mydemo.pojo.DataGroupItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * RecyclerView 通过ItemDecoration实现StickyHeader效果
 */
public class RecyclerViewItemDecoration2Activity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private HomeAdpater homeAdpater;

    private List<DataGroupItem> lists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_item_decoration2);
        setSupportActionBar(R.id.tool_bar);
        initData();
        initView();
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(homeAdpater);
        //设置itemDecoration
        mRecyclerView.addItemDecoration(new ItemDecoraionDivider(this, new ItemDecoraionDivider.GroupTitleCallBack() {
            @Override
            public DataGroupItem getGroupItem(int pos) {
                if (pos < lists.size()) {
                    return lists.get(pos);
                }
                return null;
            }
        }));
    }


    private void initData() {
        if (lists.size() == 0) {
            for (int i = 0; i < 100; i++) {
                DataGroupItem groupItem = new DataGroupItem();
                groupItem.isTitle = i % 10 == 0;
                groupItem.title = String.valueOf(i / 10);
                groupItem.text = "item " + i;
                lists.add(groupItem);
            }
        }
        homeAdpater = new HomeAdpater();
    }

    class HomeAdpater extends RecyclerView.Adapter<HomeAdpater.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(RecyclerViewItemDecoration2Activity.this)
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
                    Toast.makeText(RecyclerViewItemDecoration2Activity.this, "position:" + position, Toast.LENGTH_LONG).show();
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

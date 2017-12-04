package com.example.pan.mydemo.view.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.adapter.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RecyclerViewItemDecorationActivity extends BaseActivity {

    private RecyclerView recyclerView;

    private Toolbar toolbar;


    private List<String> mDatas;
    private List<Integer> mHeight;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view_item_decoration);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);

        dynamicAddSkinEnableView(toolbar, "background", R.color.colorPrimary);

        initData();

        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.HORIZONTAL_LIST));
        recyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter = new HomeAdapter());

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_switch_car:
                mAdapter.addData(1);
                break;

            case R.id.menu_set_account:
                mAdapter.removeData(1);
                break;


        }
        return super.onOptionsItemSelected(item);
    }

    protected void initData() {
        Random random = new Random();
        mDatas = new ArrayList<String>();
        mHeight = new ArrayList<>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
            int height = random.nextInt(200);
            mHeight.add(height > 50 ? height : 50);
        }
    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RecyclerViewItemDecorationActivity.this).inflate(R.layout.recycler_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mDatas.get(position));
            holder.iv.getLayoutParams().height = mHeight.get(position);
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public void addData(int position) {
            mDatas.add(position, "insert");
            mHeight.add(position, 100);
            notifyItemInserted(position);
        }

        public void removeData(int position) {
            mDatas.remove(position);
            mHeight.remove(position);
            notifyItemRemoved(position);
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iv;
            TextView tv;
            RelativeLayout relativeLayout;

            public MyViewHolder(View view) {
                super(view);
                relativeLayout = (RelativeLayout) view.findViewById(R.id.item_container);
                iv = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
                tv = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            }
        }

    }
}

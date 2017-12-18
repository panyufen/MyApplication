package com.example.pan.mydemo.view.database;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.db.greendao.GreenHelper;
import com.example.pan.mydemo.db.natives.NUserInfoDao;
import com.example.pan.mydemo.pojo.UserInfo;
import com.example.pan.mydemo.view.base.BaseLayoutActivity;
import com.example.pan.mydemo.view.materialdesign.RecyclerViewRelatedActivity;
import com.example.pan.mydemo.widget.AutoScrollRecyclerView;
import com.google.gson.Gson;
import com.qiufeng.greendao.greendao.gen.UserInfoDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DataBaseActivity extends BaseLayoutActivity {

    @BindView(R.id.database_type)
    TextView databaseType;
    @BindView(R.id.batabase_switch)
    SwitchCompat batabaseSwitch;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.button_insert)
    Button buttonInsert;
    @BindView(R.id.button_query)
    Button buttonQuery;
    @BindView(R.id.button_edit)
    Button buttonEdit;
    @BindView(R.id.button_delete)
    Button buttonDelete;
    @BindView(R.id.recycler_view)
    AutoScrollRecyclerView mRecyclerView;

    //private DBHelper dbHelper;
    private NUserInfoDao nUserInfoDao;
    private UserInfoDao userInfoDao;

    private boolean isGreen = false;

    private UserInfo currUserinfo;

    private List<UserInfo> userInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        nUserInfoDao = new NUserInfoDao();
        userInfoDao = GreenHelper.getInstance().getDaoSession().getUserInfoDao();
        initView();


    }


    private void initView() {
        databaseType.setText("SQLite");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new RecyclerAdapter(new RecyclerViewRelatedActivity.OnClickListener() {
            @Override
            public void onClick(View v, int pos) {
                currUserinfo = userInfos.get(pos);
                etName.setText(currUserinfo.getName());
                if (etName.hasFocus()) {
                    etName.setSelection(currUserinfo.getName().length());
                }
                etAge.setText(currUserinfo.getAge());
                if (etAge.hasFocus()) {
                    etAge.setSelection(currUserinfo.getAge().length());
                }
                etMobile.setText(currUserinfo.getMobile());
                if (etMobile.hasFocus()) {
                    etMobile.setSelection(currUserinfo.getMobile().length());
                }
                etAddress.setText(currUserinfo.getAddress());
                if (etAddress.hasFocus()) {
                    etAddress.setSelection(currUserinfo.getAddress().length());
                }
            }
        }));
        mRecyclerView.addItemDecoration(new ItemDecoratoin());
        refreshData();
    }


    @OnCheckedChanged({R.id.batabase_switch})
    public void onViewCheckChanged(CompoundButton v, boolean checked) {
        if (checked) { // greendao
            isGreen = true;
            databaseType.setText("GreenDao");
        } else {      //native
            isGreen = false;
            databaseType.setText("SQLite");
        }
        refreshData();
    }

    @OnClick({R.id.button_insert, R.id.button_query, R.id.button_edit, R.id.button_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_insert:
                currUserinfo = new UserInfo();
                currUserinfo.setName(etName.getText().toString());
                currUserinfo.setAge(etAge.getText().toString());
                currUserinfo.setMobile(etMobile.getText().toString());
                currUserinfo.setAddress(etAddress.getText().toString());
                if (isGreen) {
                    userInfoDao.insert(currUserinfo);
                } else {
                    nUserInfoDao.insert(currUserinfo);
                }
                break;
            case R.id.button_query:
                break;
            case R.id.button_edit:
                if (currUserinfo == null || currUserinfo.getId() == null) {
                    return;
                }
                currUserinfo.setName(etName.getText().toString());
                currUserinfo.setAge(etAge.getText().toString());
                currUserinfo.setMobile(etMobile.getText().toString());
                currUserinfo.setAddress(etAddress.getText().toString());
                LogUtils.i("edit " + new Gson().toJson(currUserinfo));
                if (isGreen) {
                    userInfoDao.update(currUserinfo);
                } else {
                    nUserInfoDao.update(currUserinfo, new String[]{"id"}, new String[]{String.valueOf(currUserinfo.getId())});
                }
                break;
            case R.id.button_delete:
                if (currUserinfo == null) {
                    return;
                }
                if (isGreen) {
                    userInfoDao.delete(currUserinfo);
                } else {
                    nUserInfoDao.delete(currUserinfo, new String[]{"id"}, new String[]{String.valueOf(currUserinfo.getId())});
                }
                break;
        }
        refreshData();
    }

    private void refreshData() {
        userInfos.clear();
        if (isGreen) {
            userInfos.addAll(userInfoDao.loadAll());
        } else {
            List<UserInfo> lists = nUserInfoDao.queryAll();
            userInfos.addAll(lists);
        }
        mRecyclerView.getAdapter().notifyDataSetChanged();
        LogUtils.i("query " + new Gson().toJson(userInfos));
    }


    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

        private RecyclerViewRelatedActivity.OnClickListener mListener;

        public RecyclerAdapter(RecyclerViewRelatedActivity.OnClickListener listener) {
            this.mListener = listener;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DataBaseActivity.this)
                    .inflate(R.layout.userinfo_item_layout, parent, false);
            return new RecyclerAdapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            UserInfo item = userInfos.get(position);
            holder.nameTv.setText(item.getName());
            holder.ageTv.setText(item.getAge());
            holder.mobileTv.setText(item.getMobile());
            holder.addressTv.setText(item.getAddress());
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(v, position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userInfos.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private LinearLayout linearLayout;
            private TextView nameTv;
            private TextView ageTv;
            private TextView mobileTv;
            private TextView addressTv;

            private MyViewHolder(View itemView) {
                super(itemView);
                linearLayout = (LinearLayout) itemView.findViewById(R.id.item_layout);
                nameTv = (TextView) itemView.findViewById(R.id.item_name_tv);
                ageTv = (TextView) itemView.findViewById(R.id.item_age_tv);
                mobileTv = (TextView) itemView.findViewById(R.id.item_mobile_tv);
                addressTv = (TextView) itemView.findViewById(R.id.item_address_tv);
                dynamicAddSkinEnableView(linearLayout, "background", R.drawable.item_selector);
                dynamicAddSkinEnableView(nameTv, "textColor", R.color.normal_text_color);
                dynamicAddSkinEnableView(ageTv, "textColor", R.color.normal_text_color);
                dynamicAddSkinEnableView(mobileTv, "textColor", R.color.normal_text_color);
                dynamicAddSkinEnableView(addressTv, "textColor", R.color.normal_text_color);
            }
        }
    }

    private class ItemDecoratoin extends RecyclerView.ItemDecoration {
        private Paint mPaint;

        public ItemDecoratoin() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setColor(DataBaseActivity.this.getResources().getColor(R.color.default_blue_royal));
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

}

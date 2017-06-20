package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.behavior.BottomSheetHelperBehavior;

public class NestedScrolling2Activity extends BaseActivity {
    private View tabLayout;
    private BottomSheetBehavior<View> mBehavior;
    private NestedScrollView nestedScrollView;
    private BottomSheetHelperBehavior mViewBottomSheetHelperBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_scrolling2);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        tabLayout = findViewById(R.id.tab_layout);
        mBehavior = BottomSheetBehavior.from(tabLayout);

        mViewBottomSheetHelperBehavior = BottomSheetHelperBehavior.from(findViewById(R.id.assistent_tv));
        mViewBottomSheetHelperBehavior.setOnStateChangeListener(new BottomSheetHelperBehavior.BottomSheetViewStateChangeListener() {
            @Override
            public void onChange(boolean state) {
                int state1 = mBehavior.getState();
                LogUtils.i("state " + state);
                if (state) {//向上滑动  tabLayout隐藏
                    if (state1 == BottomSheetBehavior.STATE_EXPANDED) {
                        mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                } else { //向下滑动 tablayout显示
                    if (state1 == BottomSheetBehavior.STATE_HIDDEN) {
                        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
            }
        });
    }

}

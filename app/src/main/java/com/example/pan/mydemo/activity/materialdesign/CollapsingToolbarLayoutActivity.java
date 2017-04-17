package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseNoSkinActivity;


public class CollapsingToolbarLayoutActivity extends BaseNoSkinActivity implements AppBarLayout.OnOffsetChangedListener{

    AppBarLayout mAppBarLayout;

    CollapsingToolbarLayout mCollapsingToolbarLayout;

    Toolbar mToolbar;

    LinearLayout mLayoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar_layout);
        initView();
    }

    private void initView() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("CollapsingToolbarLayout");

        mToolbar = setSupportActionBar(R.id.tool_bar, "CollapsingToolBarLayoutActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mLayoutTitle = (LinearLayout) findViewById(R.id.layout_title);

        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        LogUtils.i("onOffsetChanged " + maxScroll + " " + verticalOffset + " " + percentage);
        changeTitleAlpha(percentage);
    }


    private void changeTitleAlpha(float per) {
        float alpha = 1 - per * 2 > 0 ? 1 - per * 2 : 0;
        mLayoutTitle.setAlpha(alpha);
    }
}

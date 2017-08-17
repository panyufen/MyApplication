package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class MaterialDesignActivity extends BaseActivity {

    ActionBar mActionBar;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_design);
        mToolbar = setSupportActionBar(R.id.tool_bar);
    }

    public void startRecycler(View v) {
        startActivity(RecyclerViewActivity.class);
    }

    public void startSwipBackActivity(View v) {
        startActivity(SwipCloseActivity.class);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void startRightSwipMenuActivity(View v) {
        startActivity(RightSwipMenuActivity.class);
    }

    public void startNavigationView(View v) {
        startActivity(NavigationViewActivity.class);
    }

    public void startCollapsingToolbarLayout(View v) {
        startActivity(CollapsingToolbarLayoutActivity.class);
    }

    public void startConstraintLayout(View v) {
        startActivity(ConstraintLayoutActivity.class);
    }

    public void startFlexBoxLayout(View v) {
        startActivity(FlexBoxLayoutActivity.class);
    }

    public void startSceneTransition(View v) {
        startActivity(SceneTransitionActivity.class);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_fade_and_small);
    }

    public void startSearchView(View v) {
        startActivity(SearchViewActivity.class);
    }

    public void startNestedScrolling(View v) {
        startActivity(NestedScrollingActivity.class);
    }

    public void startNestedScrolling2(View v) {
        startActivity(NestedScrolling2Activity.class);
    }

    public void startNestedScrollView(View v) {
        startActivity(NestedScrollViewActivity.class);
    }
}

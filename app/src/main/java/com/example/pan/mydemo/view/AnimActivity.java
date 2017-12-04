package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.anim.LayoutAnimActivity;
import com.example.pan.mydemo.view.anim.PathAnimActivity;
import com.example.pan.mydemo.view.anim.PropertyAnimActivity;
import com.example.pan.mydemo.view.anim.SvgPathAnimActivity;
import com.example.pan.mydemo.view.anim.ViewPagerActivity;
import com.example.pan.mydemo.view.anim.XMLPropertyAnimActivity;
import com.example.pan.mydemo.view.base.BaseActivity;

public class AnimActivity extends BaseActivity {
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);
        mToolBar = setSupportActionBar(R.id.tool_bar);
    }

    public void startPathAnim(View view) {
        startActivity(PathAnimActivity.class);
    }

    public void startSvgPathAnim(View v) {
        startActivity(SvgPathAnimActivity.class);
    }

    public void startViewPager(View v) {
        startActivity(ViewPagerActivity.class);
    }

    public void startLayoutAnim(View v) {
        startActivity(LayoutAnimActivity.class);
    }

    public void startPropertyAnim(View v) {
        startActivity(PropertyAnimActivity.class);
    }

    public void startXMLPropertyAnim(View v) {
        startActivity(XMLPropertyAnimActivity.class);
    }

}

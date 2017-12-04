package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.transformer.ZoomOutPageTransformer;

import java.util.ArrayList;

public class BlurringActivity extends BaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayoutView;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blurring);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Blurring Activity");
        setSupportActionBar(toolbar);

        tabLayoutView = (TabLayout)findViewById(R.id.tab_layout_view);
        pagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        tabLayoutView.setupWithViewPager(viewPager);
        tabLayoutView.setTabTextColors(getResources().getColor(R.color.normal_text_color), getResources().getColor(R.color.btn_blue_released));
        tabLayoutView.setTabMode(TabLayout.MODE_FIXED);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public class CustomPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

        public CustomPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(Fragment.instantiate(BlurringActivity.this, RSBlurFragment.class.getName()));
            fragments.add(Fragment.instantiate(BlurringActivity.this, FastBlurFragment.class.getName()));
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).toString();
        }
    }
}

package com.example.pan.mydemo.view.materialdesign;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.pan.skin.loader.load.SkinManager;

public class NavigationViewActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TextView swipValueTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int color = SkinManager.getInstance().getColorPrimaryDark();
        color = color == -1 ? getResources().getColor(R.color.colorPrimaryDark) : color;
        setTranslucaentStatusBarForDrawer(color);
        setContentView(R.layout.activity_navigation_view);
        setSupportActionBar(R.id.tool_bar, "Content Title");
        init();
    }

    private void init() {
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        //用代码设置,也可以用XML属性设置
        ColorStateList csl = getResources().getColorStateList(R.color.menu_item_selector);
        mNavigationView.setItemTextColor(csl);

        swipValueTv = (TextView) findViewById(R.id.swip_value_tv);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                swipValueTv.setText(String.valueOf(slideOffset));
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Toast.makeText(NavigationViewActivity.this, "Opened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Toast.makeText(NavigationViewActivity.this, "Closed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_item_home:
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_item_blog:
                Toast.makeText(this, "blog", Toast.LENGTH_SHORT).show();
                break;

            case R.id.navigation_item_about:
                Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }
}

package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cus.pan.library.utils.DensityUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.pan.skin.loader.load.SkinManager;


public class NestedScrollingActivity extends BaseActivity {

    LinearLayout nestedScrollviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling);
        setSupportActionBar(R.id.tool_bar);
        initView();
    }

    private void initView() {
        nestedScrollviewLayout = (LinearLayout) findViewById(R.id.nested_scrollview_layout);
        nestedScrollviewLayout.removeAllViews();
        for (int i = 1; i <= 30; i++) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtils.dip2px(this, 50));
            textView.setPadding(20, 20, 20, 20);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(SkinManager.getInstance().getDrawable(R.drawable.item_selector));
            textView.setTextColor(SkinManager.getInstance().getColor(R.color.normal_text_color));
            textView.setText(String.valueOf("item " + i));
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NestedScrollingActivity.this, "" + finalI, Toast.LENGTH_SHORT).show();
                }
            });
            nestedScrollviewLayout.addView(textView);
        }
    }
}

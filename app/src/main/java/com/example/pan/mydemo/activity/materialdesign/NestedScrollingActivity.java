package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pan.mydemo.R;

public class NestedScrollingActivity extends AppCompatActivity {

    LinearLayout nestedScrollviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_scrolling);

        initView();
    }

    private void initView() {
        nestedScrollviewLayout = (LinearLayout) findViewById(R.id.nested_scrollview_layout);
        nestedScrollviewLayout.removeAllViews();
        for (int i = 1; i <= 20; i++) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
            textView.setPadding(20, 20, 20, 20);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(String.valueOf("item " + i));
            nestedScrollviewLayout.addView(textView);
        }
    }
}

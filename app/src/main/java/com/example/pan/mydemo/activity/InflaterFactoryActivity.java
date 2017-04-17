package com.example.pan.mydemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class InflaterFactoryActivity extends BaseActivity {
    private Toolbar mToolBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final StringBuffer stringBuffer = new StringBuffer();
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory()
        {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs)
            {
                LogUtils.e( "name = " + name);
                stringBuffer.append("name = "+name+" \n");
                int n = attrs.getAttributeCount();
                for (int i = 0; i < n; i++)
                {
                    LogUtils.i(attrs.getAttributeName(i) + " , " + attrs.getAttributeValue(i));
                    stringBuffer.append(attrs.getAttributeName(i) + " , " + attrs.getAttributeValue(i)+"\n");
                }

                AppCompatDelegate appCompatDelegate = getDelegate();
                View view = appCompatDelegate.createView(parent,name,context,attrs);
                return view;
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inflater_factory);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        textView = (TextView) findViewById(R.id.inflater_factory_tv);
        textView.setText(stringBuffer.toString());
    }

}

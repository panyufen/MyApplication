package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codbking.calendar.CaledarAdapter;
import com.codbking.calendar.CalendarBean;
import com.codbking.calendar.CalendarDateView;
import com.codbking.calendar.CalendarUtil;
import com.codbking.calendar.CalendarView;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

import java.util.Date;

import butterknife.BindView;

public class CalendarActivity extends BaseActivity {

    @BindView(R.id.calendarDateView)
    CalendarDateView mCalendarDateView;
    @BindView(R.id.list)
    ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        setSupportActionBar(R.id.tool_bar);
        initView();
        initList();
    }

    private void initList() {
        mList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 100;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(CalendarActivity.this).inflate(android.R.layout.simple_list_item_1, null);
                }

                TextView textView = (TextView) convertView;
                textView.setText("position:" + position);

                return convertView;
            }
        });
    }

    private void initView() {
        mCalendarDateView.setAdapter(new CaledarAdapter() {
            @Override
            public View getView(View convertView, ViewGroup parentView, CalendarBean bean) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.calendar_item_view_layout, null);
                }

                TextView chinaText = (TextView) convertView.findViewById(R.id.chinaText);
                TextView text = (TextView) convertView.findViewById(R.id.text);

                text.setText("" + bean.day);
                if (bean.mothFlag != 0) {
                    text.setTextColor(getResources().getColor(R.color.assist_text_color));
                    chinaText.setTextColor(getResources().getColor(R.color.assist_text_color));
                } else {
                    text.setTextColor(getResources().getColor(R.color.normal_text_color));
                    chinaText.setTextColor(getResources().getColor(R.color.normal_text_color));
                }
                chinaText.setText(bean.chinaDay);
                return convertView;
            }
        });

        mCalendarDateView.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, CalendarBean bean) {
//                mTitle.setText(bean.year + "/" + bean.moth + "/" + bean.day);
            }
        });

        int[] data = CalendarUtil.getYMD(new Date());
//        mTitle.setText(data[0] + "/" + data[1] + "/" + data[2]);
    }
}

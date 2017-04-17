package com.example.pan.mydemo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PicassoActivity extends BaseActivity {

    private String BASE_URL = "http://img1.3lian.com/img2011/w1/106/85/";
    private Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);
        mToolBar = setSupportActionBar(R.id.tool_bar);
        ArrayList<Dish> dishList = new ArrayList<>();

        dishList.add(new Dish(BASE_URL + "42.jpg", "水煮鱼片"));
        dishList.add(new Dish(BASE_URL + "34.jpg", "小炒肉"));
        dishList.add(new Dish(BASE_URL + "37.jpg", "清炒时蔬"));
        dishList.add(new Dish(BASE_URL + "11.jpg", "金牌烤鸭"));
        dishList.add(new Dish(BASE_URL + "12.jpg", "粉丝肉煲"));

        ListView mListView = (ListView) this.findViewById(R.id.list_view);
        MainListViewAdapter adapter = new MainListViewAdapter(dishList);
        mListView.setAdapter(adapter);

    }

    // ListView适配器
    private class MainListViewAdapter extends BaseAdapter {

        private ArrayList<Dish> dishList;

        public MainListViewAdapter(ArrayList<Dish> list) {
            this.dishList = list;
        }

        @Override
        public int getCount() {
            return dishList.size();
        }

        @Override
        public Object getItem(int position) {
            return dishList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewItemHolder item = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PicassoActivity.this).inflate(
                        R.layout.recycler_item, null);
                item = new ListViewItemHolder();
                item.img_iv = (ImageView) convertView
                        .findViewById(R.id.id_index_gallery_item_image);
                item.name = (TextView)convertView.findViewById(R.id.id_index_gallery_item_text);
                convertView.setTag(item);
            } else {
                item = (ListViewItemHolder) convertView.getTag();
            }

            Dish dish = dishList.get(position);

            //这里就是异步加载网络图片的地方
            Picasso.with(PicassoActivity.this)
                    .load(dish.getImgUrl())
                    .resize(400,400)//pixel
                    .centerInside()
                    .placeholder(R.mipmap.ic_launcher)
                    .into(item.img_iv);
            item.name.setText(dish.getName());

            return convertView;
        }

    }

    // ListView的Item组件类
    private class ListViewItemHolder {
        ImageView img_iv;
        TextView name;
    }

    //表示菜类（经过烹调的蔬菜、蛋品、肉类等）
    public class Dish {

        private String imgUrl; // 图片地址

        private String name;

        public Dish(String imgUrl,String name) {
            this.imgUrl = imgUrl;
            this.name = name;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getName(){
            return this.name;
        }

        public void setName( String name ){
            this.name = name;
        }

    }

}

package com.example.pan.mydemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pan.mydemo.R;

import java.util.List;

/**
 * Created by roy on 2015/12/28.
 */
public class PTButtonList {


    public static View create(Context context, final List<ButtonPack> packs, final OnClickButtonListener listener) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = null;
        if (packs.size() > 6) {
            root = inflater.inflate(R.layout.dialog_button_list_300, null);
        } else {
            root = inflater.inflate(R.layout.dialog_button_list, null);
        }
        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCancel();
            }
        });
        if (root.getHeight() > context.getResources().getDimension(R.dimen.max_list_height)) {
        }

        ListView listView = (ListView) root.findViewById(R.id.listView);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return packs.size();
            }

            @Override
            public Object getItem(int i) {
                return packs.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                    view = inflater.inflate(R.layout.widget_button_list_element, null);
                }
                ButtonPack buttonPack = packs.get(i);
                TextView text = (TextView) view.findViewById(R.id.tv_text);
                if (buttonPack.text != null) { //文字
                    text.setText(buttonPack.text);
                } else {
                    text.setVisibility(View.GONE);
                }
                if (buttonPack.icon != -1) {//图标
                    ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
                    icon.setImageResource(buttonPack.icon);
                    icon.setVisibility(View.VISIBLE);
                }

                listener.onShow(this, i);
                return view;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                ButtonPack buttonPack = packs.get(index);
                listener.onClick(buttonPack, index);
            }
        });
        return root;
    }


    public static abstract class OnClickButtonListener {
        public abstract void onClick(ButtonPack buttonPack, int index);

        public abstract void onCancel();

        public void onShow(BaseAdapter adapter, int index) {

        }
    }

    //按钮数据包
    public static final class ButtonPack {
        public int icon = -1;
        public String text;
        public Object source;

        public ButtonPack(String text) {
            this.text = text;
        }

        public ButtonPack(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }

        public ButtonPack setSource(Object source) {
            this.source = source;
            return this;
        }

        public <T> T getSource(Class<T> clazz) {
            return (T) source;
        }

        public Object getSource() {
            return source;
        }
    }
}

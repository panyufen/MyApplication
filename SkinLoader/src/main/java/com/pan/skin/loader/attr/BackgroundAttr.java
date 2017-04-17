package com.pan.skin.loader.attr;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.View;

import com.pan.skin.loader.load.SkinManager;
import com.pan.skin.loader.util.L;


/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:46
 */
public class BackgroundAttr extends SkinAttr {

    @Override
    public void apply(View view) {

        if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {

            int color = SkinManager.getInstance().getColor(attrValueRefId);
            if (view instanceof CardView) {//这里对CardView特殊处理下
                CardView cardView = (CardView) view;
                //给CardView设置背景色应该使用cardBackgroundColor，直接使用background就会没有圆角效果
                cardView.setCardBackgroundColor(color);
            } else {
                view.setBackgroundColor(color);
            }
            L.i("applyAttr", "apply as color");
        } else if (RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
            Drawable bg = SkinManager.getInstance().getDrawable(attrValueRefId);
            // view.setBackground(bg);
            view.setBackgroundDrawable(bg);
            L.i("applyAttr", "apply as drawable");
        }
    }
}

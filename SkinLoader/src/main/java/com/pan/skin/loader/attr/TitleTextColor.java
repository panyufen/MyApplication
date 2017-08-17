package com.pan.skin.loader.attr;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.pan.skin.loader.load.SkinManager;
import com.pan.skin.loader.util.L;

/**
 * Created by PAN on 2017/8/16.
 */

public class TitleTextColor extends SkinAttr {

    /**
     * Use to apply view with new TypedValue
     *
     * @param view
     */
    @Override
    public void apply(View view) {
        if (view instanceof android.support.v7.widget.Toolbar) {
            Toolbar toolbar = (Toolbar) view;
            if (RES_TYPE_NAME_COLOR.equals(attrValueTypeName)) {
                int color = SkinManager.getInstance().getColor(attrValueRefId);
                L.i("applyAttr", "TextColorAttr");
                toolbar.setTitleTextColor(color);
            }
        }
    }
}

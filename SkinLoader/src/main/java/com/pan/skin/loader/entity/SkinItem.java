package com.pan.skin.loader.entity;

import android.view.View;

import com.pan.skin.loader.attr.SkinAttr;
import com.pan.skin.loader.util.ListUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:9:21
 * <p/>
 * 用来存储那些有皮肤更改需求的View及其对应的属性
 */
public class SkinItem {

    public View view;

    public List<SkinAttr> attrs;

    public SkinItem() {
        attrs = new ArrayList<SkinAttr>();
    }

    public void apply() {
        if (ListUtils.isEmpty(attrs)) {
            return;
        }
        for (SkinAttr at : attrs) {
            at.apply(view);
        }
    }

    public void clean() {
        if (ListUtils.isEmpty(attrs)) {
            return;
        }
        for (SkinAttr at : attrs) {
            at = null;
        }
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }
}

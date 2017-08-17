package com.pan.skin.loader.config;

import android.content.Context;

import com.pan.skin.loader.util.PreferencesUtils;

/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:29
 */
public class SkinConfig {
    public static final String NAMESPACE = "http://schemas.android.com/android/skin";
    public static final String SKIN_FOLER_NAME = "skin";
    public static final String PREF_CUSTOM_SKIN_PATH = "pan_skin_custom_path";
    public static final String DEFALT_SKIN = "pan_skin_default";//默认皮肤
    public static final String ATTR_SKIN_ENABLE = "enable";
    public static final String SKIN_STYLE_ISLIGHT = "skin_style_islight";


    /**
     * get path of last skin package path
     *
     * @param context
     * @return path of skin package
     */
    public static String getCustomSkinPath(Context context) {
        return PreferencesUtils.getString(context, PREF_CUSTOM_SKIN_PATH, DEFALT_SKIN);
    }

    public static void saveSkinPath(Context context, String path) {
        PreferencesUtils.putString(context, PREF_CUSTOM_SKIN_PATH, path);
    }

    public static void setIsLightSkin(Context context,boolean isLight) {
        PreferencesUtils.putBoolean(context, SKIN_STYLE_ISLIGHT, isLight);
    }

    public static boolean isLightSkin(Context context) {
        return PreferencesUtils.getBoolean(context, SKIN_STYLE_ISLIGHT, false);
    }


    public static boolean isDefaultSkin(Context context) {
        return DEFALT_SKIN.equals(getCustomSkinPath(context));
    }


}

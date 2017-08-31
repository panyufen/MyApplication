package com.androidyuan.lib.screenshot;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PAN on 2017/8/22.
 */

public class ShotPreferencesUtils {

    private static SharedPreferences share;

    private static String SHARE_TAG = "SHARE_TAG_SHOT";
    private static String SHARE_SHOT_FILE_NAME = "SHARE_SHOT_FILE_NAME";

    public ShotPreferencesUtils(Context context) {
        share = context.getSharedPreferences(SHARE_TAG, Context.MODE_PRIVATE);
    }

    public static void setCurrentPicName(String fileName) {
        share.edit().putString(SHARE_SHOT_FILE_NAME, fileName).apply();
    }

    public static String getCurrentPicName() {
        if (share == null) {
            return "0";
        }
        return share.getString(SHARE_SHOT_FILE_NAME, "0");
    }
}

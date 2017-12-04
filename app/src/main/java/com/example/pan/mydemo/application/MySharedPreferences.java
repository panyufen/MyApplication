package com.example.pan.mydemo.application;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pan on 2016/5/24.
 */
public class MySharedPreferences {

    private SharedPreferences share;

    private static MySharedPreferences sharedPreference;

    private final String SHARE_TAG = "SHARE_TAG";

    private final String LOGIN_FIRST = "LOGIN_FIRST";


    private MySharedPreferences() {
        share = MyApplication.app.getSharedPreferences(SHARE_TAG, Context.MODE_PRIVATE);
    }

    public static MySharedPreferences getInstance() {
        if (sharedPreference == null) {
            synchronized (MySharedPreferences.class) {
                if (sharedPreference == null) {
                    sharedPreference = new MySharedPreferences();
                }
            }
        }
        return sharedPreference;
    }

    public void setLoginFirst() {
        share.edit().putBoolean(LOGIN_FIRST, true).apply();
    }

    public boolean getLoginFirst() {
        return share.getBoolean(LOGIN_FIRST, false);
    }

}

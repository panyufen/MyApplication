package com.cus.pan.library.utils;

import android.util.Log;

/**
 * 日志工具类
 */
public class LogUtils {
    private static boolean isShowLog = true;
    private static boolean isDebug  = true;
    private static final String TAG = "PT_TTS";
    public static void setLogFlag(boolean isOpen){
        isShowLog = isOpen;
    }
    public static void setDebugFlag(boolean isOpen){
        isDebug = isOpen;
    }

    public static void i(String s) {
        if(isShowLog){
            Log.i(TAG,s);
        }
    }

    public static void i(String s, String s1) {
        if(isShowLog){
            Log.i(TAG,s+" "+s1);
        }
    }

    public static void w(String s) {
        if(isShowLog){
            Log.w(TAG,s);
        }
    }

    public static void w(String s, String s1) {
        if(isShowLog){
            Log.w(TAG,s+" "+s1);
        }
    }

    public static void e(String s, Exception e) {
        if(isShowLog){
            if(isDebug) {
                Log.e(TAG, s, e);
            }else{
                Log.e(TAG,s+" "+e.toString());
            }
        }
    }

    public static void e(String s) {
        if(isShowLog){
            Log.e(TAG,s);
        }
    }


    public static void d(String s, String s1) {
        if(isShowLog && isDebug){
            Log.d(TAG,s+" "+s1);
        }
    }

    public static void d(String s) {
        if(isShowLog && isDebug){
            Log.d(TAG,s);
        }
    }
}

package com.example.pan.mydemo.react;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by PAN on 2016/8/16.
 */
public class LogModule extends ReactContextBaseJavaModule {


    public LogModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "PTLog";
    }

    @ReactMethod
    public void i(String s){
        Log.i("PT ",s);
    }
    @ReactMethod
    public void v(String s){
        Log.v("PT ",s);
    }
    @ReactMethod
    public void w(String s){
        Log.w("PT ",s);
    }
    @ReactMethod
    public void e(String s){
        Log.e("PT ",s);
    }



}

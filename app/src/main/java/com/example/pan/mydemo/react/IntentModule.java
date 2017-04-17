package com.example.pan.mydemo.react;

import android.app.Activity;
import android.content.Intent;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by PAN on 2016/8/19.
 */
public class IntentModule extends ReactContextBaseJavaModule {


    public IntentModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from javascript.
     */
    @Override
    public String getName() {
        return "ActivityAndroid";
    }

    @ReactMethod
    public void startActivity(String activityName, Callback callback) throws ClassNotFoundException {
        Class aimActivity = Class.forName(activityName);
        Activity activity = getCurrentActivity();
        if( activity != null ) {
            Intent intent = new Intent(activity, aimActivity);
            activity.startActivityForResult(intent,12345);
        }else{
            callback.invoke("启动页面失败");
        }
    }

}

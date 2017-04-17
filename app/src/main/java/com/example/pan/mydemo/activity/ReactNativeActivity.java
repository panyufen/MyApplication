package com.example.pan.mydemo.activity;

import android.os.Bundle;

import com.facebook.react.ReactActivity;

public class ReactNativeActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     * e.g. "MoviesApp"
     */
    @Override
    protected String getMainComponentName() {
        return "ReNaProject";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}

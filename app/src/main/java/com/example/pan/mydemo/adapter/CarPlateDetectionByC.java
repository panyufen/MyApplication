package com.example.pan.mydemo.adapter;

import org.opencv.core.Mat;

/**
 * Created by PAN on 2016/11/26.
 */

public class CarPlateDetectionByC implements DetectionFilter {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("opencv_java3");
    }

    public CarPlateDetectionByC() {

    }

    @Override
    public void apply(Mat rgba, Mat gray, Mat dst) {
        rgba.copyTo(dst);
        findAndDetectionCarPlate(rgba.nativeObj,gray.nativeObj,dst.nativeObj);
    }


    private native String stringFromJNI();

    private native int findAndDetectionCarPlate( long rbga,long gray,long dst );

}

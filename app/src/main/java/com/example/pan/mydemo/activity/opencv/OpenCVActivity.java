package com.example.pan.mydemo.activity.opencv;

import android.os.Bundle;
import android.view.View;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

public class OpenCVActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cv);
    }

    public void startOpenCVGray(View v) {
        startActivity(OpenCVGrayActivity.class);
    }

    public void startOpenCVHSVDetection(View v) {
        startActivity(HSVDetectionActivity.class);
    }

    public void startOpenCVThreshold(View v) {
        startActivity(OpenCVThresholdActivity.class);
    }

    public void startOpenFeature(View v) {
        startActivity(OpenCVFeatureActivity.class);
    }


    public void startOpenCVFaceDetection(View v) {
        startActivity(OpencvFaceDetectionActivity.class);
    }

    public void startOpenCVCarCard(View v) {
        startActivity(OpenCVCarPlateActivity.class);
    }

    public void startOpenCVCarCardByC(View v) {
        startActivity(OpenCVCarPlateDetectionByCActivity.class);
    }

    public void startOpenCVCarDetec(View v) {
        startActivity(OpenCVCarDetectionActivity.class);
    }

    public void startOpenCVMadkeDelaunayFace(View v) {
        startActivity(OpenCVMakeDelaunayForFaceActivity.class);
    }


}

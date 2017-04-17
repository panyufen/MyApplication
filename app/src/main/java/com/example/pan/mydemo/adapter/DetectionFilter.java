package com.example.pan.mydemo.adapter;

import org.opencv.core.Mat;

/**
 * Created by PAN on 2016/11/9.
 */

public interface DetectionFilter {

    public void apply(Mat rgba,Mat gray, Mat dst);
}

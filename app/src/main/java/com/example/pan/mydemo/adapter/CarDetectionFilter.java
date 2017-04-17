package com.example.pan.mydemo.adapter;

import android.content.Context;

import com.cus.pan.library.utils.FileUtils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

/**
 * Created by PAN on 2016/11/14.
 */

public class CarDetectionFilter implements DetectionFilter{

    private File mCascadeFile;
    private CascadeClassifier carsDetector;

    public CarDetectionFilter(Context context) {
        String carDetectionFile = "cars3.xml";
        FileUtils.copyFolderFromAssets(context,false,"harr",FileUtils.getCacheDir(context).getAbsolutePath() + "/harr");
        mCascadeFile = new File(FileUtils.getCacheDir(context).getAbsolutePath() + "/harr/", carDetectionFile);
        carsDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
    }

    @Override
    public void apply(Mat rgba, Mat gray, Mat dst) {
        gray.copyTo(dst);
        MatOfRect carsDetections = new MatOfRect();

//        Imgproc.threshold(gray,dst,0,255,Imgproc.THRESH_OTSU);


        carsDetector.detectMultiScale(gray, carsDetections);
        for (Rect rect : carsDetections.toArray()) {
            Imgproc.rectangle(dst, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 3);
        }
    }
}

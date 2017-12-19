package com.example.pan.mydemo.view.opencv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.adapter.CarPlateDetectionByC;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class OpenCVCarPlateDetectionByCActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private Mat mRgba;
    private Mat mGray;
    private Mat mTempRgba;
    private Mat mTempGray;

    private CameraBridgeViewBase mOpenCvCameraView;

    private CarPlateDetectionByC mCarPlateDetectionFilter;

    private boolean isHighDefinition = false;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("camera", "OpenCV loaded successfully");

                    // Load native library after(!) OpenCV initialization

                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_open_cvcar_plate_detection_by_c);
        initView();
    }

    private void initView() {
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.feature_camera_view);
        mOpenCvCameraView.enableFpsMeter();
        mOpenCvCameraView.setMaxFrameSize(1280, 720);
        mOpenCvCameraView.setCvCameraViewListener(this);

        mOpenCvCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOpenCvCameraView.disableView();
                mOpenCvCameraView.setMaxFrameSize(800, 480);
                mOpenCvCameraView.enableView();
            }
        });

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.i("camera", "width height " + width + " " + height);
        if (mCarPlateDetectionFilter == null) {
            mCarPlateDetectionFilter = new CarPlateDetectionByC();
        }
        mRgba = new Mat();
        mGray = new Mat();

        mTempGray = new Mat();
        try {
            mTempRgba = Utils.loadResource(this, R.drawable.carcarda);
            Imgproc.cvtColor(mTempRgba, mTempGray, Imgproc.COLOR_RGB2GRAY);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
//        mTempRgba.copyTo(mRgba);
//        mTempGray.copyTo(mGray);

        Mat result = new Mat();
        if (mCarPlateDetectionFilter != null) {
            mCarPlateDetectionFilter.apply(mRgba, mGray, result);
            return result;
        }

        return mRgba;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOpenCvCameraView.disableView();
    }

}

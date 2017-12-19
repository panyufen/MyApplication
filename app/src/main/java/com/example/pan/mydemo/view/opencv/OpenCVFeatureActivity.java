package com.example.pan.mydemo.view.opencv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.adapter.ImageDetectionFilter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenCVFeatureActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private Button mFSelect, mFClear;

    private SeekBar mFSeeker;
    private TextView mFSeekValue;


    private Mat mRgba;
    private Mat mGray;
    private Mat tartFrame;

    private CameraBridgeViewBase mOpenCvCameraView;

    private Point mScreenCenter, mOrignCenter, mFeatureCenter;

    private boolean isProcess = false;
    private List<KeyPoint> mKeyPointRecord = new ArrayList<>();
    private int keyPointObjId;


    private List<Point> mPointFilter = new ArrayList<>();

    private int filterEdge = 5;

    private ImageDetectionFilter imageDetectionFilter;


    private boolean isSelected = false;

    private int tartgetWidth = 150;

    private int mScreenWidth, mScreenHeight;


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
        setContentView(R.layout.activity_open_cvfeature);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mScreenCenter = new Point(mScreenWidth / 2, mScreenHeight / 2);
        initView();
    }

    private void initView() {

        mFSelect = (Button) findViewById(R.id.feature_select);
        mFClear = (Button) findViewById(R.id.feature_clear);

        mFSelect.setOnClickListener(this);
        mFClear.setOnClickListener(this);


        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.feature_camera_view);
        mOpenCvCameraView.setMaxFrameSize(1280, 720);
        mOpenCvCameraView.enableFpsMeter();

        mOpenCvCameraView.setCvCameraViewListener(this);

        mFSeekValue = (TextView) findViewById(R.id.feature_seek_value);

        mFSeeker = (SeekBar) findViewById(R.id.feature_seek);
        mFSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                filterEdge = progress;
                mFSeekValue.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        Log.i("camera", "width height " + width + " " + height);

        mOrignCenter = new Point(width / 2, height / 2);

        try {
            imageDetectionFilter = new ImageDetectionFilter(this, R.drawable.sample2);
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
        if (tartgetWidth > (int) (mGray.width() * 0.2)) {
            tartgetWidth = (int) (mGray.width() * 0.2);
        }
        Mat result = new Mat();

//        int width = mGray.cols(), height = mGray.rows();
//        float scope = (float) height / (float) width;
//
//        Mat resultRgba = new Mat(new Size(width, width / scope), mRgba.type());
//        Mat resultGray = new Mat(new Size(width, width / scope), mGray.type());
//
//        Rect rect = new Rect(
//                0,
//                (int) (width / scope - height) / 2,
//                width, height);
////        Mat targetG = resultGray.submat(rect);
////        mGray.copyTo(targetG);
//        Mat targetR = resultRgba.submat(rect);
//        mRgba.copyTo(targetR);
        LogUtils.i("rect: " + mRgba.size().toString());
////        Mat rotMat = Imgproc.getRotationMatrix2D(mScreenCenter, 270d, 1 / scope);
////        Imgproc.warpAffine(resultRgba, resultRgba, rotMat, new Size(mScreenWidth, mScreenHeight));
////        Imgproc.warpAffine(resultRgba, resultGray, rotMat, new Size(mScreenWidth, mScreenHeight));


        if (!isProcess) {
            if (imageDetectionFilter != null) {
                imageDetectionFilter.apply(mRgba, mGray, result);
            }
        } else {
            return null;
        }

        if (!isSelected) {
            //屏幕中心选取区域
            Imgproc.rectangle(result, new Point(mOrignCenter.x - tartgetWidth, mOrignCenter.y - tartgetWidth), new Point(mOrignCenter.x + tartgetWidth, mOrignCenter.y + tartgetWidth), new Scalar(255, 0, 0), 2);
        }
        return result;

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feature_select:
                isProcess = true;

                tartFrame = mRgba.clone();
                int rows = tartFrame.rows();
                int cols = tartFrame.cols();
                Mat targetMat = tartFrame.submat(rows / 2 - tartgetWidth, rows / 2 + tartgetWidth, cols / 2 - tartgetWidth, cols / 2 + tartgetWidth);
                imageDetectionFilter.stop();
                try {
                    imageDetectionFilter = new ImageDetectionFilter(targetMat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i(" width height ", "start " + rows + " " + cols + " " + targetMat.cols() + " " + targetMat.rows() + " " + targetMat.size());
                isSelected = true;
                isProcess = false;
                break;

            case R.id.feature_clear:
                isProcess = true;
                imageDetectionFilter.stop();
                try {
                    imageDetectionFilter = new ImageDetectionFilter(this, R.drawable.sample2);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                isSelected = false;
                isProcess = false;
                break;
        }
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

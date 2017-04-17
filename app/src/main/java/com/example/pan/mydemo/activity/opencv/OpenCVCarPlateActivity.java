package com.example.pan.mydemo.activity.opencv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.adapter.CarPlateDetectionFilter;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class OpenCVCarPlateActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private Mat mRgba;
    private Mat mGray;
    private Mat mTempRgba;
    private Mat mTempGray;

    private SeekBar mFSeeker;
    private TextView mFSeekValue;

    private CameraBridgeViewBase mOpenCvCameraView;

    private CarPlateDetectionFilter mCarPlateDetectionFilter;

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
        setContentView(R.layout.activity_open_cvcar_card);
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

//        mOpenCvCameraView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int cols = mRgba.cols();
//                int rows = mRgba.rows();
//                int x = (int) event.getX();
//                int y = (int) event.getY();
//                if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;
//
//                Rect touchedRect = new Rect();
//
//                touchedRect.x = (x > 4) ? x - 4 : 0;
//                touchedRect.y = (y > 4) ? y - 4 : 0;
//                touchedRect.width = (x + 4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
//                touchedRect.height = (y + 4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;
//
//                Mat touchedRegionRgba = mRgba.submat(touchedRect);
//
//                Mat touchedRegionHsv = new Mat();
//                Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);
//
//                // Calculate average color of touched region
//                Scalar mBlobColorHsv = Core.sumElems(touchedRegionHsv);
//                int pointCount = touchedRect.width * touchedRect.height;
//                for (int i = 0; i < mBlobColorHsv.val.length; i++) {
//                    mBlobColorHsv.val[i] /= pointCount;
//                }
//                Log.i("Touched", "Touched hsv color: (" + mBlobColorHsv.val[0] + ", " + mBlobColorHsv.val[1] + ", " + mBlobColorHsv.val[2] + ", " + mBlobColorHsv.val[3] + ")");
//                return false;
//            }
//        });


        mFSeekValue = (TextView) findViewById(R.id.feature_seek_value);

        mFSeeker = (SeekBar) findViewById(R.id.feature_seek);
        mFSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mFSeekValue.setText("" + progress);
                mCarPlateDetectionFilter.setLowS(progress);
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
        try {
            if (mCarPlateDetectionFilter == null) {
                mCarPlateDetectionFilter = new CarPlateDetectionFilter(this, R.mipmap.digits);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRgba = new Mat();
        mGray = new Mat();

        mTempGray = new Mat();
        try {
            mTempRgba = Utils.loadResource(this, R.mipmap.carcarda);
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

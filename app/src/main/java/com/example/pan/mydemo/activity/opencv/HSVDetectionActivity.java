package com.example.pan.mydemo.activity.opencv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.view.ZoomImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.androidyuan.lib.screenshot.Shotter.SCREEN_SHOT_PATH_NAME;

public class HSVDetectionActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.feature_text1)
    TextView featureText1;
    @BindView(R.id.feature_seek1)
    SeekBar featureSeek1;
    @BindView(R.id.feature_text2)
    TextView featureText2;
    @BindView(R.id.feature_seek2)
    SeekBar featureSeek2;
    @BindView(R.id.feature_text3)
    TextView featureText3;
    @BindView(R.id.feature_seek3)
    SeekBar featureSeek3;
    @BindView(R.id.feature_text4)
    TextView featureText4;
    @BindView(R.id.feature_seek4)
    SeekBar featureSeek4;
    @BindView(R.id.feature_text5)
    TextView featureText5;
    @BindView(R.id.feature_seek5)
    SeekBar featureSeek5;
    @BindView(R.id.feature_text6)
    TextView featureText6;
    @BindView(R.id.feature_seek6)
    SeekBar featureSeek6;
    @BindView(R.id.opt_layout)
    LinearLayout optLayout;
    @BindView(R.id.show_image_view)
    ZoomImageView showImageView;

    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);

    private String mImgPath = "";
    private final String mImgName = "22.png";

    private Thread thread;
    private boolean canRun = true;

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsvdetection);
        ButterKnife.bind(this);
        setSupportActionBar(R.id.tool_bar);
        init();
    }

    private void init() {
        mLowerBound.val[0] = 0;
        mUpperBound.val[0] = 255;

        mLowerBound.val[1] = 0;
        mUpperBound.val[1] = 255;

        mLowerBound.val[2] = 0;
        mUpperBound.val[2] = 255;

        mLowerBound.val[3] = 0;
        mUpperBound.val[3] = 255;

        initView();

    }

    private Bitmap dealImageToShow() {
        //读取并显示图片
        mImgPath = getExternalFilesDir(SCREEN_SHOT_PATH_NAME) + File.separator;
        Mat imageMat = Imgcodecs.imread(mImgPath + mImgName);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(imageMat, mLowerBound, mUpperBound, imageMat);
        Bitmap bitmap = Bitmap.createBitmap(imageMat.width(), imageMat.height(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(imageMat, bitmap);
        return bitmap;
    }

    private void initView() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (canRun) {
                    final Bitmap bitmap = dealImageToShow();
                    HSVDetectionActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showImageView.setImageBitmap(bitmap);
                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        featureSeek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLowerBound.val[0] = progress;
                featureText1.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        featureSeek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mUpperBound.val[0] = progress;
                featureText2.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        featureSeek3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLowerBound.val[1] = progress;
                featureText3.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        featureSeek4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mUpperBound.val[1] = progress;
                featureText4.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        featureSeek5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLowerBound.val[2] = progress;
                featureText5.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        featureSeek6.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mUpperBound.val[2] = progress;
                featureText6.setText(String.valueOf(progress));
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
    protected void onPause() {
        super.onPause();
        canRun = false;
        thread.interrupt();
    }

    @Override
    protected void onResume() {
        super.onResume();
        canRun = true;
        if (thread != null && thread.isInterrupted()) {
            thread.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        canRun = false;
    }
}

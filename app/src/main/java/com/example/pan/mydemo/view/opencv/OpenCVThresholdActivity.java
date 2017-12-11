package com.example.pan.mydemo.view.opencv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.widget.ZoomImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

import butterknife.BindView;

public class OpenCVThresholdActivity extends BaseActivity {

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
    @BindView(R.id.opt_layout)
    LinearLayout optLayout;
    @BindView(R.id.show_image_view)
    ZoomImageView showImageView;

    private String mImgPath = "";
    private final String mImgName = "22.png";

    private Thread thread;
    private boolean canRun = true;

    private int minVal = 0;
    private int maxVal = 255;

    static {
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvthreshold);
        setSupportActionBar(R.id.tool_bar);
        initView();
    }

    private Bitmap dealImageToShow() {
        //读取并显示图片
        mImgPath = getExternalFilesDir("hsv") + File.separator;
        Mat imageMat = Imgcodecs.imread(mImgPath + mImgName);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.threshold(imageMat, imageMat, minVal, maxVal, Imgproc.THRESH_OTSU);
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
                    OpenCVThresholdActivity.this.runOnUiThread(new Runnable() {
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
                minVal = progress;
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
                maxVal = progress;
                featureText2.setText(String.valueOf(progress));
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

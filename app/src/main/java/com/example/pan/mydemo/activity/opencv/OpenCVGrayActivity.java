package com.example.pan.mydemo.activity.opencv;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class OpenCVGrayActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cvgray);

        Button GiveFaceButton = (Button) findViewById(R.id.GiveFaceButton);
        GiveFaceButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent();

                //开启picture画面Type设置为image*
                myIntent.setType("image/*");

                //使用Intent.ACTION_GET_CONTENT这个Action
                myIntent.setAction(Intent.ACTION_GET_CONTENT);

                /*取得相片后返回本画面*/
                startActivityForResult(myIntent, 1);
            }
        });

        Button GetFaceButton = (Button) findViewById(R.id.GetFaceButton);
        GetFaceButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent();

                //开启picture画面Type设置为image*
                myIntent.setType("image/*");

                //使用Intent.ACTION_GET_CONTENT这个Action
                myIntent.setAction(Intent.ACTION_GET_CONTENT);

                /*取得相片后返回本画面*/
                startActivityForResult(myIntent, 2);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                Uri uri = data.getData();
                LogUtils.e( uri.toString());
                ContentResolver cr = this.getContentResolver();

                try {
                    InputStream input = cr.openInputStream(uri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(input, null, options);
                    if (options.outWidth > 1024 || options.outHeight > 1024) {
                        options.inSampleSize = Math.max(options.outWidth / 1024, options.outHeight / 1024);
                    }
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri), null, options);

                    /*以下进行灰度化处理（使用C++写底层函数jni调用）*/
                    /*
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();
                    int[] pix = new int[w * h];
                    bitmap.getPixels(pix, 0, w, 0, 0, w, h);
                    int[] resultPixels = OpenCVHelper.gray(pix, w, h);
                    Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
                    result.setPixels(resultPixels,0,w,0,0,w,h);
                    */

                    ImageView imageView = (ImageView) findViewById(R.id.IV01);
                    /*将处理好的灰度图设定到ImageView*/
                    imageView.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            case 2:
                Uri uri2 = data.getData();
                LogUtils.e(uri2.toString());
                ContentResolver cr2 = this.getContentResolver();

                try {
                    InputStream input = cr2.openInputStream(uri2);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(input, null, options);
                    if (options.outWidth > 1024 || options.outHeight > 1024) {
                        options.inSampleSize = Math.max(options.outWidth / 1024, options.outHeight / 1024);
                    }
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(cr2.openInputStream(uri2), null, options);

                    /*使用opencv包进行灰度化处理*/
                    OpenCVLoader.initDebug();

                    Mat rgbMat = new Mat();
                    Mat grayMat = new Mat();
                    Bitmap grayBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
                    Utils.bitmapToMat(bitmap, rgbMat);
                    Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);
                    Utils.matToBitmap(grayMat, grayBitmap);

                    /*将grayBitmap设定到ImageView*/
                    ImageView imageView = (ImageView) findViewById(R.id.IV02);
                    imageView.setImageBitmap(grayBitmap);
                } catch (FileNotFoundException e) {
                    LogUtils.e(e.getMessage());
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}

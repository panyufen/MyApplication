package com.example.pan.mydemo.adapter;

import android.content.Context;

import com.cus.pan.library.utils.FileUtils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * Created by PAN on 2017/8/24.
 */

public class GameOcrDataBase {


    private String imgPath = "";

    String[] strings = {
            "之", "翼", "羽", "织",
            "尾", "=", "=", "星",
            "辰", "夏", "沫", "卢",
            "比", "纳", "特", "贪",
            "婪", "翅", "膀", "夜",
            "墮", "落", "确", "定"
    };

    float[] trainingLabels = {
            0, 1, 2, 3,
            4, 5, 6, 7,
            8, 9, 10, 11,
            12, 13, 14, 9,
            10, 1, 5, 2,

            5, 6, 6, 3,
            10, 2, 9, 9,
            7, 7, 0, 10,
            10, 10, 10, 8,
            8, 9, 9, 9,

            2, 2, 2, 4,
            4, 3, 3, 1,
            5, 5, 5, 5,
            5, 5, 6, 6,
            6, 6, 6, 6,
            15, 15, 16, 16,
            20, 20, 21, 21,
            19, 2, 17, 17,
            17, 18, 18, 18,
            22, 23


    };

    Mat trainingLabelsMat = new Mat(trainingLabels.length, 1, CvType.CV_32SC1);
    Mat trainingDataMat = new Mat(trainingLabels.length, 525, CvType.CV_32FC1);

    public GameOcrDataBase(final Context context) {
        imgPath = FileUtils.getCacheDir(context).getAbsolutePath() + "/gameocr/";
        //将assets 中ocr 样本复制到sdcard中
        FileUtils.copyFolderFromAssets(context, false, "gameocr", imgPath);
//        Log.i("imgPath", "imagpath" + imgPath);
        Mat source;

        for (int i = 0; i < trainingLabels.length; i++) {
            String imgAbsPath = imgPath + (i / 10) + (i % 10) + ".png";
            source = Imgcodecs.imread(imgAbsPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            int totalLength = source.width() * source.height();
//            Log.i("imgPath", "imagpath length " + totalLength + " " + imgAbsPath);
            source.convertTo(source, CvType.CV_32F);
            Mat temp = source.reshape(1, totalLength);//change image's col & row
            for (int j = 0; j < totalLength; j++) {
                double[] data = temp.get(j, 0);
                trainingDataMat.put(i, j, data);
            }
            //assign label data
            trainingLabelsMat.put(i, 0, trainingLabels[i]);
        }


    }

    public Mat getTrainingLabelsMat() {
        return trainingLabelsMat;
    }

    public Mat getTrainingDataMat() {
        return trainingDataMat;
    }

    public String getResultLabel(int f) {
        if (f >= 0 && f < strings.length) {
            return strings[f];
        }
        return "";
    }
}

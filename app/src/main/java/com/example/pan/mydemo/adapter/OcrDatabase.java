package com.example.pan.mydemo.adapter;

import android.content.Context;
import android.util.Log;

import com.cus.pan.library.utils.FileUtils;
import com.cus.pan.library.utils.LogUtils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OcrDatabase {

    private String imgPath = "";

    Mat trainingLabelsMat = new Mat(330, 1, CvType.CV_32SC1);
    Mat trainingDataMat = new Mat(330, 144, CvType.CV_32FC1);
    List<String> strings = new ArrayList<>();

    float[] trainingLabels = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3,
            4, 4, 4, 4, 4, 4, 4, 4, 4, 4,
            5, 5, 5, 5, 5, 5, 5, 5, 5, 5,
            6, 6, 6, 6, 6, 6, 6, 6, 6, 6,
            7, 7, 7, 7, 7, 7, 7, 7, 7, 7,
            8, 8, 8, 8, 8, 8, 8, 8, 8, 8,
            9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
            'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A', 'A',
            'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B', 'B',
            'C', 'C', 'C', 'C', 'C', 'C', 'C', 'C', 'C', 'C',

            'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D', 'D',
            'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E', 'E',
            'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F', 'F',

            'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G', 'G',
            'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H', 'H',
            'J', 'J', 'J', 'J', 'J', 'J', 'J', 'J', 'J', 'J',

            'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K', 'K',
            'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L',
            'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M', 'M',

            'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'N',
            'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P',
            'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q', 'Q',

            'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S', 'S',
            'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T', 'T',
            'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U', 'U',

            'V', 'V', 'V', 'V', 'V', 'V', 'V', 'V', 'V', 'V',
            'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W', 'W',
            'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X',

            'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y', 'Y',
            'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z', 'Z'
    };


    public OcrDatabase(final Context context) {
        imgPath = FileUtils.getCacheDir(context).getAbsolutePath() + "/ocr/";
        //将assets 中ocr 样本复制到sdcard中
        FileUtils.copyFolderFromAssets(context, false, "ocr", imgPath);

        Log.i("imgPath", "imagpath" + imgPath);
        Mat source;

        //assign training Mat [0-9 a-z 各10个样本]
        for (int i = 0; i < 330; i++) {
            String imgAbsPath = "";
            if (i < 10) {
                imgAbsPath = imgPath + "0" + i + ".jpg";
            } else {
                imgAbsPath = imgPath + i + ".jpg";
            }
            File file = new File(imgAbsPath);
            if (!file.exists()) {
                String index = i + "";
                index = index.substring(0, 2);
                imgAbsPath = imgPath + index + "1" + ".jpg";
            }
            source = Imgcodecs.imread(imgAbsPath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
            //assign ocr's image data into Mat
            Mat temp = source.reshape(1, 144);//change image's col & row
            for (int j = 0; j < 144; j++) {
                double[] data = new double[1];
                data = temp.get(j, 0);
                trainingDataMat.put(i, j, data);
            }
            LogUtils.i("gameotion 2 " + trainingDataMat.row(0) + " " + source.type());
            //assign label data
            trainingLabelsMat.put(i, 0, trainingLabels[i]);
        }
    }

    public Mat getTrainingLabelsMat() {
        return trainingLabelsMat;
    }

    public void setTrainingLabelsMat(Mat trainingLabelsMat) {
        this.trainingLabelsMat = trainingLabelsMat;
    }

    public Mat getTrainingDataMat() {
        return trainingDataMat;
    }

    public void setTrainingDataMat(Mat trainingDataMat) {
        this.trainingDataMat = trainingDataMat;
    }


    public Mat handleForML(Mat src) {
        Mat handle = new Mat(1, 144, CvType.CV_32FC1);
        Mat tempSample = src.reshape(1, 144);
        for (int j = 0; j < 144; j++) {
            double[] data = new double[1];
            data = tempSample.get(j, 0);
            handle.put(0, j, data);
        }

        return handle;
    }

    public String getResultLabel(int f) {
        return strings.get(f);
    }
}

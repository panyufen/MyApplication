package com.example.pan.mydemo.adapter;

import android.content.Context;
import android.util.Log;

import com.cus.pan.library.utils.FileUtils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;

/**
 * Created by PAN on 2016/11/10.
 */

public class FaceDetectionFilter implements DetectionFilter {

    private Mat mRgba;
    private Mat mGray;

    private Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0);
    private Scalar EYE_RECT_COLOR = new Scalar(255, 0, 0);

    private File mCascadeFile;
    private CascadeClassifier faceCascade;
    private CascadeClassifier leftEyeCascade;
    private CascadeClassifier rightEyeCascade;

    private float mRelativeFaceSize = 0.05f;
    private int mAbsoluteFaceSize = 0;

    MatOfRect faces;

    MatOfRect leftEyes;
    MatOfRect rightEyes;

    public FaceDetectionFilter(Context context) {
        //  haarcascade_frontalcatface_extended.xml
        //  lbpcascade_frontalcatface.xml
        //  lbpcascade_frontalface.xml
        //  lbpcascade_profileface.xml
        //  lbpcascade_silverware.xml
        //  haarcascade_eye.xml
        //  haarcascade_eye_tree_eyeglasses.xml
        //  haarcascade_lefteye_2splits.xml
        //  haarcascade_righteye_2splits.xml
        //  haarcascade_frontalface_alt.xml

        String faceTrainName = "lbpcascade_frontalface.xml";
        String leftEyeTrainName = "haarcascade_righteye_2splits.xml";
        String rightEyeTrainName = "haarcascade_lefteye_2splits.xml";

        FileUtils.copyFolderFromAssets(context,false,"harr",FileUtils.getCacheDir(context).getAbsolutePath() + "/harr");
        mCascadeFile = new File(FileUtils.getCacheDir(context).getAbsolutePath() + "/harr/", faceTrainName);
        faceCascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        mCascadeFile = new File(FileUtils.getCacheDir(context).getAbsolutePath() + "/harr/", leftEyeTrainName);
        leftEyeCascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());

        mCascadeFile = new File(FileUtils.getCacheDir(context).getAbsolutePath() + "/harr/", rightEyeTrainName);
        rightEyeCascade = new CascadeClassifier(mCascadeFile.getAbsolutePath());


    }

    @Override
    public void apply(Mat rgba, Mat gray, Mat dst) {
        mRgba = rgba;
        mGray = gray;
        int height = mGray.rows();
        if (mAbsoluteFaceSize == 0) {
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        faces = new MatOfRect();

        if (faceCascade != null) {
            faceCascade.detectMultiScale(mGray, faces, 1.1, 2, 2, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        if (dst != mRgba) {
            mRgba.copyTo(dst);
        }

        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
            Rect tempFaceArea = facesArray[i];
            Imgproc.rectangle(dst, tempFaceArea.tl(), tempFaceArea.br(), FACE_RECT_COLOR, 2);
            Log.i("height ","height "+tempFaceArea.height+" "+height*0.3);
            if (tempFaceArea.height >= height * 0.3) {//大于等于屏幕高度0.3时 才进行人面部特征识别
                Mat faceAreaGray = mGray.submat(tempFaceArea);
                Mat faceAreaRgba = dst.submat(tempFaceArea);

                leftEyes = new MatOfRect();
                if (leftEyeCascade != null) {
                    leftEyeCascade.detectMultiScale(faceAreaGray, leftEyes, 1.1, 2, 2, new Size(60, 60), new Size(mAbsoluteFaceSize, mAbsoluteFaceSize));
                }
                Rect[] leftEyesArray = leftEyes.toArray();
                for (int j = 0; j < leftEyesArray.length; j++) {
                    Imgproc.rectangle(faceAreaRgba, leftEyesArray[j].tl(), leftEyesArray[j].br(), EYE_RECT_COLOR, 2);
                }

                rightEyes = new MatOfRect();
                if (rightEyeCascade != null) {
                    rightEyeCascade.detectMultiScale(faceAreaGray, rightEyes, 1.1, 2, 2, new Size(60, 60), new Size(mAbsoluteFaceSize, mAbsoluteFaceSize));
                }
                Rect[] rightEyesArray = leftEyes.toArray();
                for (int j = 0; j < rightEyesArray.length; j++) {
                    Imgproc.rectangle(faceAreaRgba, rightEyesArray[j].tl(), rightEyesArray[j].br(), EYE_RECT_COLOR, 2);
                }
//            Imgproc.cvtColor(faceAreaRgba,faceAreaRgba,Imgproc.COLOR_RGB2GRAY);

                faceAreaGray.release();
                faceAreaRgba.release();
            }
        }

    }

}

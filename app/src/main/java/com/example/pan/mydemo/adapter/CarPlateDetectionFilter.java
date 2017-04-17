package com.example.pan.mydemo.adapter;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_32FC1;

/**
 * Created by PAN on 2016/11/11.
 */
public class CarPlateDetectionFilter implements DetectionFilter {

    private boolean initStatus = false;

    Mat drawOriMat;

    KNearest kNearest = KNearest.create();

    Mat results = new Mat();
    Mat neighborResponses = new Mat();
    Mat dist = new Mat();

    OcrDatabase ocrDatabase;

    int detectionDep = 0;

    double time = 1000_000_000d;

    public CarPlateDetectionFilter(final Context context, int res) throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ocrDatabase = new OcrDatabase(context);
                boolean r = kNearest.train(ocrDatabase.getTrainingDataMat(), Ml.ROW_SAMPLE, ocrDatabase.getTrainingLabelsMat());
                Log.i("train", "train 训练 是否成功 " + r);
                initStatus = true;
            }
        }).start();
        mLowerBound.val[0] = 140;
        mUpperBound.val[0] = 160;

        mLowerBound.val[1] = 160;
        mUpperBound.val[1] = 255;

        mLowerBound.val[2] = 100;
        mUpperBound.val[2] = 200;

        mLowerBound.val[3] = 0;
        mUpperBound.val[3] = 255;
    }

    public void setLowS(int i) {
        mLowerBound.val[2] = i;
    }

    @Override
    public void apply(Mat rgba, Mat gray, Mat dst) {
        detectionDep = 1;
        rgba.copyTo(dst);
        drawOriMat = dst;

        //矩形车牌定位
//        findCard(gray, dst);
//        numberDetection();
        Log.i("applyTime","start "+System.nanoTime()/time);
        //颜色定位车牌,并识别
        findCardByColor(rgba, gray, dst);
        Log.i("applyTime","end "+System.nanoTime()/time);
    }

    // Lower and Upper bounds for range checking in HSV color space
    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);

    private void findCardByColor(Mat rgba, Mat gray, Mat dst) {
        Mat rangeMat = new Mat();
        Mat sourceHsv = new Mat();
        Imgproc.cvtColor(rgba, sourceHsv, Imgproc.COLOR_RGB2HSV_FULL);
        Log.i("applyTime","start1 "+System.nanoTime()/time);
        Core.inRange(sourceHsv, mLowerBound, mUpperBound, rangeMat);

        Log.i("applyTime","start2 "+System.nanoTime()/time);
        sourceHsv.release();

//        Imgproc.threshold(rangeMat, rangeMat, 0, 255, Imgproc.THRESH_OTSU);

        Log.i("applyTime","start3 "+rangeMat.channels());

//太卡 先去除
//         //除噪可扫描车牌轮廓
//        Imgproc.GaussianBlur(rangeMat, rangeMat, new Size(9, 9), 0, 0);
//        Log.i("applyTime","start3 "+System.nanoTime()/time);
//        // dilate
//        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 2));
//        Log.i("applyTime","start3 "+System.nanoTime()/time);
//        Imgproc.dilate(rangeMat, rangeMat, element);
//        Log.i("applyTime","start4 "+System.nanoTime()/time);
//        Imgproc.dilate(rangeMat, rangeMat, element);
//        Log.i("applyTime","start5 "+System.nanoTime()/time);
//        Imgproc.dilate(rangeMat, rangeMat, element);
//        Log.i("applyTime","start6 "+System.nanoTime()/time);


        Mat tempRangeMat = new Mat(); //用于轮廓检测
        rangeMat.copyTo(tempRangeMat);
        List<MatOfPoint> contours = new ArrayList<>();
        List<MatOfInt> hull = new ArrayList<>();
        Mat hierarchy = new Mat();

        Log.i("applyTime","start8 "+System.nanoTime()/time);
        Imgproc.findContours(tempRangeMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Log.i("applyTime","start9 "+System.nanoTime()/time);
        int contoursSize = contours.size();
        for (int i = 0; i < contoursSize; i++) {
            MatOfInt matOfInt = new MatOfInt();
            Imgproc.convexHull(contours.get(i),matOfInt, false);
            int index=(int)matOfInt.get(((int)matOfInt.size().height)-1,0)[0];
            Log.i("hull",matOfInt.size()+" "+index);
        }

        tempRangeMat.release();
        hierarchy.release();

        for (MatOfPoint cp : contours) {
            if (cp.toList().size() < 2) {
                continue;
            }
            Rect r = Imgproc.boundingRect(cp);

            if (r.width < 80 || r.height < 30) { //宽小于80 或高小于50 舍去
                continue;
            }

            if (r.x < rangeMat.cols() * 0.1 || r.y < rangeMat.rows() * 0.1 //边界处理 在屏幕边缘的舍去
                    || r.x + r.width > rangeMat.cols() * 0.9 || r.y + r.height > rangeMat.rows() * 0.9) {
                continue;
            }
            float borderW, borderH;
            borderW = r.width;
            borderH = r.height;

            if (borderW / borderH < 1.5) { //宽高比小于2的舍去
                continue;
            }

            Mat tempCarPlateAreaGray = new Mat(); //灰色车牌区域 用于处理 寻找文字轮廓
            Mat carPlateAreaGray = new Mat();
            gray.submat(r).copyTo(tempCarPlateAreaGray);
            if (borderW / borderH >= 1.5 && borderW / borderH < 2.8f) {  //可能为不是水平拍照 需要矫正
                Mat tempDealMat = rangeMat.submat(r);
                Mat lines = new Mat();
                Imgproc.Canny(tempDealMat, tempDealMat, 50, 50);        // canny边缘检测为了Hough减少运算量
                Imgproc.HoughLines(tempDealMat, lines, 20, Math.PI / 180, tempDealMat.cols());

                double xMove = 0, yMove = 0;
                boolean leftAffine = true;
                for (int x = 0; x < lines.total(); x++) {
                    double[] vec = lines.get(0, x);
                    if (vec != null && vec.length == 2) {
                        double rho = vec[0];
                        double theta = vec[1];
                        Point start;
                        Point end;
                        double a = Math.cos(theta);
                        double b = Math.sin(theta);
                        double x0 = a * rho;
                        double y0 = b * rho;
                        start = new Point(x0 + 1000 * (-b), y0 + 1000 * a);
                        end = new Point(x0 - 1000 * (-b), y0 - 1000 * a);
                        Imgproc.line(tempDealMat, start, end, new Scalar(255), 3);
                        //寻找在区域内的start end 点
//                        Log.i("startPoint ", "pint" + tempDealMat.size() + " " + start.x + " " + start.y + " " + end.x + " " + end.y);

                        double range = (end.y - start.y) / (end.x - start.x);
                        yMove = tempDealMat.cols() * -range * 0.95;

                        Log.i("xMoveyMove", "xMoveyMove " + tempDealMat.size() + " " + xMove + " " + yMove + " " + leftAffine);
                    }
                }
                lines.release();

                MatOfPoint2f srcTri = new MatOfPoint2f();
                MatOfPoint2f dstTri = new MatOfPoint2f();

                Point[] srcPoints = new Point[3];
                Point[] dstPoints = new Point[3];

                srcPoints[0] = new Point(0, 0);
                srcPoints[1] = new Point(tempDealMat.cols() - 1, 0);
                srcPoints[2] = new Point(0, tempDealMat.rows() - 1);
                dstPoints[0] = new Point(0, 0);
                dstPoints[1] = new Point(tempDealMat.cols() - 1, (int) (yMove));
                dstPoints[2] = new Point(0, tempDealMat.rows() - 1 - tempDealMat.rows() * 0.15);

                srcTri.fromArray(srcPoints);
                dstTri.fromArray(dstPoints);

                Mat wrapMat = Imgproc.getAffineTransform(srcTri, dstTri);

                Imgproc.warpAffine(rgba.submat(r), rgba.submat(r), wrapMat, tempDealMat.size());//用于显示
                Imgproc.warpAffine(gray.submat(r), gray.submat(r), wrapMat, tempDealMat.size());


//                Imgproc.putText(rangeMat, (borderW / borderH) + " ", r.tl(), 0, 1, new Scalar(255), 2);
//                Imgproc.rectangle(rangeMat, r.tl(), r.br(), new Scalar(255), 3);


//                Imgproc.putText(dst, (borderW / borderH) + " ", r.tl(), 0, 1, new Scalar(255), 2);
//                Imgproc.rectangle(dst, r.tl(), r.br(), new Scalar(0, 255, 0), 3);

                rgba.submat(r).copyTo(dst.submat(r));//显示矫正后的效果

                if (detectionDep <= 2) {
                    detectionDep++;//防止递归死循环
                    findCardByColor(rgba, gray, dst);
                }

                tempDealMat.release();
                wrapMat.release();
            }

            if (borderW / borderH >= 2.8f && borderW / borderH < 4f) { //正规水平蓝色车牌
                Mat plateAreaRgba = dst.submat(r);
                Imgproc.threshold(tempCarPlateAreaGray, tempCarPlateAreaGray, 0, 255, Imgproc.THRESH_OTSU);
                //图片边缘处理
                Imgproc.line(tempCarPlateAreaGray, new Point(0, 0), new Point(tempCarPlateAreaGray.cols(), 0), new Scalar(0), r.height / 3);
                Imgproc.line(tempCarPlateAreaGray, new Point(0, tempCarPlateAreaGray.rows()), new Point(tempCarPlateAreaGray.cols(), tempCarPlateAreaGray.rows()), new Scalar(0), r.height / 3);
                Imgproc.line(tempCarPlateAreaGray, new Point(0, 0), new Point(0, tempCarPlateAreaGray.rows()), new Scalar(0), r.height / 4);
                Imgproc.line(tempCarPlateAreaGray, new Point(tempCarPlateAreaGray.cols(), 0), new Point(tempCarPlateAreaGray.cols(), tempCarPlateAreaGray.rows()), new Scalar(0), r.height / 4);

                Mat wordElement = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(2, 5));
                if( r.width > 300 ){
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                }
                if (r.width > 480) {
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                }
                if (r.width > 1000) { //如果车牌区域较大 需要多次膨胀
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                    Imgproc.dilate(tempCarPlateAreaGray, tempCarPlateAreaGray, wordElement);
                }

                tempCarPlateAreaGray.copyTo(carPlateAreaGray);

                List<MatOfPoint> inContours = new ArrayList<>();  //查找特征
                Mat inHierarchy = new Mat();
                Imgproc.findContours(tempCarPlateAreaGray, inContours, inHierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
                Mat sourceMat = new Mat(1, 144, CV_32FC1);
                for (MatOfPoint icp : inContours) {
                    if (cp.toList().size() < 2) {
                        continue;
                    }

                    Rect ir = Imgproc.boundingRect(icp);

                    if (ir.height < borderH * 0.6//高度小于边框6/10
//                            || ir.tl().y < borderH * 0.1 //文字顶部高度距离边框顶部小于 1/10
//                            || ir.br().y > borderH * 0.9 //文字顶部高度距离边框顶部小于 1/10
                            ) {
                        continue;
                    }
                    if (ir.width < borderH && ir.height < borderH * 0.8) {
                        Imgproc.rectangle(plateAreaRgba, ir.tl(), ir.br(), new Scalar(255), 1);

                        Mat sample = gray.submat(r).submat(ir);
                        Mat wordDst = new Mat(12, 12, CvType.CV_32F);
                        Imgproc.threshold(sample, sample, 0, 255, Imgproc.THRESH_OTSU);
                        Core.bitwise_not(sample, sample);
                        Imgproc.resize(sample, wordDst, wordDst.size(), 0, 0, Imgproc.INTER_AREA);
                        wordDst.convertTo(wordDst, CvType.CV_32F);

                        //显示识别前文字处理效果
                        Mat showWord = new Mat();
                        sample.copyTo(showWord);

                        Imgproc.cvtColor(showWord, showWord, Imgproc.COLOR_GRAY2RGBA);
                        if (plateAreaRgba.rows() >= ir.y + showWord.rows() && plateAreaRgba.cols() > ir.x + showWord.cols()) {
                            showWord.copyTo(plateAreaRgba.submat(ir.y, ir.y + showWord.rows(), ir.x, ir.x + showWord.cols()));
                        }

                        wordDst = wordDst.reshape(1, 144);
                        for (int k = 0; k < 144; k++) {
                            double[] data = wordDst.get(k, 0);
                            sourceMat.put(0, k, data);
                        }

                        if( initStatus ) {//如果识别库初始化完毕才进行识别
                            int result = (int) kNearest.findNearest(sourceMat.row(0), 1, results, neighborResponses, dist);

                            String ocrResult = "" + result;
                            if (result >= 65 && result <= 90) {//英文字母
                                ocrResult = String.valueOf((char) result);
                            }
                            Imgproc.putText(plateAreaRgba, ocrResult, new Point(ir.x, ir.y + ir.height), 0, 1, new Scalar(255, 0, 0), 2);
                        }
                        sample.release();
                        wordDst.release();
                        showWord.release();
                    }
                }

//                Imgproc.cvtColor(carPlateAreaGray, carPlateAreaGray, Imgproc.COLOR_GRAY2RGBA);
//                carPlateAreaGray.copyTo(dst.submat(r));  //显示处理后的图像

                Imgproc.putText(dst, String.format("%.2f", borderW / borderH)+" "+borderW, r.tl(), 0, 1, new Scalar(255), 2);
                Imgproc.rectangle(dst, r.tl(), r.br(), new Scalar(0, 255, 0), 1);

                plateAreaRgba.release();
                wordElement.release();
                sourceMat.release();
                tempCarPlateAreaGray.release();
                inHierarchy.release();
                carPlateAreaGray.release();
            }
        }
        rangeMat.release();
    }
}

package com.example.pan.mydemo.adapter;

import android.content.Context;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图像检测的类，大概思路如下所示
 * 1、对参考图片和实时获取的视频帧数据的处理（特征点的检测描述）
 * 2、对描述的特征点进行匹配运算
 * 3、根据匹配结果判断特征点之间距离，在根据距离拾取最佳的点
 * 4、根据拾取的点计算出单应性矩阵
 * 5、根据单应性矩阵对模版图片进行透视变换，
 * 6、再提取变化之后的角点坐标，使用线框连接四个角点即可
 *
 * @author scy
 */
public class ImageDetectionFilter implements DetectionFilter {

    // 参考图片帧
    private final Mat mReferenceImage;
    private final MatOfKeyPoint mReferenceKeypoints =
            new MatOfKeyPoint();
    private final Mat mReferenceDescriptors = new Mat();
    // CVType defines the color depth, number of channels, and
    // channel layout in the image.
    private Mat mReferenceCorners = new Mat(4, 1, CvType.CV_32FC2);

    // 场景图片帧
    private final MatOfKeyPoint mSceneKeypoints = new MatOfKeyPoint();
    private final Mat mSceneDescriptors = new Mat();
    private final Mat mCandidateSceneCorners = new Mat(4, 1, CvType.CV_32FC2);
    private Mat mSceneCorners = new Mat(4, 1, CvType.CV_32FC2);
    private final MatOfPoint mIntSceneCorners = new MatOfPoint();

    ArrayList<Point> goodReferencePointsList = new ArrayList<Point>();
    ArrayList<Point> goodScenePointsList = new ArrayList<Point>();


    // 灰度图片
    private final Mat mGraySrc = new Mat();
    // 获取匹配对
    private final MatOfDMatch mMatches = new MatOfDMatch();
    /**
     * 定义检测、描述和匹配相关算法
     * 使用star、freak和汉明距离的强制匹配算法
     */
    private final FeatureDetector mFeatureDetector = FeatureDetector.create(FeatureDetector.DYNAMIC_ORB);
    private final DescriptorExtractor mDescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.OPPONENT_ORB);
    private final DescriptorMatcher mDescriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);
    // 设置line绘制的颜色，绿色
    private final Scalar mLineColor = new Scalar(0, 255, 0);


    private boolean canProcess = true;

    double time = 1000000000d;
    /**
     * 构造方法，类的初始化，以及对模板（参考）图片的一些处理
     *
     * @param context
     * @param referenceImageResourceID
     * @throws IOException
     */
    public ImageDetectionFilter(final Context context, final int referenceImageResourceID) throws IOException {
        this(Utils.loadResource(context, referenceImageResourceID, Imgcodecs.CV_LOAD_IMAGE_COLOR));
    }


    public ImageDetectionFilter(Mat referenceImg) throws IOException {
        //加载图片
        mReferenceImage = referenceImg;
        // 对图片进行格式转换
        final Mat referenceImageGray = new Mat();
        Imgproc.cvtColor(mReferenceImage, referenceImageGray, Imgproc.COLOR_BGR2GRAY);

//        Imgproc.cvtColor(mReferenceImage, mReferenceImage, Imgproc.COLOR_BGR2RGBA);

        // 定义参考图片的上下左右角点，为后面 的仿射变化做准备
        mReferenceCorners = new Mat(4, 1, CvType.CV_32FC2);
        mReferenceCorners.put(0, 0, new double[]{0.0, 0.0});
        mReferenceCorners.put(1, 0, new double[]{referenceImageGray.cols(), 0.0});
        mReferenceCorners.put(2, 0, new double[]{referenceImageGray.cols(), referenceImageGray.rows()});
        mReferenceCorners.put(3, 0, new double[]{0.0, referenceImageGray.rows()});
        // 对特征点进行检测和描述
        mFeatureDetector.detect(referenceImageGray, mReferenceKeypoints);
        mDescriptorExtractor.compute(referenceImageGray, mReferenceKeypoints, mReferenceDescriptors);
        canProcess = true;
    }


    /**
     * 对场景图片帧进行相关处理
     * 其中在这里获取匹配对
     * 然后绘制获取的线框
     */
    @Override
    public void apply(final Mat src, final Mat gray, final Mat dst) {
        Log.i("applyTime","start0 "+System.nanoTime()/time);
        Imgproc.cvtColor(src, mGraySrc, Imgproc.COLOR_RGBA2GRAY);
        Log.i("applyTime","start1 "+System.nanoTime()/time);
        if (canProcess) {
            mFeatureDetector.detect(mGraySrc, mSceneKeypoints);
            Log.i("applyTime","start2 "+System.nanoTime()/time);
            if (dst != src) {
                src.copyTo(dst);
            }

            if( goodScenePointsList.size() < 4 ) {//主要是为了画特征点，没有其他作用
                for (KeyPoint sIp : mSceneKeypoints.toList()) {
                    Imgproc.circle(dst, new Point(sIp.pt.x, sIp.pt.y), 5, new Scalar(0, 255, 0));
                }
            }
            Log.i("applyTime","start3 "+System.nanoTime()/time);
            mDescriptorExtractor.compute(mGraySrc, mSceneKeypoints,mSceneDescriptors);
            Log.i("applyTime","start4 "+System.nanoTime()/time);
            if(canProcess) {
                mDescriptorMatcher.match(mSceneDescriptors,mReferenceDescriptors, mMatches);
                Log.i("applyTime","start5 "+System.nanoTime()/time);
                if( canProcess ) {
                    findSceneCorners();
                    Log.i("applyTime","start6 "+System.nanoTime()/time);
                    draw(src, dst);
                    Log.i("applyTime","start7 "+System.nanoTime()/time);
                }else{
                    src.copyTo(dst);
                }
            }else{
                src.copyTo(dst);
            }
        }else{
            src.copyTo(dst);
        }
    }

    private void findSceneCorners() {

        List<DMatch> matchesList = mMatches.toList();
        // 匹配对太少
        if (matchesList.size() < 4) {
            // There are too few matches to find the homography.
            return;
        }
        // 将MatOfKeyPoint数据结构存储的特征点数据转换成List，便于后面获取
        List<KeyPoint> referenceKeypointsList = mReferenceKeypoints.toList();
        List<KeyPoint> sceneKeypointsList = mSceneKeypoints.toList();

        // Calculate the max and min distances between keypoints.
        // 计算特征点之间的最大和最小距离
        double maxDist = 0.0;
        double minDist = Double.MAX_VALUE;
        for (DMatch match : matchesList) {
            double dist = match.distance;
            if (dist < minDist) {
                minDist = dist;
            }
            if (dist > maxDist) {
                maxDist = dist;
            }
        }

        // The thresholds for minDist are chosen subjectively
        // based on testing. The unit is not related to pixel
        // distances; it is related to the number of failed tests
        // for similarity between the matched descriptors.
        // 根据距离对角点进行取舍
        if (minDist > 50.0) {
            // The target is completely lost.
            // Discard any previously found corners.
            mSceneCorners.create(0, 0, mSceneCorners.type());
            return;
        } else if (minDist > 25.0) {
            // The target is lost but maybe it is still close.
            // Keep any previously found corners.
            return;
        }

        // Identify "good" keypoints based on match distance.
        goodReferencePointsList = new ArrayList<Point>();
        goodScenePointsList = new ArrayList<Point>();
        // 最佳距离极限为minDist的1.75倍，然后拾取在此范围的点到ArrayList中
        double maxGoodMatchDist = 1.75 * minDist;
        for (DMatch match : matchesList) {
            if (match.distance < maxGoodMatchDist) {
                goodReferencePointsList.add(referenceKeypointsList.get(match.trainIdx).pt);
                goodScenePointsList.add(sceneKeypointsList.get(match.queryIdx).pt);
            }
        }

        // 如果在范围内的（拾取到的）点数小于4,则表示没有发现标志
        if (goodReferencePointsList.size() < 4 ||
                goodScenePointsList.size() < 4) {
            // There are too few good points to find the homography.
            return;
        }

        // 再从ArrayList转换成MatOfPoint2f数据结构（OpenCV中），同样是为了后面的处理
        // 一个是参考图片的点数据，一个是图像帧的点数据
        MatOfPoint2f goodReferencePoints = new MatOfPoint2f();
        goodReferencePoints.fromList(goodReferencePointsList);

        MatOfPoint2f goodScenePoints = new MatOfPoint2f();
        goodScenePoints.fromList(goodScenePointsList);

        // 计算单应性矩阵，根据最佳参考图像和场景图片的特征点（需要描述）
        Mat homography = Calib3d.findHomography(goodReferencePoints, goodScenePoints);
        /**
         * 根据单应性矩阵对参考图像帧进行透视变换，将2D场景转换成3D
         * 保存在 mCandidateSceneCorners
         */
        Core.perspectiveTransform(mReferenceCorners,
                mCandidateSceneCorners, homography);
        // 对mCandidateSceneCorners进行类型转换
        mCandidateSceneCorners.convertTo(mIntSceneCorners,
                CvType.CV_32S);
        // 输入数据（四边形）必须是凸面

        mSceneCorners = new Mat(4, 1, CvType.CV_32FC2);

        if (Imgproc.isContourConvex(mIntSceneCorners)) {
            mCandidateSceneCorners.copyTo(mSceneCorners);
        }
    }

    protected void draw(Mat src, Mat dst) {
        // 如果没有找到标志，在左上角绘制标志图片的缩影
//        if (mSceneCorners.height() < 4) {
        // The target has not been found.

        // Draw a thumbnail of the target in the upper-left
        // corner so that the user knows what it is.

        int height = mReferenceImage.height();
        int width = mReferenceImage.width();
        int maxDimension = Math.min(dst.width(),
                dst.height()) / 2;
        double aspectRatio = width / (double) height;
        if (height > width) {
            height = maxDimension;
            width = (int) (height * aspectRatio);
        } else {
            width = maxDimension;
            height = (int) (width / aspectRatio);
        }
        Mat dstROI = dst.submat(0, height / 2, 0, width / 2);
        Imgproc.resize(mReferenceImage, dstROI, dstROI.size(), 0.0, 0.0, Imgproc.INTER_AREA);

        for (Point rIp : goodReferencePointsList) {
            Imgproc.circle(mReferenceImage, new Point(rIp.x, rIp.y), 5, new Scalar(255, 0, 0));
        }


        for (Point sIp : goodScenePointsList) {
            Imgproc.circle(dst, new Point(sIp.x, sIp.y), 5, new Scalar(0, 255, 0));
        }

//        }

        // 找到标志之后绘制标志边框，参考图片四个角点经过透视变换转换成模版图片中的四个角点
        // 这样就完成了！
        // Outline the found target in green.
        Imgproc.line(dst, new Point(mSceneCorners.get(0, 0)),
                new Point(mSceneCorners.get(1, 0)), mLineColor, 2);
        Imgproc.line(dst, new Point(mSceneCorners.get(1, 0)),
                new Point(mSceneCorners.get(2, 0)), mLineColor, 2);
        Imgproc.line(dst, new Point(mSceneCorners.get(2, 0)),
                new Point(mSceneCorners.get(3, 0)), mLineColor, 2);
        Imgproc.line(dst, new Point(mSceneCorners.get(3, 0)),
                new Point(mSceneCorners.get(0, 0)), mLineColor, 2);
    }

    public void stop() {
        canProcess = false;
    }
}

package com.example.pan.mydemo.adapter;

import android.content.Context;

import com.cus.pan.library.utils.FileUtils;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Subdiv2D;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAN on 2016/11/14.
 */

public class MakeDelaunayForFaceDetectionFilter implements DetectionFilter {

    int CenterX=304;
    int CenterY=276;
    //人臉輪廓(眼,鼻,眉,嘴巴)68個關鍵點座標
    float[][] faceData={
            {207, 242},
            {210, 269},
            {214, 297},
            {220, 322},
            {229, 349},
            {243, 373},
            {261, 394},
            {282, 412},
            {308, 416},
            {334, 408},
            {356, 388},
            {374, 367},
            {388, 344},
            {397, 319},
            {403, 291},
            {405, 263},
            {408, 235},
            {221, 241},
            {232, 225},
            {250, 219},
            {270, 219},
            {289, 223},
            {320, 222},
            {339, 216},
            {358, 215},
            {375, 220},
            {387, 233},
            {304, 240},
            {304, 259},
            {304, 277},
            {304, 296},//center
            {281, 311},
            {292, 312},
            {303, 314},
            {315, 310},
            {326, 307},
            {243, 247},
            {254, 240},
            {266, 239},
            {276, 245},
            {266, 247},
            {254, 249},
            {332, 243},
            {343, 236},
            {356, 236},
            {367, 242},
            {356, 245},
            {344, 245},
            {263, 346},
            {278, 341},
            {293, 336},
            {303, 340},
            {315, 335},
            {331, 338},
            {348, 342},
            {332, 353},
            {318, 360},
            {305, 362},
            {294, 361},
            {279, 356},
            {270, 347},
            {293, 347},
            {304, 348},
            {316, 345},
            {342, 343},
            {316, 345},
            {304, 348},
            {294, 347}
    };

    private File mCascadeFile;
    private CascadeClassifier faceDetector;

    public MakeDelaunayForFaceDetectionFilter(Context context) {
        String carDetectionFile = "haarcascade_frontalface_alt.xml";
        FileUtils.copyFolderFromAssets(context,false,"harr",FileUtils.getCacheDir(context).getAbsolutePath() + "/harr");
        mCascadeFile = new File(FileUtils.getCacheDir(context).getAbsolutePath() + "/harr/", carDetectionFile);
        faceDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
    }

    @Override
    public void apply(Mat rgba, Mat gray, Mat dst) {
        rgba.copyTo(dst);
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(gray, faceDetections);
        for (Rect rect : faceDetections.toArray()) {
            try {
                //使用Haar+adaBoost找出人臉的ROI,再將68點座標做轉換
                int diffX=(rect.x + (int)rect.width/2)-CenterX;
                int diffY=(rect.y + (int)rect.height/2)-CenterY;

                float[][] newFace= HandleFaceKeyPoints(faceData,diffX,diffY);

                //Subdiv2D Delaunay三角劃分-開使處理

                MatOfPoint2f obj=new MatOfPoint2f();
                List<Point> allPoints=new ArrayList<Point>();

                //System.out.println(faceData[0][0]+","+faceData[0][1]);

                for(int j=0;j<68;j++){
                    allPoints.add(new Point(newFace[j][0],newFace[j][1]));
                }
                Rect rectSubdiv2D=new Rect(0,0,gray.width(),gray.height());
                Subdiv2D subdiv=new Subdiv2D(rectSubdiv2D);
                obj.fromList(allPoints);
                subdiv.insert(obj);

                MatOfInt idx=new MatOfInt();
                List<MatOfPoint2f> facetList=new ArrayList<MatOfPoint2f>();
                MatOfPoint2f facetCenters=new MatOfPoint2f();

                subdiv.getVoronoiFacetList(idx, facetList, facetCenters);
                for(int j=0;j<68;j++){
                    Point tmpPoint=allPoints.get(j);
                    Imgproc.circle(dst, tmpPoint,4, new Scalar(0,0,255), -1);
                }
//                MatOfFloat6 triangleList = new MatOfFloat6();
//                subdiv.getTriangleList(triangleList);
//                //System.out.println(triangleList.dump());
//
//                for(int j=0;j<triangleList.rows();j++){
//
//                    double dataU=triangleList.get(j, 0)[0];
//                    double dataV=triangleList.get(j, 0)[1];
//                    double dataW=triangleList.get(j, 0)[2];
//                    double dataX=triangleList.get(j, 0)[3];
//                    double dataY=triangleList.get(j, 0)[4];
//                    double dataZ=triangleList.get(j, 0)[5];
//                    Point p1=new Point(dataU,dataV);
//                    Point p2=new Point(dataW,dataX);
//                    Point p3=new Point(dataY,dataZ);
//
//
//                    Imgproc.line(gray, p1, p2, new Scalar(64,255,128));
//                    Imgproc.line(gray, p2, p3, new Scalar(64,255,128));
//                    Imgproc.line(gray, p3, p1, new Scalar(64,255,128));
//                }

            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    //座標轉換method
    public float[][] HandleFaceKeyPoints(float[][] standard,int shiftX,int shiftY){
        float[][] newFacePoints=new float[68][2];
        for(int i=0;i<68;i++){

            newFacePoints[i][0]=standard[i][0]+shiftX;
            newFacePoints[i][1]=standard[i][1]+shiftY;

        }

        return newFacePoints;

    }
}

#include <jni.h>
#include <string>
#include <vector>
#include <stdio.h>
#include "android/log.h"
#include "opencv2/core.hpp"
#include "opencv2/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"

#define  LOG_TAG    "MyApp"
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
using namespace cv;
using namespace std;
extern "C" {
void clearLiuDing(Mat mask, int &top, int &bottom);
bool bFindLeftRightBound(Mat mask, int &posLeft, int &posRight);

jstring Java_com_example_pan_mydemo_adapter_CarPlateDetectionByC_stringFromJNI(JNIEnv *env, jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


jint Java_com_example_pan_mydemo_adapter_CarPlateDetectionByC_findAndDetectionCarPlate(
        JNIEnv *env, jclass type, jlong rMat, jlong gMat, jlong dMat
) {
    Mat *rgba = (Mat *) rMat;
    Mat *gray = (Mat *) gMat;
    Mat *dst = (Mat *) dMat;
    int preViewWidth = rgba->cols;
    int preViewHeight = rgba->rows;
    //转HSV 用于按颜色识别车牌区域
    Scalar hsvBlueLo(0, 150, 90, 0);
    Scalar hsvBlueHi(40, 255, 210, 255);

    cvtColor(*rgba, *rgba, CV_BGR2HSV_FULL);
    inRange(*rgba, hsvBlueLo, hsvBlueHi, *rgba);

    GaussianBlur(*rgba, *rgba, Size(9, 9), 0, 0);

    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    // 寻找轮廓
    findContours(*rgba, contours, hierarchy, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
    unsigned long contoursSize = contours.size();
    //计算凸包点
    vector<vector<Point> > hull(contoursSize);
    for (int i = 0; i < contoursSize; i++) { convexHull(Mat(contours[i]), hull[i], false); }

    /// 绘出轮廓及其凸包
    Mat drawing = Mat::zeros((*rgba).size(), CV_8UC3);
    for (int z = 0; z < contoursSize; z++) {
        drawContours(drawing, hull, z, Scalar(0, 255, 0), 1, 8, vector<Vec4i>(), 0, Point());
    }
    cvtColor(drawing, drawing, COLOR_RGBA2GRAY);
//    drawing.copyTo(*dst);
    for (int i = 0; i < contoursSize; i++) {
        Rect2f rect = boundingRect(Mat(hull[i]));
        if (rect.width < preViewWidth * 0.02 || rect.height < preViewHeight * 0.02) { //宽小于或高小于 舍去
            continue;
        }
        float borderW, borderH;
        borderW = rect.width;
        borderH = rect.height;
        //在车牌周围画出尺寸
        char text[20];
        sprintf(text, "%d %d %.2f", (int) rect.width, (int) rect.height, borderW / borderH);
        putText(drawing, text, rect.tl(), 0, 1, Scalar(255));

        if (borderW / borderH < 1.5) { //宽高比小于1.5的舍去
            continue;
        }
        Mat carPlate = gray->operator()(rect);
        if (borderW / borderH >= 1.5 && borderW / borderH < 2.8) {  //可能为不是水平拍照 需要矫正

        }

        if (borderW / borderH >= 2.8 && borderW / borderH < 4) { //正规水平蓝色车牌
            threshold(carPlate, carPlate, 0, 255, THRESH_OTSU);
            vector<vector<Point> > carContours;
            vector<Vec4i> hierarchy1;
            //去边框处理
            int plateTop = 0, plateBottom = 0;
            clearLiuDing(carPlate, plateTop, plateBottom);
            int plateLeft = 0, plateRight = 0;
            if (!bFindLeftRightBound(carPlate, plateLeft, plateRight)) {
                plateLeft = 0;
                plateRight = carPlate.cols - 1;
            }
            LOGI("debug size:%.2f %.2f %.2f %.2f %d %d %d %d ",
                 rect.x, rect.x + rect.width, rect.y, rect.y + rect.height,
                 (int) (rect.x) + plateLeft, (int) (rect.x) + plateRight,
                 (int) (rect.y) + plateTop, (int) (rect.y) + plateBottom
            );
            Point2f tl(rect.x + plateLeft, rect.y + plateTop);
            Point2f br(rect.x + plateRight, rect.y + plateBottom);
            Rect measureRect(tl, br);
            //更新carPlate 为裁剪后的
            carPlate = gray->operator()(measureRect);
            //复制carPlate用于查找字母
            Mat tempCarPlate = carPlate.clone();
            //寻找字母
            findContours(tempCarPlate, carContours, hierarchy1, CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE, Point(0, 0));
            tempCarPlate.release();
            unsigned long wordContoursSize = carContours.size();
            for (int j = 0; j < wordContoursSize; j++) {
                Rect2f r2f = boundingRect(Mat(carContours[j]));
                if (r2f.height < borderH * 0.6//高度小于边框6/10
//                            || ir.tl().y < borderH * 0.1 //文字顶部高度距离边框顶部小于 1/10
//                            || ir.br().y > borderH * 0.9 //文字顶部高度距离边框顶部小于 1/10
                        ) {
                    continue;
                }
                if (r2f.width < borderH && r2f.height < borderH * 0.8) {
                    rectangle(carPlate, r2f.tl(), r2f.br(), Scalar(255), 1);
                }
            }
            rectangle(drawing, measureRect.tl(), measureRect.br(), Scalar(255), 2);
            carPlate.copyTo(drawing.operator()(measureRect));
        }
        carPlate.release();
    }
    drawing.copyTo(*dst);
    drawing.release();
    return 0;
}

void clearLiuDing(Mat mask, int &top, int &bottom) {
    const int x = 7;

    for (int i = 0; i < mask.rows / 2; i++) {
        int whiteCount = 0;
        int jumpCount = 0;
        for (int j = 0; j < mask.cols - 1; j++) {
            if (mask.at<char>(i, j) != mask.at<char>(i, j + 1)) jumpCount++;

            if ((int) mask.at<uchar>(i, j) == 255) {
                whiteCount++;
            }
        }
        if ((jumpCount < x && whiteCount * 1.0 / mask.cols > 0.15) ||
            whiteCount < 4) {
            top = i;
        }
    }
    top -= 1;
    if (top < 0) {
        top = 0;
    }

    // ok,找到上下边界

    for (int i = mask.rows - 1; i >= mask.rows / 2; i--) {
        int jumpCount = 0;
        int whiteCount = 0;
        for (int j = 0; j < mask.cols - 1; j++) {
            if (mask.at<char>(i, j) != mask.at<char>(i, j + 1)) jumpCount++;
            if (mask.at<uchar>(i, j) == 255) {
                whiteCount++;
            }
        }
        if ((jumpCount < x && whiteCount * 1.0 / mask.cols > 0.15) ||
            whiteCount < 4) {
            bottom = i;
        }
    }
    bottom += 1;
    if (bottom >= mask.rows) {
        bottom = mask.rows - 1;
    }

    if (top >= bottom) {
        top = 0;
        bottom = mask.rows - 1;
    }
}

bool bFindLeftRightBound(Mat mask, int &posLeft, int &posRight) {

    //从两边寻找边界

    float span = mask.rows * 0.2f;

    //左边界检测

    for (int i = 0; i < mask.cols - span - 1; i += 2) {
        int whiteCount = 0;
        for (int k = 0; k < mask.rows; k++) {
            for (int l = i; l < i + span; l++) {
                if (mask.data[k * mask.step[0] + l] == 255) {
                    whiteCount++;
                }
            }
        }
        if (whiteCount * 1.0 / (span * mask.rows) > 0.36) {
            posLeft = i;
            break;
        }
    }
    span = mask.rows * 0.2f;

    //右边界检测

    for (int i = mask.cols - 1; i > span; i -= 2) {
        int whiteCount = 0;
        for (int k = 0; k < mask.rows; k++) {
            for (int l = i; l > i - span; l--) {
                if (mask.data[k * mask.step[0] + l] == 255) {
                    whiteCount++;
                }
            }
        }

        if (whiteCount * 1.0 / (span * mask.rows) > 0.26) {
            posRight = i;
            break;
        }
    }

    if (posLeft < posRight) {
        return true;
    }
    return false;
}

}

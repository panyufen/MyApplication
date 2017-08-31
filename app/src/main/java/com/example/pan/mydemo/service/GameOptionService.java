package com.example.pan.mydemo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.androidyuan.lib.screenshot.ScreenShotActivity;
import com.androidyuan.lib.screenshot.ShotPreferencesUtils;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.adapter.GameOcrDataBase;
import com.google.gson.Gson;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.KNearest;
import org.opencv.ml.Ml;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.androidyuan.lib.screenshot.Shotter.SCREEN_SHOT_PATH_NAME;
import static org.opencv.core.CvType.CV_32F;

public class GameOptionService extends Service {
    static {
        System.loadLibrary("opencv_java3");
    }

    private Context mContext;
    private String mScreenFileName = "";
    private String mFilePath = "";
    private Mat mImageMat = null;

    //需要将测试样本resize到的宽高
    private final int SAMPLE_WIDTH = 21;
    private final int SAMPLE_HEIGHT = 25;
    //截取的屏幕宽高
    int mScreenWidth = 0;
    int mScreenHeight = 0;

    private android.graphics.Point mRefreshBtnLoc = new android.graphics.Point();

    private Scalar mLowerBound = new Scalar(0);
    private Scalar mUpperBound = new Scalar(0);

    KNearest kNearest = KNearest.create();
    private boolean initStatus = false;
    private GameOcrDataBase gameOcrDataBase;
    Mat results = new Mat();
    Mat neighborResponses = new Mat();
    Mat dist = new Mat();

    List<Wing> mWingLists = new ArrayList<>();
    Handler handler;

    private boolean detectedListenerThreadCanStop = false;
    private boolean needStartDetect = true;
    private Timer mTimer = null;

    public GameOptionService() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        mContext = this;
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        mLowerBound.val[0] = 185;
        mUpperBound.val[0] = 255;
        mLowerBound.val[1] = 20;
        mUpperBound.val[1] = 255;
        mLowerBound.val[2] = 209;
        mUpperBound.val[2] = 255;
        mLowerBound.val[3] = 0;
        mUpperBound.val[3] = 255;

        mWingLists.clear();
        Wing xczy = new Wing();
        xczy.name = "星辰之翼";
        xczy.wingDatas = new int[]{7, 8, 0, 1};
        mWingLists.add(xczy);
        Wing xmzy = new Wing();
        xmzy.name = "夏沫羽织";
        xmzy.wingDatas = new int[]{9, 10, 2, 3};
        mWingLists.add(xmzy);
        Wing lbntzy = new Wing();
        lbntzy.name = "卢比纳特之翼";
        lbntzy.wingDatas = new int[]{11, 12, 13, 14, 0, 1};
        mWingLists.add(lbntzy);

//        Wing lbntzw = new Wing();
//        lbntzw.name = "卢比纳特之尾";
//        lbntzw.wingDatas = new int[]{11, 12, 13, 14, 0, 4};
//        mWingLists.add(lbntzw);

        initOcrTransProgress();

        //启动识别监听 对应的管理程序
        initDetectListenerThread();
    }

    /**
     * 用于开启 截图并识别  的管理线程
     */
    private void initDetectListenerThread() {
        Thread detectedListenerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!detectedListenerThreadCanStop) {
                    if (needStartDetect) {
                        needStartDetect = false;
                        threadSleep(3000);
                        //开始截图并识别
                        startDetected();
                    }
                }
            }
        });
        detectedListenerThread.start();
    }


    private void startDetected() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                LogUtils.i("capture screen start");
                captureScreen();
                threadSleep(4000);
                detecScreenPic();
            }
        }, 0, 30000);


//        Thread detectedThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LogUtils.i("capture screen start");
//                captureScreen();
//                threadSleep(4000);
//                detecScreenPic();
//            }
//        });
//        detectedThread.start();
    }


    /**
     * 读取屏幕截图文件，并进行识别
     */
    private void detecScreenPic() {
        mScreenFileName = ShotPreferencesUtils.getCurrentPicName();
        if (!"0".equals(mScreenFileName)) {
            ShotPreferencesUtils.setCurrentPicName("0");
            if (getExternalFilesDir(SCREEN_SHOT_PATH_NAME) != null) {
                mFilePath = getExternalFilesDir(SCREEN_SHOT_PATH_NAME) + File.separator;
                //将图片读取为Mat
                mImageMat = dealToMat(mFilePath + mScreenFileName);
//                LogUtils.i("image Info " + mImageMat.size() + " " + mImageMat.type());

                //进行识别Mat
                mImageMat = matFilterProcess(mImageMat);
                //保存Mat到文件
            }
        }
    }

    /**
     * 识别mat
     *
     * @param mat
     * @return
     */
    private Mat matFilterProcess(Mat mat) {
        mScreenWidth = mat.width();
        mScreenHeight = mat.height();
        //按钮点击坐标
        mRefreshBtnLoc.x = (int) (mScreenWidth * 0.878);
        mRefreshBtnLoc.y = (int) (mScreenHeight * 0.173);

        //识别每一个区域
        int proTitleW = (int) (mScreenWidth * 0.1282);
        int proTitleH = (int) (mScreenHeight * 0.0417);

        int proTitleX1 = (int) (mScreenWidth * 0.4530);
        int proTitleX2 = (int) (mScreenWidth * 0.7625);
        int proTitleY1 = (int) (mScreenHeight * 0.2694);
        int proTitleY2 = (int) (mScreenHeight * 0.4416);
        int proTitleY3 = (int) (mScreenHeight * 0.61388);
        //左一
        Wing leftW1 = dealProductMat(mat, proTitleX1, proTitleY1, proTitleW, proTitleH);
//        leftW1 = new Wing();
//        leftW1.name = "测试名字";
        if (leftW1 != null) {
            LogUtils.i("matchResult name " + leftW1.name);
            makeToast(leftW1.name);
            toBuyTargetWing(proTitleX1, proTitleY1);
            needStartDetect = true;
            mTimer.cancel();
            return null;
        }
        //左二
        Wing leftW2 = dealProductMat(mat, proTitleX1, proTitleY2, proTitleW, proTitleH);
        if (leftW2 != null) {
            LogUtils.i("matchResult name " + leftW2.name);
            makeToast(leftW2.name);
            toBuyTargetWing(proTitleX1, proTitleY2);
            needStartDetect = true;
            mTimer.cancel();
            return null;
        }

        //左三
        Wing leftW3 = dealProductMat(mat, proTitleX1, proTitleY3, proTitleW, proTitleH);
        if (leftW3 != null) {
            LogUtils.i("matchResult name " + leftW3.name);
            makeToast(leftW3.name);
            toBuyTargetWing(proTitleX1, proTitleY3);
            needStartDetect = true;
            mTimer.cancel();
            return null;
        }

        //右一
        Wing rightW1 = dealProductMat(mat, proTitleX2, proTitleY1, proTitleW, proTitleH);
        if (rightW1 != null) {
            LogUtils.i("matchResult name " + rightW1.name);
            makeToast(rightW1.name);
            toBuyTargetWing(proTitleX2, proTitleY1);
            needStartDetect = true;
            mTimer.cancel();
            return null;
        }

        //右二
        Wing rightW2 = dealProductMat(mat, proTitleX2, proTitleY2, proTitleW, proTitleH);
        if (rightW2 != null) {
            LogUtils.i("matchResult name " + rightW2.name);
            makeToast(rightW2.name);
            toBuyTargetWing(proTitleX2, proTitleY2);
            needStartDetect = true;
            mTimer.cancel();
            return null;
        }

        //右三
        Wing rightW3 = dealProductMat(mat, proTitleX2, proTitleY3, proTitleW, proTitleH);
        if (rightW3 != null) {
            LogUtils.i("matchResult name " + rightW3.name);
            makeToast(rightW3.name);
            toBuyTargetWing(proTitleX2, proTitleY3);
            mTimer.cancel();
            needStartDetect = true;
            return null;
        }

        //如果没有找到翅膀 则 点击刷新
        inputTap(mRefreshBtnLoc.x, mRefreshBtnLoc.y);

//        Mat btnMat = dealBtnMat(mScreenWidth, mScreenHeight, mat);

//识别数字较困难有条件在做
//        Mat timeMat = dealTimeMat(mScreenWidth, mScreenHeight, mat);
//        writeFileByMat(mFilePath + "mat_3" + mScreenFileName, timeMat);
//        Intent intent3 = new Intent(GameOptionService.this, ZoomActivity.class);
//        intent3.putExtra("imageUrl", mFilePath + "mat_3" + mScreenFileName);
//        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent3);

        return mat;
    }


    /**
     * 对产品第一个区域进行识别
     *
     * @param sx
     * @param sy
     * @param pw
     * @param ph
     * @param mat
     * @return
     */
    private Wing dealProductMat(Mat mat, int sx, int sy, int pw, int ph) {
        //裁剪出标题区域
        Rect titleRect = new Rect(sx, sy, pw, ph);
        Mat titleMat = mat.submat(titleRect);
        //转hsv 提取文字
        Mat hsvFontMat = new Mat();
        Imgproc.cvtColor(titleMat, hsvFontMat, Imgproc.COLOR_RGB2HSV_FULL);
        Core.inRange(hsvFontMat, mLowerBound, mUpperBound, hsvFontMat);

        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(1, 5));
        Imgproc.dilate(hsvFontMat, hsvFontMat, element);

//        writeFileByMat(mFilePath + "mat_hsv_" + System.currentTimeMillis() + mScreenFileName, hsvFontMat);
//        Intent intent3 = new Intent(GameOptionService.this, ZoomActivity.class);
//        intent3.putExtra("imageUrl", mFilePath + "mat_hsv_" + mScreenFileName);
//        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent3);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(hsvFontMat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        int contoursSize = contours.size();
        for (int i = 0; i < contoursSize; i++) {
            Imgproc.drawContours(hsvFontMat, contours, i, new Scalar(0, 255, 0));
        }


        List<Rect> contoursMatch = new ArrayList<>();
        for (MatOfPoint cp1 : contours) {
            Rect tempr = Imgproc.boundingRect(cp1);
            if (tempr.height < titleRect.height / 2 || tempr.width < titleRect.height / 2) {//宽或高 小于 图片高度/2的丢掉
                continue;
            }
            contoursMatch.add(tempr);
        }

        //从左到右排序
        for (int j = 0; j < contoursMatch.size(); j++) {
            Rect rectj = contoursMatch.get(j);
            for (int k = j + 1; k < contoursMatch.size(); k++) {
                Rect rectk = contoursMatch.get(k);
                if (rectk.x < rectj.x) {
                    contoursMatch.set(k, rectj);
                    contoursMatch.set(j, rectk);
                    rectj = rectk;
                }
            }
        }

        List<Rect> resultContours = new ArrayList<>();
        //容错处理
        for (int a = 0; a < contoursMatch.size() - 1; a++) {
            Rect recta = contoursMatch.get(a);
            resultContours.add(recta);
            Rect rectb = contoursMatch.get(a + 1);
            if (rectb.x - recta.x > recta.width * 1.5) { //说明中间有个字未之别出
                int addX = recta.x + recta.width;
                Rect rectAdd = new Rect(addX, recta.y, rectb.x - addX, (recta.height + rectb.height) / 2);
                resultContours.add(a + 1, rectAdd);
            }
            if (a == contoursMatch.size() - 2) {
                resultContours.add(contoursMatch.get(contoursMatch.size() - 1));
            }
        }
        int[] detecResult = new int[6];
        int length = resultContours.size() > detecResult.length ? detecResult.length : resultContours.size();
        for (int i = 0; i < length; i++) {
            Rect r = resultContours.get(i);
            //裁剪出產品區域
            Mat rgbFontMat = titleMat.submat(r);
            Mat grayFontMat = new Mat();
            Imgproc.cvtColor(rgbFontMat, grayFontMat, Imgproc.COLOR_RGBA2GRAY);
            //二值化
            Imgproc.threshold(grayFontMat, grayFontMat, 0, 255, Imgproc.THRESH_OTSU);

            Mat sourceWordMat = new Mat(1, SAMPLE_WIDTH * SAMPLE_HEIGHT, CV_32F);
            Mat wordDst = new Mat(SAMPLE_HEIGHT, SAMPLE_WIDTH, CV_32F);
            //将图片缩放到对应尺寸
            Imgproc.resize(grayFontMat, wordDst, wordDst.size(), 0, 0, Imgproc.INTER_AREA);
            wordDst.convertTo(wordDst, CV_32F);

//            writeFileByMat(mFilePath + "mat_" + i + "_" + System.currentTimeMillis() + mScreenFileName, wordDst);

            wordDst = wordDst.reshape(1, SAMPLE_WIDTH * SAMPLE_HEIGHT);
            for (int k = 0; k < SAMPLE_WIDTH * SAMPLE_HEIGHT; k++) {
                double[] data = wordDst.get(k, 0);
                sourceWordMat.put(0, k, data);
            }
            if (initStatus) {//识别库初始化完毕才进行识别
                int result = (int) kNearest.findNearest(sourceWordMat, 1, results, neighborResponses, dist);
//                LogUtils.i("ocrResult " + i + " => " + result + " " + gameOcrDataBase.getResultLabel(result));
                detecResult[i] = result;
            }
        }

        return matchResult(detecResult);
    }

    //匹配识别出的数字数组为对应wing
    private Wing matchResult(int[] results) {
        if (results == null) {
            return null;
        }
        int[] matchBoolean = new int[]{0, 0, 0, 0, 0, 0};
        int matchIndex = -1;
        int matchCount = 0;
        for (int i = 0; i < mWingLists.size(); i++) {
            Wing wing1 = mWingLists.get(i);
            int[] tempData = wing1.wingDatas;
            for (int k = 0; k < results.length; k++) {
                if (k < tempData.length) {
                    if (tempData[k] == results[k]) {
                        matchBoolean[k] = 1;
                        matchCount++;
                    } else {
                        matchCount = 0;
                        matchBoolean[k] = 2;
                        break;
                    }
                    if (k == 0 && matchBoolean[k] == 2) { //如果第一个就不匹配 就跳出
                        break;
                    }
                }
            }
            LogUtils.i("matchResult count " + matchCount + " " + tempData.length + " " + i + " " + new Gson().toJson(results) + " " + new Gson().toJson(tempData) + " " + new Gson().toJson(matchBoolean));
            if (matchCount == tempData.length) {
                matchIndex = i;
                break;
            }
        }
        for (int b : matchBoolean) {
            if (b == 2) {
                return null;
            }
        }
        return matchIndex >= 0 && matchIndex < mWingLists.size() ? mWingLists.get(matchIndex) : null;
    }

    /**
     * 识别到对应商品后需要进行的操作
     * 1.点击对应商品
     * 2.隔2秒在点击对应位置
     * 3.在弹出的窗口中点击 “购买” 按钮
     * 4.在弹出的窗口中点击 “确定” 按钮
     *
     * @param x
     * @param y
     */
    private void toBuyTargetWing(int x, int y) {
        //点击商品分类，进入商品页
        inputTap(x, y);
        //线程休眠2秒
        threadSleep(2000);
        //点击商品
        inputTap(x, y);


// 前期先不做识别等操作
//        //截取屏幕
//        captureScreen();
//        threadSleep(3000);
//        //识别购买按钮
//        detecBuyDi);

        //睡眠2秒
        threadSleep(2000);
        //直接进行购买点击
        int buyBtnX = (int) (mScreenWidth * 0.4609);
        int buyBtnY = (int) (mScreenHeight * 0.7180);
        inputTap(buyBtnX, buyBtnY);
        //睡眠2秒
        threadSleep(2000);
//        //进行确认点击
        int comfirmBtnX = (int) (mScreenWidth * 0.5859);
        int comfirmBtnY = (int) (mScreenHeight * 0.6555);
        inputTap(comfirmBtnX, comfirmBtnY);

    }

    private void detecBuyDialog() {
//        int sx =
    }


    private Mat dealBtnMat(int width, int height, Mat mat) {
        int clipX = (int) (width * 0.867);
        int clipY = (int) (height * 0.145);
        int clipWidth = (int) (width * 0.097);
        int clipHeight = (int) (height * 0.049);
//        LogUtils.i("widthHeight2 " + mScreenWidth + " " + mScreenHeight + " " + clipX + " " + clipY + " " + clipWidth + " " + clipHeight);
        Rect rect = new Rect(clipX, clipY, clipWidth, clipHeight);
        return mat.submat(rect);
    }

    private Mat dealTimeMat(int width, int height, Mat mat) {
        int clipX = (int) (width * 0.718);
        int clipY = (int) (height * 0.145);
        int clipWidth = (int) (width * 0.137);
        int clipHeight = (int) (height * 0.036);
        Rect rect = new Rect(clipX, clipY, clipWidth, clipHeight);
        return mat.submat(rect);
    }


    private Mat dealToMat(String fileName) {
        Mat imageMat = Imgcodecs.imread(fileName);
        return imageMat;
    }

    private boolean writeFileByMat(String path, Mat mat) {
        return Imgcodecs.imwrite(path, mat);
    }


    /**
     * 模拟点击屏幕某个点
     */
    private void inputTap(int x, int y) {
        execShellCmd("input tap " + x + " " + y);
    }

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    private void execShellCmd(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流  
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void threadSleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Ocr训练程序
     */
    private void initOcrTransProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameOcrDataBase = new GameOcrDataBase(mContext);
                initStatus = kNearest.train(gameOcrDataBase.getTrainingDataMat(), Ml.ROW_SAMPLE, gameOcrDataBase.getTrainingLabelsMat());
                Log.i("train", "train 训练 是否成功 " + initStatus);
            }
        }).start();
    }

    /**
     * 截取屏幕
     */
    private void captureScreen() {
        Intent intentIn = new Intent(GameOptionService.this, ScreenShotActivity.class);
        intentIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GameOptionService.this.startActivity(intentIn);
    }


    private void makeToast(final String text) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameOptionService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    class Wing {
        public String name;
        int[] wingDatas;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        detectedListenerThreadCanStop = true;
        LogUtils.i("服务已退出 ");
    }
}

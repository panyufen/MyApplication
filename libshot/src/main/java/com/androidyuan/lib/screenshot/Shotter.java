package com.androidyuan.lib.screenshot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

/**
 * Created by wei on 16-12-1.
 */
public class Shotter {
    public static final String SCREEN_SHOT_PATH_NAME = "screenshot";
    private final SoftReference<Context> mRefContext;
    private ImageReader mImageReader;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private String mLocalUrl = "";

    private OnShotListener mOnShotListener;

    private Context mContext;


    public Shotter(Context context, Intent data) {
        mContext = context;
        this.mRefContext = new SoftReference<>(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK,
                    data);

            mImageReader = ImageReader.newInstance(
                    getScreenWidth(),
                    getScreenHeight(),
                    PixelFormat.RGBA_8888,//此处必须和下面 buffer处理一致的格式 ，RGB_565在一些机器上出现兼容问题。
                    1);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void virtualDisplay() {

        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                Resources.getSystem().getDisplayMetrics().densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

    }

    public void startScreenShot(OnShotListener onShotListener, String loc_url) {
        mLocalUrl = loc_url;
        startScreenShot(onShotListener);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void startScreenShot(OnShotListener onShotListener) {

        mOnShotListener = onShotListener;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            virtualDisplay();

            Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Image image = mImageReader.acquireLatestImage();

                                        AsyncTaskCompat.executeParallel(new SaveTask(), image);
                                    }
                                },
                    300);

        }

    }


    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(Image... params) {

            if (params == null || params.length < 1 || params[0] == null) {

                return null;
            }

            Image image = params[0];

            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                    Bitmap.Config.ARGB_8888);//虽然这个色彩比较费内存但是 兼容性更好
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            File fileImage = null;
            if (bitmap != null) {
                try {

                    if (TextUtils.isEmpty(mLocalUrl)) {
//                        mLocalUrl = getContext().getExternalFilesDir(SCREEN_SHOT_PATH_NAME).getAbsoluteFile()
//                                + "/" + SystemClock.currentThreadTimeMillis() + ".png";
                        mLocalUrl = getContext().getExternalFilesDir(SCREEN_SHOT_PATH_NAME).getAbsoluteFile()
                                + "/" + System.currentTimeMillis() + ".png";
                    }
                    fileImage = new File(mLocalUrl);

                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }
            }

            if (fileImage != null) {
                return bitmap;
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }

            if (mVirtualDisplay != null) {
                mVirtualDisplay.release();
            }

            if (mOnShotListener != null) {
                File temp = new File(mLocalUrl);
                mOnShotListener.onFinish(temp.getName());
            }

        }
    }


    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getContext().getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);
    }

    private Context getContext() {
        return mRefContext.get();
    }


    private int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        return dm.widthPixels > 1280 ? 1280 : dm.widthPixels;
    }

    private int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowMgr = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        windowMgr.getDefaultDisplay().getRealMetrics(dm);
        return dm.heightPixels > 720 ? 720 : dm.heightPixels;
    }


    // a  call back listener
    public interface OnShotListener {
        void onFinish(String fileName);
    }
}

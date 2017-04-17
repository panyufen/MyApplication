package com.example.pan.mydemo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.pan.mydemo.R;

/**
 * Created by PAN on 2016/9/14.
 */
public class DrawFingerView extends View {

    private Bitmap mBitmap;
    private Paint mPaint;
    private Matrix matrix;

    public DrawFingerView(Context context) {
        this(context, null);
    }

    public DrawFingerView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public DrawFingerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        Bitmap tempBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.finger_view);
        int mBitmapWidth = tempBitmap.getWidth();
        int mBitmapHeight = tempBitmap.getHeight();
        mBitmap = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(tempBitmap, 0, 0, mPaint);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
    }

    /**
     * Pass the touch screen motion event down to the target view, or this
     * view if it is the target.
     *
     * @param event The motion event to be dispatched.
     * @return True if the event was handled by the view, false otherwise.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int x = (int) event.getX();
        final int y = (int) event.getY();
        Log.i("touch", x + " " + y + " " + event.getAction());
        //填色
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            fillColorArea(x, y);
        }
        return true;

    }

    private void fillColorArea(int x, int y) {
        Bitmap bm = mBitmap;

        int pixel = bm.getPixel(x, y);

        int newColor = Color.RED;

        int w = bm.getWidth();
        int h = bm.getHeight();
        //拿到该bitmap的颜色数组
        int[] pixels = new int[w * h];
        bm.getPixels(pixels, 0, w, 0, 0, w, h);
        //填色
        fillColor(pixels, w, h, pixel, newColor, x, y);
        //重新设置bitmap
        bm.setPixels(pixels, 0, w, 0, 0, w, h);
        postInvalidate();
    }

    private void fillColor(int[] pixels, int w, int h, int pixel, int newColor, int x, int y) {


        int index = w * y + x;

        Log.i("color ", index + " " + pixels[index] + " " + newColor + " " + pixels.length);

        int aboveRowIndex1 = w * (y - 1) + x;
        int belowRowIndex1 = w * (y + 1) + x;

        int aboveRowIndex2 = w * (y - 2) + x;
        int belowRowIndex2 = w * (y + 2) + x;

        int aboveRowIndex3 = w * (y - 3) + x;
        int belowRowIndex3 = w * (y + 3) + x;


        fillAreaColor(pixels, newColor, index);
        fillAreaColor(pixels, newColor, aboveRowIndex1);
        fillAreaColor(pixels, newColor, belowRowIndex1);

        fillAreaColor(pixels, newColor, aboveRowIndex2);
        fillAreaColor(pixels, newColor, belowRowIndex2);

        fillAreaColor(pixels, newColor, aboveRowIndex3);
        fillAreaColor(pixels, newColor, belowRowIndex3);


    }


    private void fillAreaColor(int[] pixels, int color, int i) {
        pixels[i - 3] = color;
        pixels[i - 2] = color;
        pixels[i - 1] = color;
        pixels[i] = color;
        pixels[i + 1] = color;
        pixels[i + 2] = color;
        pixels[i + 3] = color;
    }

}

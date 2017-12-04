package com.example.pan.mydemo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by PAN on 2016/9/9.
 */
public class ConfirmView extends View {



    Path mSuccessPath;
    PathMeasure mPathMeasure;
    Paint mPaint;
    RectF oval;
    float STROKEN_WIDTH = 5;

    public ConfirmView(Context context) {
        super(context);
        init();
    }

    public ConfirmView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConfirmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mSuccessPath = new Path();
        mPathMeasure = new PathMeasure(mSuccessPath, false);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF0099CC);
        mPaint.setStrokeWidth(STROKEN_WIDTH);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        oval = new RectF();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);

        Path path1 = new Path();
        Path path2 = new Path();
        Path path3 = new Path();
        Path path4 = new Path();

        path1.addCircle(0, 0, 200, Path.Direction.CW);
        path2.addRect(0, -200, 200, 200, Path.Direction.CW);
        path3.addCircle(0, -100, 100, Path.Direction.CW);
        path4.addCircle(0, 100, 100, Path.Direction.CCW);
//
//
        path1.op(path2, Path.Op.DIFFERENCE);
        path1.op(path3, Path.Op.UNION);
        path1.op(path4, Path.Op.DIFFERENCE);

        RectF rectF = new RectF();
        path1.computeBounds(rectF,true);

        Log.i("rectF ",rectF.left+" "+rectF.top+" "+rectF.bottom+" "+rectF.right);

        canvas.drawPath(path1, mPaint);
//        canvas.drawPath(path2, mPaint);
//        canvas.drawPath(path3, mPaint);
//        canvas.drawPath(path4, mPaint);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawRect(rectF,paint);


    }
}

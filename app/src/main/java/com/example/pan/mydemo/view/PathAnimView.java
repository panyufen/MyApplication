package com.example.pan.mydemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

/**
 * Created by PAN on 2016/9/9.
 */
public class PathAnimView extends View {
    public PathAnimView(Context context) {
        super(context);
        init();
    }

    public PathAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private Point startPoint = new Point();

    private Point topPoint=new Point();
    private Point topPoint1=new Point();
    private Point topPoint2=new Point();

    private Point endPoint=new Point();

    private Point currentPoint = new Point(0,0);

    Rect rect = new Rect(10,10,40,20);






    private Path path = new Path();

    private PathMeasure pathMeasure;

    private Paint paint;


    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);


    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        startPoint.x= 50;
        startPoint.y = getMeasuredHeight()-50;

        topPoint.x = getMeasuredWidth()/5;
        topPoint.y= getMeasuredHeight()*2/4;

        topPoint1.x = getMeasuredWidth()*2/4;
        topPoint1.y= getMeasuredHeight()/4;

        topPoint2.x = getMeasuredWidth()*4/5;
        topPoint2.y= getMeasuredHeight()*2/4;

        endPoint.x = getMeasuredWidth()-50;
        endPoint.y = getMeasuredHeight()-30;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        canvas.drawLine(startPoint.x,startPoint.y,topPoint.x,topPoint.y,paint);

        canvas.drawLine(topPoint.x,topPoint.y,topPoint1.x,topPoint1.y,paint);

        canvas.drawLine(topPoint1.x,topPoint1.y,topPoint2.x,topPoint2.y,paint);

        canvas.drawLine(topPoint2.x,topPoint2.y,endPoint.x,endPoint.y,paint);


        rect.left = currentPoint.x-40;
        rect.top = currentPoint.y-10;
        rect.right = currentPoint.x;
        rect.bottom = currentPoint.y+10;

        canvas.drawRect(rect,paint);

        canvas.drawPath(path,paint);

    }

    public void startAnim(int s){
        path.moveTo(startPoint.x, startPoint.y);

        path.cubicTo(topPoint.x,topPoint.y,topPoint1.x,topPoint1.y,endPoint.x,endPoint.y);

        pathMeasure = new PathMeasure(path,false);
        ValueAnimator valueAnimator  = ValueAnimator.ofFloat(0,pathMeasure.getLength());
        valueAnimator.setDuration(s);
        valueAnimator.setInterpolator(new BounceInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float)animation.getAnimatedValue();
                float[] tempPoint = new float[2];

                float[] tan = new float[2];

                pathMeasure.getPosTan(value,tempPoint,tan);

                float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);



                currentPoint.x = (int)tempPoint[0];
                currentPoint.y = (int)tempPoint[1];
                Log.i("path",currentPoint.x+"   "+currentPoint.y+" "+degrees);
                postInvalidate();
            }
        });

    }

}

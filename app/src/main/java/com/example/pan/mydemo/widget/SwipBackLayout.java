package com.example.pan.mydemo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Notice:
 *  在页面最外层添加 SwipBackLayout
 *  设置setSwipBackListener 在回调中 finish页面
 *
 *  页面 需要设置 背景透明
 *  <style name="transparent" parent="AppTheme">
 *      <item name="android:windowBackground">@android:color/transparent</item>
 *      <item name="android:windowIsTranslucent">true</item>
 *  </style>
 *
 *  页面关闭时 无切换动画
 *  overridePendingTransition(0,0);
 *
 * Created by Pan on 2016/6/22.
 */
public class SwipBackLayout extends RelativeLayout {

    private String TAG= "DragLayout ";

    private Context mContext;

    private View markBg;

    private ViewGroup container;

    private ViewDragHelper viewDragHelper;

    //滑动到1/edgeRange 关闭页面
    private int edgeRange = 4;

    private float markBgAlpha = 0.7f;
//    private float markMinAlpha = 0.3f;

    private OnSwipListener onSwipListener;

    private Point point = new Point();

    private int mLeft;


    public SwipBackLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {

                Log.i(TAG,"tryCaptureView "+child+" "+pointerId);
                return false;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                mLeft = left>0?left:0;
                float range = 1.0f-(float)mLeft/((float)getWidth()/edgeRange);
                range = range>0?range:0;
                markBg.setAlpha(markBgAlpha*range);

                Log.i(TAG,"clampViewPositionHorizontal "+" "+left+" "+mLeft+" "+mLeft/getWidth()+" "+getWidth()+" alpha "+markBgAlpha*(1f-mLeft/getWidth()));
                return mLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return child.getTop();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                Log.i(TAG,"onViewReleased "+" "+xvel+" "+yvel+" "+container);

                if (releasedChild == container){
                    Log.i(TAG,"onViewReleased "+" "+" "+point.x);
                    if( mLeft >getWidth()/edgeRange ){
                        point.x = getWidth();
                    }
                    viewDragHelper.settleCapturedViewAt(point.x, releasedChild.getTop());
                    invalidate();
                }

            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                super.onEdgeDragStarted(edgeFlags, pointerId);

                Log.i(TAG,"onEdgeDragStarted "+edgeFlags+" "+pointerId);

                viewDragHelper.captureChildView(container, pointerId);


            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 1;
            }
        });
        viewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG,"onFinishInflate");
        markBg = new View(mContext);
        markBg.setBackgroundColor(Color.BLACK);
        markBg.setAlpha(markBgAlpha);
        addView(markBg,0);
        container = (ViewGroup)getChildAt(1);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.i(TAG,"onLayout");
        point.x = container.getLeft();
        point.y = container.getTop();
    }

    @Override
    public void computeScroll() {
        if(viewDragHelper.continueSettling(true)){
            invalidate();
        }else{
            Log.i(TAG,"computeScroll end");
            if( onSwipListener != null && mLeft >getWidth()/edgeRange){
                onSwipListener.onEnd();
            }
        }

    }

    public void setSwipBackListener( OnSwipListener listener ){
        onSwipListener = listener;
    }



    public interface OnSwipListener{
        void onEnd();
    }

}

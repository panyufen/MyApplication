package com.example.pan.mydemo.widget.floatview;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.cus.pan.library.utils.DensityUtils;
import com.example.pan.mydemo.R;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 自定义悬浮球
 * 可自由拖动，松手的时候悬浮球靠边，靠左边还是右边取决于松手的瞬间悬浮球位于左半屏幕还是右半屏幕；
 * 靠边后如果悬浮球没有被触摸，3000ms后变暗，如果没有被触摸，600ms后悬浮球隐藏一部分
 */
public class FloatView extends android.support.v7.widget.AppCompatImageView {
    private float mX;
    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private float xStart;
    private NoDuplicateClickListener mClickListener;
    private WindowManager windowManager;
    // 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
    private LayoutParams windowManagerParams;
    //保存当前是否为移动模式
    private boolean isMove = false;
    //保存悬浮球在左边还是右边
    private boolean isRight = false;
    //是否触摸悬浮窗
    private boolean isTouch = false;
    //第一个定时器是否取消
    private boolean isCancel;
    //是否靠边隐藏
    private boolean isHide;
    //第二个定时器是否取消
    private boolean isSecondCancel;
    //手指是否离开悬浮球
    private boolean isUp;
    private boolean canClick = true;
    //默认的悬浮球
    private int defaultResource;
    //变暗的悬浮球
    private int darkResource;
    //变暗而且只有右半部分的悬浮球
    private int leftResource;
    //变暗而且只有左半部分的悬浮球
    private int rightResource;


    private static final int KEEP_TO_SIDE = 0;
    private static final int HIDE = 1;
    private static final int MOVE_SLOWLY = 2;

    private FloatViewPreferebceManager mPreferenceManager = null;
    private Context context;

    private Timer timer;
    private TimerTask timerTask;
    private Timer secondTimer;
    private TimerTask secondTask;

    private View image;
    private int count;
    //松开手悬浮球移动的频率
    private static final int FREQUENCY = 16;
    //松开手后悬浮球移动的步数
    private int step;
    //松开手后悬浮球移动的时间 3s
    private int stepTime = 3000;

    public FloatView(Context context, LayoutParams windowManagerParams, WindowManager windowManager) {
        super(context);
        this.context = context;
        image = this;
        this.windowManager = windowManager;

        defaultResource = R.drawable.float_light;
        darkResource = R.drawable.float_dark;
        leftResource = R.drawable.float_dark_left;
        rightResource = R.drawable.float_dark_right;

        this.windowManagerParams = windowManagerParams;
        isMove = false;

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏，规定移动的步数最多为20步
            step = 10;
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏，规定移动的步数最多为12步
            step = 6;
        }
        statusBarHeight = getStatusHeight(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mPreferenceManager = new FloatViewPreferebceManager(context);
//		windowManagerParams.type = LayoutParams.TYPE_PHONE;
        //设置window type, 级别太高，dialog弹不出来,设置为LAST_APPLICATION_WINDOW并不需要权限：SYSTEM_ALERT_WINDOW
        windowManagerParams.type = LayoutParams.TYPE_TOAST;
        windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

        // 设置Window flag
        windowManagerParams.flags = windowManagerParams.flags |
                LayoutParams.FLAG_NOT_FOCUSABLE |
                LayoutParams.FLAG_NOT_TOUCH_MODAL;
//				LayoutParams.FLAG_FULLSCREEN ;
//				|LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        /*
		 * 注意，flag的值可以为：
		 * LayoutParams.FLAG_NOT_TOUCH_MODAL 	不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE   	不可聚焦
		 * LayoutParams.FLAG_NOT_TOUCHABLE   	不可触摸
		 * LayoutParams.FLAG_FULLSCREEN      	窗口显示时，隐藏所有的屏幕装饰（例如状态条）。使窗口占用整个显示区域。
		 * LayoutParams.FLAG_LAYOUT_NO_LIMITS  	允许窗口扩展到屏幕之外 
		 */
        // 调整悬浮窗口至左上角，便于调整坐标
        windowManagerParams.gravity = Gravity.LEFT | Gravity.TOP;

        // 以屏幕左上角为原点，设置x、y初始值
        windowManagerParams.x = (int) mPreferenceManager.getFloatX();
        windowManagerParams.y = (int) mPreferenceManager.getFloatY();
        // 设置悬浮窗口长宽数据
        windowManagerParams.width = LayoutParams.WRAP_CONTENT;
        windowManagerParams.height = DensityUtils.dip2px(getContext(), 46);
        isRight = mPreferenceManager.isDisplayRight();
        setImageResource(defaultResource);
        setScaleType(ScaleType.FIT_CENTER);
        startTimerCount();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case KEEP_TO_SIDE:
                    setImageResource(darkResource);
                    cancelTimerCount();
                    startSecondTimerCount();
                    break;
                case HIDE:
                    cancelSecondTimerCount();
                    if (isRight) {
                        isHide = true;
                        setImageResource(rightResource);
                    } else {
                        setImageResource(leftResource);
                    }
                    break;
                case MOVE_SLOWLY:
                    if (j == count + 1) {
                        canClick = true;
                    }
                    //根据悬浮球离最近的屏幕边缘的距离来定移动的步数
                    count = (int) (2 * step * Math.abs(distance) / screenWidth); //count/step = distance/(screenWidth/2)


                    if (j <= count) {
                        windowManagerParams.x = (int) (xStart - j * distance / count);
                        windowManager.updateViewLayout(image, windowManagerParams); // 刷新显示
                        j++;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(MOVE_SLOWLY);
                            }
                        }, FREQUENCY);
                    }
                    break;
            }
        }

        ;
    };
    private int statusBarHeight;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isTouch = true;
        isUp = false;
        xStart = 0;
        //System.out.println("statusBarHeight:"+statusBarHeight);
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY(); // statusBarHeight是系统状态栏的高度

//		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
                // 获取相对View的坐标，即以此View左上角为原点
                isMove = false;
                mTouchX = event.getX();
                mTouchY = event.getY();
                cancelTimerCount();
                cancelSecondTimerCount();
                break;
            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                int xMove = Math.abs((int) (event.getX() - mTouchX));
                int yMove = Math.abs((int) (event.getY() - mTouchY));
                if (xMove > 5 || yMove > 5) {
                    //x轴或y轴方向的移动距离大于5个像素，视为拖动，否则视为点击
                    isMove = true;
                    setImageResource(defaultResource);
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                isTouch = false;
                float halfScreen = screenWidth / 2;
                if (isMove) {
                    isUp = true;
                    mX = mTouchX;
                    isMove = false;
                    if (x <= halfScreen) {
                        xStart = x - mTouchX;
                        x = 0;
                        isRight = false;
                    } else {
                        xStart = x;
                        x = screenWidth + mTouchX + image.getWidth();//为了保证悬浮球靠边隐藏，而且在极限情况下（横屏）不会从屏幕右边突然跳到屏幕左边
                        isRight = true;
                    }
                    updateViewPosition();
                    mPreferenceManager.setFloatX(x);
                    mPreferenceManager.setFloatY(y - mTouchY - statusBarHeight);
                    mPreferenceManager.setDisplayRight(isRight);
                    startTimerCount();

                } else {
                    setImageResource(defaultResource);
                    if (mClickListener != null && canClick) {
                        mClickListener.onClick(this);
                    }
                }
                mTouchX = mTouchY = 0;
                break;
        }
        return true;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setNoDuplicateClickListener(NoDuplicateClickListener l) {
        this.mClickListener = l;
    }

    private int j;
    private float distance;
    private int screenWidth;

    private void updateViewPosition() {
        if (isUp) {
            canClick = false;
            //松开手后，悬浮球靠边速度太快，做了个延时，使靠边这个过程更平滑
            distance = xStart - x;
            j = 0;
            windowManagerParams.y = (int) (y - mTouchY - statusBarHeight);
//			windowManagerParams.windowAnimations = android.R.anim.slide_in_left;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(MOVE_SLOWLY);
                }
            }, FREQUENCY);

        } else {
            windowManagerParams.x = (int) (x - mTouchX);
            windowManagerParams.y = (int) (y - mTouchY - statusBarHeight);
            windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
        }
    }

    //开启第一个定时器
    public void startTimerCount() {
        isCancel = false;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isTouch && !isCancel) {
                    handler.sendEmptyMessage(KEEP_TO_SIDE);
                }
            }
        };
        timer.schedule(timerTask, 3000);
    }

    //关闭第一个定时器
    public void cancelTimerCount() {
        isCancel = true;
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    //开启第二个定时器
    public void startSecondTimerCount() {
        isSecondCancel = false;
        secondTimer = new Timer();
        secondTask = new TimerTask() {
            @Override
            public void run() {
                if (!isSecondCancel) {
                    handler.sendEmptyMessage(HIDE);
                }
            }
        };
        secondTimer.schedule(secondTask, 600);
    }

    //关闭第二个定时器
    public void cancelSecondTimerCount() {
        isSecondCancel = true;
        if (secondTimer != null) {
            secondTimer.cancel();
            secondTimer = null;
        }
        if (secondTask != null) {
            secondTask.cancel();
            secondTask = null;
        }
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}

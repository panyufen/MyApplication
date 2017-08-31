package com.example.pan.mydemo.view.floatview;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.service.FloatDialogService;
import com.example.pan.mydemo.service.GameOptionService;

/**
 * 悬浮球管理类
 */
public class FloatManager {
    private LayoutParams windowParams = new LayoutParams();
    private FloatView floatView = null;
    private boolean isDisplay = false;
    private FloatDialogService floatDialogService;
    private FloatViewPreferebceManager mPreferenceManager;
    private boolean isRight;
    private boolean isHide;
    private View popupView;
    private PopupWindow popupWindow;
    private FloatViewSecond floatViewSecond;

    //点击悬浮球，popupWindow显示的延迟时长。这么做的原因是：当悬浮球靠边隐藏后，点击悬浮球，悬浮球先由一半变为完整的一个，
    //同时会加载popupWindow的enter动画，这两个过程同时发生，特别当悬浮球在右边的时候会暴露出有一个悬浮球盖在popupWindow上，
    //而popupWindow下面还有一个悬浮球,所以在这设置一个延时。
    private int postDelayedTime = 20;

    private WindowManager windowManager;

    private boolean isClickedStart = false;

    public FloatManager(FloatDialogService floatDialogService) {
        this.floatDialogService = floatDialogService;
        mPreferenceManager = new FloatViewPreferebceManager(floatDialogService);
        windowManager = (WindowManager) floatDialogService.getSystemService(Context.WINDOW_SERVICE);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:

                    break;
            }
        }
    };

    public void createView() {
        if (isDisplay)
            return;
        LogUtils.i("float view create");
        floatView = new FloatView(floatDialogService, windowParams, windowManager);
        floatView.setNoDuplicateClickListener(floatViewClick);
        // 显示FloatView悬浮球
        windowManager.addView(floatView, windowParams);
        isDisplay = true;
    }

    public void removeView() {
        if (!isDisplay)
            return;
        floatView.cancelTimerCount();
        if (floatViewSecond != null) {
            dismissPopupWindow();
            windowManager.removeView(floatViewSecond);
            floatViewSecond = null;
        }
        // 在程序退出(Activity销毁）时销毁悬浮窗口
        windowManager.removeView(floatView);

        isDisplay = false;
    }

    private NoDuplicateClickListener floatViewClick = new NoDuplicateClickListener() {

        public void onNoDulicateClick(View v) {
            isHide = floatView.isHide();
            isRight = mPreferenceManager.isDisplayRight();
            //如果悬浮球处于隐藏状态（暗，一半），将延时时长设为100ms，默认20ms
            if (isHide) {
                postDelayedTime = 100;
            } else {
                postDelayedTime = 20;
            }
            isHide = false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //点击悬浮球，在原位置再盖一个一样的悬浮球，遮住一部分popupWindow，这样弹出popupWindow就像从悬浮球里面抽出来的一样。一切都是为了效果。。。
                    floatViewSecond = new FloatViewSecond(floatDialogService, windowParams, windowManager);
                    floatViewSecond.setOnClickListener(new NoDuplicateClickListener() {
                        @Override
                        public void onNoDulicateClick(View v) {
                            //最外面的悬浮球不能拖动，点击它，销毁掉popupWindow，然后再移除掉最外层的悬浮球
                            dismissPopupWindow();
                            //这里移除最外层的悬浮球做一个延迟，因为popupWindow的exit动画duration设置的是150ms
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    floatView.startTimerCount();
                                    if (floatViewSecond != null) {
                                        windowManager.removeView(floatViewSecond);
                                    }
                                    floatViewSecond = null;
                                }
                            }, 200);
                        }
                    });
                    windowManager.addView(floatViewSecond, windowParams);
                    if (isRight) {
                        popupView = View.inflate(floatDialogService, R.layout.float_popup_window_right, null);
                    } else {
                        popupView = View.inflate(floatDialogService, R.layout.float_popup_window_left, null);
                    }
                    TextView floatMenuItemFirstTv = (TextView) popupView.findViewById(R.id.float_menu_item_first_tv);
                    if (isClickedStart) {
                        floatMenuItemFirstTv.setText("暂停");
                    } else {
                        floatMenuItemFirstTv.setText("开始");
                    }

                    initView();
                    popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    popupWindow.setTouchable(true);
                    popupWindow.setClippingEnabled(false);//允许弹出窗口超出屏幕范围
                    if (isRight) {
                        //为popupWindow设置进入和退出的动画效果，我记得明明可以在代码里写动画的，但是我试了好几次都失败了，只能用xml的形式做动画效果
                        popupWindow.setAnimationStyle(R.style.popupWindowRightAnimation);
                        popupWindow.showAtLocation(floatView, Gravity.RIGHT, 0, 0);
                    } else {
                        popupWindow.setAnimationStyle(R.style.popupWindowAnimation);
                        popupWindow.showAtLocation(floatView, Gravity.LEFT, 0, 0);
                    }
                }
            }, postDelayedTime);
            hideMenuDelay(3000);
        }

    };

    private void dismissPopupWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 程序进入后台或者退出事调用
     */
    public void cancelTimerCount() {
        if (floatView != null) {
            floatView.cancelTimerCount();
            floatView.cancelSecondTimerCount();
        }
    }

    // popupWindow的布局及点击事件
    private void initView() {
        LinearLayout floatMenuItemFirst = (LinearLayout) popupView.findViewById(R.id.float_menu_item_first);
        LinearLayout floatMenuItemClose = (LinearLayout) popupView.findViewById(R.id.float_menu_item_close);
        final TextView floatMenuItemFirstTv = (TextView) popupView.findViewById(R.id.float_menu_item_first_tv);
        floatMenuItemFirst.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                new ShotPreferencesUtils(floatDialogService);
//                ShotPreferencesUtils.setCurrentPicName("22.png");
                if (!isClickedStart) {
                    //启动图像识别服务
                    Intent gameOptIntent = new Intent(floatDialogService, GameOptionService.class);
                    floatDialogService.startService(gameOptIntent);
                    floatMenuItemFirstTv.setText("暂停");
                    isClickedStart = true;
                } else {
                    Intent gameOptIntent = new Intent(floatDialogService, GameOptionService.class);
                    floatDialogService.stopService(gameOptIntent);
                    isClickedStart = false;
                    floatMenuItemFirstTv.setText("开始");
                }
                hideMenuDelay(2000);
            }
        });
        floatMenuItemClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView();
                floatDialogService.stopSelf();
                Intent gameOptIntent = new Intent(floatDialogService, GameOptionService.class);
                floatDialogService.stopService(gameOptIntent);
            }
        });
    }


    /**
     * 延时关闭菜单
     */
    private void hideMenuDelay(int delayTime) {
        //这里移除最外层的悬浮球做一个延迟，因为popupWindow的exit动画duration设置的是150ms
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissPopupWindow();
            }
        }, delayTime - 200);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                floatView.startTimerCount();
                if (floatViewSecond != null) {
                    windowManager.removeView(floatViewSecond);
                }
                floatViewSecond = null;
            }
        }, delayTime);
    }

}

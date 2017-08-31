package com.example.pan.mydemo.view.floatview;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.example.pan.mydemo.R;


/**
 * 悬浮球
 */
public class FloatViewSecond extends android.support.v7.widget.AppCompatImageView {
	// 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
	private LayoutParams windowManagerParams;
	private WindowManager windowManager;
	//保存当前机器人在左边还是右边
	private boolean  isRight = false;
	private int defaultResource ;
	private FloatViewPreferebceManager mPreferenceManager = null;
	private Context context;

	public FloatViewSecond(Context context, LayoutParams windowManagerParams, WindowManager windowManager) {
		super(context);
		this.context = context;
		defaultResource = R.mipmap.float_no_transparent;
		this.windowManager = windowManager;
		this.windowManagerParams = windowManagerParams;
		mPreferenceManager = new FloatViewPreferebceManager(context);
//		windowManagerParams.type = LayoutParams.TYPE_PHONE;
		windowManagerParams.type = LayoutParams.TYPE_TOAST; // 设置window type, 级别太高，dialog弹不出来
		windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		
		// 设置Window flag
		windowManagerParams.flags = windowManagerParams.flags|
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
		windowManagerParams.x = (int)mPreferenceManager.getFloatX();
		windowManagerParams.y = (int)mPreferenceManager.getFloatY();
		// 设置悬浮窗口长宽数据
		windowManagerParams.width = LayoutParams.WRAP_CONTENT;
		windowManagerParams.height = LayoutParams.WRAP_CONTENT;
		isRight = mPreferenceManager.isDisplayRight();
		setImageResource(defaultResource);
	}
}

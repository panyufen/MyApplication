package com.example.pan.mydemo.view.floatview;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FloatViewPreferebceManager {
	private Context mContext;

	public FloatViewPreferebceManager(Context context) {
		mContext = context;
	}

	/**
	 * get the shared proferences for getting or setting
	 * 
	 * @return
	 */
	private SharedPreferences getSharedPreferences() {
		return mContext.getSharedPreferences(FloatViewConfig.PERFERENCE_NAME,
				Context.MODE_PRIVATE);
	}
	/**
	 * get the editor for saving the key value
	 * 
	 * @return
	 */
	private Editor getEditer() {
		return getSharedPreferences().edit();
	}

	public float getFloatX() {
		SharedPreferences swg = getSharedPreferences();

		return swg.getFloat(FloatViewConfig.PREF_KEY_FLOAT_X, 0f);
	}

	public void setFloatX(float x) {
		Editor editor = getEditer();
		editor.putFloat(FloatViewConfig.PREF_KEY_FLOAT_X, x);
		editor.commit();
	}

	public float getFloatY() {
		SharedPreferences swg = getSharedPreferences();

		return swg.getFloat(FloatViewConfig.PREF_KEY_FLOAT_Y, 0f);
	}

	public void setFloatY(float y) {
		Editor editor = getEditer();
		editor.putFloat(FloatViewConfig.PREF_KEY_FLOAT_Y, y);
		editor.commit();
	}

	public boolean onlyDisplayOnHome() {
		SharedPreferences swg = getSharedPreferences();

		return swg.getBoolean(FloatViewConfig.PREF_KEY_DISPLAY_ON_HOME, true);
	}

	public void setDisplayOnHome(boolean onlyDisplayOnHome) {
		Editor editor = getEditer();
		editor.putBoolean(FloatViewConfig.PREF_KEY_DISPLAY_ON_HOME, onlyDisplayOnHome);
		editor.commit();
	}

	public boolean isDisplayRight() {
		SharedPreferences swg = getSharedPreferences();

		return swg.getBoolean(FloatViewConfig.PREF_KEY_IS_RIGHT, false);
	}

	public void setDisplayRight(boolean isRight) {
		Editor editor = getEditer();
		editor.putBoolean(FloatViewConfig.PREF_KEY_IS_RIGHT, isRight);
		editor.commit();
	}
}

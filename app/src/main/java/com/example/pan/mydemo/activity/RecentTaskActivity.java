package com.example.pan.mydemo.activity;

import android.app.ActivityManager.TaskDescription;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import com.cus.pan.library.utils.BitmapUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

/**
 * 这个页面会显示在最近任务中，参考文档：
 * https://developer.android.google.cn/guide/components/recents.html
 */
public class RecentTaskActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_task);
        setSupportActionBar(R.id.tool_bar, "RecentTask");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapUtils.drawableToBitmap(getDrawable(R.mipmap.detecimg));
            int color = getResources().getColor(R.color.colorPrimary);
            TaskDescription taskDescription = new TaskDescription("RecentTask", icon,color);
            setTaskDescription(taskDescription);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if( keyCode == KeyEvent.KEYCODE_BACK ){
            finish();
            overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.widget.CircleRectView;

import butterknife.BindView;
import butterknife.OnClick;

public class LoadingActivity extends BaseActivity {

    @BindView(R.id.interupt_btn)
    CircleRectView interuptBtn;

    private Handler handler = new MyHandler();

    private final int MIN_COUNT = 0;
    private int curCount = 3;
    private boolean isInterupted = false;

    private final int UPDATE_TEXT = 1;
    private final int JUMP_MAIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        interuptBtn.setText(String.valueOf(curCount));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (curCount >= MIN_COUNT) {
                    if (isInterupted) {
                        break;
                    }
                    Message message = new Message();
                    message.what = UPDATE_TEXT;
                    message.arg1 = curCount;
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    curCount--;
                }
                Message jumpMsg = new Message();
                jumpMsg.what = JUMP_MAIN;
                handler.sendMessage(jumpMsg);
            }
        }).start();

    }

    @OnClick(R.id.interupt_btn)
    public void onViewClicked() {
        go2main();
        isInterupted = true;
    }


    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT:
                    updateBtnText(String.valueOf(msg.arg1));
                    break;
                case JUMP_MAIN:
                    go2main();
                    break;
            }

        }
    }

    private void updateBtnText(String text) {
        interuptBtn.setText(text);
    }


    private synchronized void go2main() {
        if (!isInterupted) {
            LoadingActivity.this.startActivity(MainActivity.class);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler = null;
    }
}

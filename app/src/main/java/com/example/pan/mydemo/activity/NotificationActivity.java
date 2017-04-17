package com.example.pan.mydemo.activity;

import android.app.NotificationManager;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.broadcast.NotificationBroascastReveiver;

import java.util.Set;

import cn.jiguang.common.ClientConfig;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;

public class NotificationActivity extends BaseActivity {

    NotificationBroascastReveiver notificationBroascastReveiver;
    JPushClient jpushClient = new JPushClient("3398c36ffcf31bbe3cee4926", "a0c66969c5ad3b1d22893887", null, ClientConfig.getInstance());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setSupportActionBar(R.id.tool_bar, "Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((NotificationManager)getSystemService(NOTIFICATION_SERVICE)).cancelAll();

        notificationBroascastReveiver = new NotificationBroascastReveiver();
        IntentFilter filter = new IntentFilter();
        registerReceiver(notificationBroascastReveiver, filter);
        JPushInterface.resumePush(this);
        JPushInterface.setAliasAndTags(this, "pan", null, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Toast.makeText(NotificationActivity.this, "setAliasAndTags Success", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void startSimpleNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_Normal);
    }

    public void startProgressNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_Progress);
    }

    public void startBigTextNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_BigText);
    }

    public void startInBoxNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_Inbox);
    }

    public void startBigPictureNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_BigPicture);
    }


    public void startMediaStyleNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_Media);
    }


    public void startCustomNotify(View v) {
        sendJPush(NotificationBroascastReveiver.TYPE_Custom);
    }

    private void sendJPush(int type) {
        final PushPayload payload = PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.alias("pan"))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert("")
                                .build())
                        .build())
                .setMessage(new Message.Builder().addExtra("type", type).setMsgContent("内容").build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PushResult pushResult = jpushClient.sendPush(payload);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationBroascastReveiver);
    }
}

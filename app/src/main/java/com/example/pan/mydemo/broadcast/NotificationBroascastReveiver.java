package com.example.pan.mydemo.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.NotificationActivity;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;
/**
 * Created by PAN on 2017/4/13.
 */
public class NotificationBroascastReveiver extends BroadcastReceiver {

    private String TAG = "JPush Reveiver";

    public static final int TYPE_Normal = 1;
    public static final int TYPE_Progress = 2;
    public static final int TYPE_BigText = 3;
    public static final int TYPE_Inbox = 4;
    public static final int TYPE_BigPicture = 5;
    public static final int TYPE_Hangup = 6;
    public static final int TYPE_Media = 7;
    public static final int TYPE_Custom = 8;
    private NotificationManager manager;

    private Context mContext;

    private String msg = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        LogUtils.i("jPushreveiver ");
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.i(TAG, "[MyReceiver] 接收Registration Id : " + regId);


        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.i(TAG, "收到了自定义消息。消息内容是：" + msg);
            String json = bundle.getString(JPushInterface.EXTRA_EXTRA);
            MsgType msgType = new Gson().fromJson(json, MsgType.class);
            Log.i(TAG, "消息类型：" + json + " " + msgType.type);

            switch (msgType.type) {
                case TYPE_Normal:
                    showSimpleNotify();
                    break;
                case TYPE_Progress:
                    showProgressNotify();
                    break;
                case TYPE_BigText:
                    showBigTextNotify();
                    break;
                case TYPE_Inbox:
                    showInBoxNotify();
                    break;
                case TYPE_BigPicture:
                    showBigPictureNotify();
                    break;
                case TYPE_Media:
                    showMediaStyleNotify();
                    break;
                case TYPE_Custom:
                    showCustomNotify();
                    break;
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "收到了通知");
            // 在这里可以做些统计，或者做些其他工作

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为

        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }

    }


    public void showSimpleNotify() {
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        //Ticker是状态栏显示的提示
        builder.setTicker("");
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle("标题");
        //第二行内容 通常是通知正文
        builder.setContentText(msg);
        //第三行内容 通常是内容摘要什么的 在低版本机器上不一定显示
        builder.setSubText("这里显示的是通知第三行内容！");
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样，如果设置了ContentInfo则number会被隐藏
        builder.setNumber(1);
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //下拉显示的大图标
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_Normal, intent, 0);
        //点击跳转的intent
        builder.setContentIntent(pIntent);
        //通知默认的声音 震动 呼吸灯
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manager.notify(TYPE_Normal, notification);
    }

    public void showProgressNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        //禁止用户点击删除按钮删除
        builder.setAutoCancel(false);
        //禁止滑动删除
        builder.setOngoing(true);
        //右上角时间显示
        builder.setShowWhen(true);
        builder.setContentTitle("下载中...10%");
        builder.setProgress(100, 10, false);
        builder.setContentInfo("10%");
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_Progress, intent, 0);
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        manager.notify(TYPE_Progress, notification);
    }

    public void showBigTextNotify() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("BigTextStyle");
        builder.setContentText("BigTextStyle演示示例");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        android.support.v4.app.NotificationCompat.BigTextStyle style = new android.support.v4.app.NotificationCompat.BigTextStyle();
        style.bigText("这里是点击通知后要显示的正文，可以换行可以显示很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长");
        style.setBigContentTitle("点击后的标题");
        //SummaryText没什么用 可以不设置
        style.setSummaryText("末尾只一行的文字内容");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_BigText, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manager.notify(TYPE_BigText, notification);
    }

    public void showInBoxNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("InboxStyle");
        builder.setContentText("InboxStyle演示示例");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        android.support.v4.app.NotificationCompat.InboxStyle style = new android.support.v4.app.NotificationCompat.InboxStyle();
        style.setBigContentTitle("BigContentTitle")
                .addLine("第一行，第一行，第一行，第一行，第一行，第一行，第一行")
                .addLine("第二行")
                .addLine("第三行")
                .addLine("第四行")
                .addLine("第五行")
                .setSummaryText("SummaryText");
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_Inbox, intent, 0);
        builder.setContentIntent(pIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        manager.notify(TYPE_Inbox, notification);
    }

    public void showBigPictureNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("BigPictureStyle");
        builder.setContentText("BigPicture演示示例");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        android.support.v4.app.NotificationCompat.BigPictureStyle style = new android.support.v4.app.NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        style.bigPicture(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.sample2));
        builder.setStyle(style);
        builder.setAutoCancel(true);
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_BigPicture, intent, 0);
        //设置点击大图后跳转
        builder.setContentIntent(pIntent);
        Notification notification = builder.build();
        manager.notify(TYPE_BigPicture, notification);
    }


    public void showMediaStyleNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("MediaStyle");
        builder.setContentText("Song Title");
        builder.setSmallIcon(R.mipmap.app_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.app_icon));
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent intent = new Intent(mContext, NotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, TYPE_Media, intent, 0);
        builder.setContentIntent(pIntent);
        //第一个参数是图标资源id 第二个是图标显示的名称，第三个图标点击要启动的PendingIntent
        builder.addAction(R.drawable.ic_star, "", null);
        builder.addAction(R.drawable.ic_star, "", null);
        builder.addAction(R.mipmap.route_play_normal, "", pIntent);
        builder.addAction(R.drawable.ic_star, "", null);
        NotificationCompat.MediaStyle style = new NotificationCompat.MediaStyle();
        style.setMediaSession(new MediaSessionCompat(mContext, "MediaSession",
                new ComponentName(mContext, Intent.ACTION_MEDIA_BUTTON), null).getSessionToken());
        //CancelButton在5.0以下的机器有效
        style.setCancelButtonIntent(pIntent);
        style.setShowCancelButton(true);
        //设置要现实在通知右方的图标 最多三个
        style.setShowActionsInCompactView(2, 3);
        builder.setStyle(style);
        builder.setShowWhen(false);
        Notification notification = builder.build();
        manager.notify(TYPE_Media, notification);
    }


    public void showCustomNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("Notification");
        builder.setContentText("自定义通知栏示例");
        builder.setSmallIcon(R.mipmap.app_icon);
        //builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.push));
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setShowWhen(false);
        NotificationCompat.Style style = new android.support.v4.app.NotificationCompat.Style() {
            @Override
            public void setBuilder(android.support.v4.app.NotificationCompat.Builder builder) {
                super.setBuilder(builder);
            }
        };
        builder.setStyle(style);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_template_custom);
        remoteViews.setTextViewText(R.id.title, "Notification");
        remoteViews.setTextViewText(R.id.text, "song");
//        if(command==CommandNext){
//            remoteViews.setImageViewResource(R.id.btn1,R.drawable.ic_pause_white);
//        }else if(command==CommandPlay){
//            if(playerStatus==StatusStop){
//                remoteViews.setImageViewResource(R.id.btn1,R.drawable.ic_pause_white);
//            }else{
//                remoteViews.setImageViewResource(R.id.btn1,R.drawable.ic_play_arrow_white_18dp);
//            }
//        }
        Intent Intent1 = new Intent(mContext, NotificationActivity.class);
        Intent1.putExtra("command", "1");
        //getService(Context context, int requestCode, @NonNull Intent intent, @Flags int flags)
        //不同控件的requestCode需要区分开 getActivity broadcoast同理
        PendingIntent PIntent1 = PendingIntent.getActivity(mContext, 5, Intent1, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn1, PIntent1);

        Intent Intent2 = new Intent(mContext, NotificationActivity.class);
        Intent2.putExtra("command", "2");
        PendingIntent PIntent2 = PendingIntent.getActivity(mContext, 6, Intent2, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn2, PIntent2);

        Intent Intent3 = new Intent(mContext, NotificationActivity.class);
        Intent3.putExtra("command", "3");
        PendingIntent PIntent3 = PendingIntent.getActivity(mContext, 7, Intent3, 0);
        remoteViews.setOnClickPendingIntent(R.id.btn3, PIntent3);

        builder.setContent(remoteViews);
        Notification notification = builder.build();
        manager.notify(TYPE_Custom, notification);
    }


    class MsgType {
        public int type;
    }
}

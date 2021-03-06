package com.example.pan.mydemo.view;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.cus.pan.library.utils.WhiteListUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.service.FloatDialogService;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.view.customview.CustomViewActivity;
import com.example.pan.mydemo.view.database.DataBaseActivity;
import com.example.pan.mydemo.view.ffmpeg.FFmpegActivity;
import com.example.pan.mydemo.view.materialdesign.MaterialDesignActivity;
import com.example.pan.mydemo.view.opencv.OpenCVActivity;
import com.example.pan.mydemo.view.opengl.OpenGLActivity;
import com.example.pan.mydemo.view.queue.PriorityConQueueActivity;
import com.example.pan.mydemo.view.rxjava.RxJavaActivity;
import com.example.pan.mydemo.view.web.WebIntentActivity;

import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends BaseActivity {

    private ActionBar actionBar;

    private Handler handler;

    private List<String> names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(R.id.tool_bar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            setToolbarNavIconVisible(actionBar, false);
        }
        PermissionGen.needPermission(this, 100,
                new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CALL_PHONE

                }
        );

//        Logger.i(names.size()+" ");

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });


    }


    @PermissionFail(requestCode = 100)
    public void permissionDenied() {
        Toast.makeText(this, "Permission is denied", Toast.LENGTH_LONG).show();
    }

    @PermissionSuccess(requestCode = 100)
    public void permissionGranted() {
        Toast.makeText(this, "All Permissions is granted", Toast.LENGTH_LONG).show();
    }


    private void call() {
//        Intent intent = new Intent(Intent.ACTION_DIAL);
//        Uri data = Uri.parse("tel:" + "10086");
//        intent.setData(data);
//        startActivity(intent);
    }


    public void startAnim(View v) {
        startActivity(AnimActivity.class);
    }

    public void startService(View v) {
        startActivity(ServiceActivity.class);
    }

    public void startVolley(View v) {
        startActivity(VolleyActivity.class);
    }

    public void startOkHttp(View v) {
        startActivity(OkHttpActivity.class);
    }

    public void startWeb(View v) {
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(WebActivity.URL_KEY, "http://blog.csdn.net/angel1hao/article/details/52094475");
        startActivity(intent);
    }

    public void startWebJsBridge(View v) {
        Intent intent = new Intent(this, WebJsBridgeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(WebJsBridgeActivity.URL_KEY, "file:///android_asset/jsbridge/index.html");
        startActivity(intent);
    }

    public void startRxJava(View v) {
        startActivity(RxJavaActivity.class);
    }

    public void startRetrofit(View v) {
        startActivity(RetrofitActivity.class);
    }


    public void startPicasso(View v) {
        startActivity(PicassoActivity.class);
    }

    public void startFresco(View v) {
        startActivity(FrescoActivity.class);
    }

    public void startGlide(View v) {
        startActivity(GlideActivity.class);
    }

    public void startCordova(View v) {
        startActivity(CordovaActivity.class);
    }

    public void startReactNative(View v) {
        startActivity(ReactNativeActivity.class);
    }

    public void startCusView(View v) {
        startActivity(CustomViewActivity.class);
    }


    public void startCheckDev(View v) {
        startActivity(CheckDevActivity.class);
    }

    public void startBaiduMapDemo(View v) {
        startActivity(BaiduMapDemoActivity.class);
    }

    public void startSkinLoader(View v) {
        startActivity(SkinLoaderActivity.class);
    }

    public void startInflaterFactory(View v) {
        startActivity(InflaterFactoryActivity.class);
    }

    public void startAccessibilityService(View v) {
        startActivity(AccessibilityServiceActivity.class);
    }

    public void startBlurring(View v) {
        startActivity(BlurringActivity.class);
    }

    public void startOpenCV(View v) {
        startActivity(OpenCVActivity.class);
    }

    public void startFileReader(View v) {
        startActivity(FileReaderActivity.class);
    }

    public void startNotification(View v) {
        startActivity(NotificationActivity.class);
    }

    public void startOpenGL(View v) {startActivity(OpenGLActivity.class);}

    public void startRecentTask(View v) {
        Intent intent = new Intent(this, RecentTaskActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);

        }
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void startMaterialDesign(View v) {
        startActivity(MaterialDesignActivity.class);
    }

    public void startWebIntent(View v) {
        startActivity(WebIntentActivity.class);
    }

    public void startPriorityConQueue(View v) {
        startActivity(PriorityConQueueActivity.class);
    }

    public void startNFC(View view) {
        startActivity(NFCActivity.class);
    }

    public void startFloatView(View view) {
        Intent floatDialogIntent = new Intent(this, FloatDialogService.class);
        startService(floatDialogIntent);
    }

    public void startPayActivity(View view) {
        startActivity(PayActivity.class);
    }

    public void startDatabase(View v) {
        startActivity(DataBaseActivity.class);
    }

    public void startFFmpeg(View v) {
        startActivity(FFmpegActivity.class);
    }

    public void startWhiteListActivity(View v) {
        WhiteListUtils.enterWhiteListSetting(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i("Lifecycle onRestoreInstanceState");
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i("Lifecycle onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i("Lifecycle onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i("Lifecycle onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i("Lifecycle onStop");
    }


}

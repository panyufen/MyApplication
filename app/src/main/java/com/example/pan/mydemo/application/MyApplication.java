package com.example.pan.mydemo.application;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.example.pan.mydemo.service.FloatDialogService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.pan.skin.loader.base.SkinBaseApplication;
import com.umeng.commonsdk.UMConfigure;

import cn.jpush.android.api.JPushInterface;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

/**
 * Created by PAN on 2016/9/26.
 */
//public class MyApplication extends SkinBaseApplication implements ReactApplication, Thread.UncaughtExceptionHandler {
public class MyApplication extends SkinBaseApplication implements Thread.UncaughtExceptionHandler {

    public static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(new DumperPluginsProvider() {
                    @Override
                    public Iterable<DumperPlugin> get() {
                        return new Stetho.DefaultDumperPluginsBuilder(getApplicationContext())
                                .provide(new MyDumperPlugin())
                                .finish();
                    }
                })
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(getApplicationContext()))
                .build());
        Fresco.initialize(this);

        //初始化极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        //百度地图
        SDKInitializer.initialize(getApplicationContext());

        //如果不用第三方异常处理库，需要自己捕获异常
        Thread.setDefaultUncaughtExceptionHandler(this);

        //数据库查看lib
        SQLiteStudioService.instance().start(this);

        //友盟初始化
        UMConfigure.init(this, "5ae912a8b27b0a549a000262", "UMENG_CHANNEL", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(true);
    }

//    /**
//     * Get the default {@link ReactNativeHost} for this app.
//     */
//    @Override
//    public ReactNativeHost getReactNativeHost() {
//        return mReactNativeHost;
//    }
//
//    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
//
//        String jsBundleFilePath = Environment.getExternalStorageDirectory().getPath() +
//                "/RNBundle/index.android.bundle";
//
//        @Nullable
//        @Override
//        protected String getJSBundleFile() {
//            File file = new File(jsBundleFilePath);
//            if (file.exists()) {
//                return jsBundleFilePath;
//            }
//
//            return null;
//        }
//
//
//        @Override
//        protected boolean getUseDeveloperSupport() {
//            return BuildConfig.DEBUG;
//        }
//
//        @Override
//        protected List<ReactPackage> getPackages() {
//            return Arrays.<ReactPackage>asList(
//                    new MainReactPackage(),
//                    new CustomReactPackage()
//
//            );
//        }
//    };

    private class MyDumperPlugin implements DumperPlugin {


        @Override
        public String getName() {
            return null;
        }

        @Override
        public void dump(DumperContext dumpContext) throws DumpException {

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        //主线程中未捕获的异常
        e.printStackTrace();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FloatDialogService.floatManager.removeView();
        SQLiteStudioService.instance().stop();
    }
}

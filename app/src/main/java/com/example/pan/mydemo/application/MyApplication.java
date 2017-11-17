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


import cn.jpush.android.api.JPushInterface;

/**
 * Created by PAN on 2016/9/26.
 */
//public class MyApplication extends SkinBaseApplication implements ReactApplication, Thread.UncaughtExceptionHandler {
    public class MyApplication extends SkinBaseApplication implements Thread.UncaughtExceptionHandler {
    @Override
    public void onCreate() {
        super.onCreate();

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

        //异常处理
        Thread.setDefaultUncaughtExceptionHandler(this);
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
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FloatDialogService.floatManager.removeView();
    }
}

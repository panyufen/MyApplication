package com.example.pan.mydemo.application;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.example.pan.mydemo.service.FloatDialogService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.react.BuildConfig;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.stetho.DumperPluginsProvider;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumpException;
import com.facebook.stetho.dumpapp.DumperContext;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.pan.skin.loader.base.SkinBaseApplication;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by PAN on 2016/9/26.
 */
public class MyApplication extends SkinBaseApplication implements ReactApplication{

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
    }

    /**
     * Get the default {@link ReactNativeHost} for this app.
     */
    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {

        String jsBundleFilePath = Environment.getExternalStorageDirectory().getPath()+
                "/RNBundle/index.android.bundle";

        @Nullable
        @Override
        protected String getJSBundleFile() {
            File file = new File(jsBundleFilePath);
            if(file.exists() ){
                return jsBundleFilePath;
            }

            return null;
        }


        @Override
        protected boolean getUseDeveloperSupport() {
            return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
            return Arrays.<ReactPackage>asList(
                    new MainReactPackage(),
                    new CustomReactPackage()

            );
        }
    };

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
    public void onTerminate() {
        super.onTerminate();
        FloatDialogService.floatManager.removeView();
    }
}

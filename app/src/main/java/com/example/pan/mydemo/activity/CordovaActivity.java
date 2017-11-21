package com.example.pan.mydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import org.apache.cordova.ConfigXmlParser;
import org.apache.cordova.CordovaInterfaceImpl;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CordovaWebViewImpl;
import org.apache.cordova.LOG;
import org.apache.cordova.MyCordovaPlugIn;
import org.apache.cordova.engine.SystemWebChromeClient;
import org.apache.cordova.engine.SystemWebView;
import org.apache.cordova.engine.SystemWebViewEngine;

/**
 * Created by PAN on 2016/8/11.
 */
public class CordovaActivity extends BaseActivity implements MyCordovaPlugIn.Abc{

    CordovaWebView cordovaWebView;
    Button button;

    public static final String START_URL = "file:///android_asset/www/index.html";

//    public static final String START_URL ="https://m.baidu.com";

    private CordovaInterfaceImpl cordovaInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cordova);
        //这里载入config.xml文件
        ConfigXmlParser parser = new ConfigXmlParser();
        parser.parse(this);

        button = (Button)findViewById(R.id.button);

        cordovaInterface = new CordovaInterfaceImpl(this) {
            @Override
            public Object onMessage(String id, Object data) {
                if ("onPageFinished".equals(id)) {
                    //页面载入结束回调

                }
                return super.onMessage(id, data);
            }
        };


        LOG.setLogLevel(LOG.VERBOSE);

        SystemWebView webView = (SystemWebView) findViewById(R.id.cordova_view);

        cordovaWebView = new CordovaWebViewImpl(new SystemWebViewEngine(webView));

        webView.setWebChromeClient(new MyWebChromeClient((SystemWebViewEngine) cordovaWebView.getEngine()));

        cordovaWebView.init(cordovaInterface, parser.getPluginEntries(), parser.getPreferences());

        cordovaWebView.loadUrl(START_URL);

        button = (Button )findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyCordovaPlugIn cordovaPlugin = (MyCordovaPlugIn)cordovaWebView.getPluginManager().getPlugin("MyCordova");
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("a", "异步发送消息");
//                    jsonObject.put("b","这里是b");
//                    cordovaPlugin.sendCmd(jsonObject);
//                } catch (JSONException msg) {
//                    msg.printStackTrace();
//                }

                //这里调用js
                try {
                    cordovaWebView.loadUrl("javascript:nativeCallJs('这里是来自Native的消息')");
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void showToast() {
        Toast.makeText(this, "plugin to activity ", Toast.LENGTH_SHORT).show();
    }


    class MyWebChromeClient extends SystemWebChromeClient {

        public MyWebChromeClient(SystemWebViewEngine parentEngine) {
            super(parentEngine);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

            Log.i("onProgressChanged"," "+newProgress);

            super.onProgressChanged(view, newProgress);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        cordovaInterface.onActivityResult(requestCode, resultCode, intent);
    }



    @Override
    protected void onDestroy() {
        cordovaWebView.handleDestroy();
        super.onDestroy();
    }

}

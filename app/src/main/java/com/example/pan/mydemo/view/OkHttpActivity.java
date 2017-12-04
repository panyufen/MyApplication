package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cus.pan.library.utils.FileUtils;
import com.cus.pan.library.utils.LogUtils;
import com.cus.pan.library.utils.Logger;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.http.OkHttpHelper;
import com.example.pan.mydemo.http.bean.reqbean.HomeReqBean;
import com.example.pan.mydemo.http.bean.resbean.HomeResBean;
import com.example.pan.mydemo.http.event.HomeResEvent;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

public class OkHttpActivity extends BaseActivity {
    private Toolbar mToolBar;
    //compile 'com.squareup.okhttp3:okhttp:3.4.1'
    String url = "https://www.baidu.com";

    String url12306 = "https://kyfw.12306.cn/otn/";

    String url2 = "https://www.pt18.cn/webapi/v2.5/ver?ver=1.3.1.1&requestApiUri=%2Fwebapi%2Fv2.5%2Fver";

    String urlTest = "https://ptps.pt18.cn/webapi/v2.5/ver/index.ashx?ver=1.4.2.3&requestApiUri=%2Fwebapi%2Fv2.5%2Fver%2Findex.ashx";

    String cert12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICsTCCAhqgAwIBAgIIODtw6bZEH1kwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UEBhMCQ04xKTAn\n" +
            "BgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMB4X\n" +
            "DTE0MDUyNjAxNDQzNloXDTE5MDUyNTAxNDQzNlowazELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNp\n" +
            "bm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MRkwFwYDVQQLHhCUwY3vW6JiN2cNUqFOLV/D\n" +
            "MRYwFAYDVQQDEw1reWZ3LjEyMzA2LmNuMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8Cxlz\n" +
            "+V/4KkUk8YTxVxzii7xp2gZPWuuVBiwQ6iwL98it75WNGiYCUasDXy3O8wY+PtZFvgEKkpHqQ1U6\n" +
            "uemiHStthUS1xTBsU/TuXF6AHc+oduP6zCGKcUnHRAksRb8BGSgzBA/X3B9CUKnYa9YA2EBIYccr\n" +
            "zIh6aRAjDHbvYQIDAQABo4GBMH8wHwYDVR0jBBgwFoAUeV62d7fiUoND7cdRiExjhSwAQ1gwEQYJ\n" +
            "YIZIAYb4QgEBBAQDAgbAMAsGA1UdDwQEAwIC/DAdBgNVHQ4EFgQUj/0m74jhq993ItPCldNHYLJ8\n" +
            "84MwHQYDVR0lBBYwFAYIKwYBBQUHAwIGCCsGAQUFBwMBMA0GCSqGSIb3DQEBBQUAA4GBAEXeoTkv\n" +
            "UVSeQzAxFIvqfC5jvBuApczonn+Zici+50Jcu17JjqZ0zEjn4HsNHm56n8iEbmOcf13fBil0aj4A\n" +
            "Qz9hGbjmvQSufaB6//LM1jVe/OSVAKB4C9NUdY5PNs7HDzdLfkQjjDehCADa1DH+TP3879N5zFoW\n" +
            "DgejQ5iFsAh0\n" +
            "-----END CERTIFICATE-----";

    TextView textView;

    //创建okHttpClient对象
    OkHttpClient mOkHttpClient;

    String cacheDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        int cacheSize = 10 * 1024 * 1024;
        textView = (TextView) findViewById(R.id.textview);
        LogUtils.i(cacheDir);
        SSLSocketFactory sslSocketFactory = null;
        try {
//            sslSocketFactory = setCertificates(getAssets().open("kyfw.cer"));
//            sslSocketFactory = setCertificates(new Buffer().writeUtf8(cert12306).inputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .cache(new Cache(new File(cacheDir), cacheSize))
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new StethoInterceptor());
        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory);
        }
        mOkHttpClient = builder.build();

    }


    public void get(View v) {
        //创建一个Request
        final Request request = new Request.Builder().url(urlTest).build();
        //new  call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.e(e.getMessage() + " ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String htmlStr = response.body().string();
                OkHttpActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(htmlStr);
                    }
                });
                LogUtils.i(htmlStr + " ");
            }

        });
    }


    public void post(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("action", "gdri")
                            .add("date", "2016-09-26")
                            .add("id", "562")
                            .add("vid", "2279")
                            .build();
                    Request request = new Request.Builder()
                            .url("https://www.pt18.cn/webapi/v2.5/monitor.ashx")
                            .post(formBody)
                            .build();

                    final Response response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    final String resString = response.body().string();
                    LogUtils.i(resString + " ");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                textView.setText(String.valueOf(resString + " "));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void PostByUtils(View view) {
        FileUtils.copyFileFromAssets(this, "685ba3810ad799bfd94cb24cc5865490.jpg", getExternalCacheDir().getAbsolutePath() + "/685ba3810ad799bfd94cb24cc5865490.jpg");

        File file = new File(getExternalCacheDir().getAbsolutePath() + "/685ba3810ad799bfd94cb24cc5865490.jpg");
        LogUtils.i("file " + file.getAbsolutePath() + " " + file.exists());

        HomeReqBean homeReqBean = new HomeReqBean();
        homeReqBean.temp = 1;
        homeReqBean.role = 1;
        homeReqBean.city = "沈阳市";
        homeReqBean.src = "0ca7a96d91ec798d25c827686e9b4962";
        homeReqBean.city_id_change = 210100;
        homeReqBean.isnew = 1;
        homeReqBean.version = 3106230;
        homeReqBean.uid = 926664;
        homeReqBean.rank = "member";
        homeReqBean.ysApp = "android";
        homeReqBean.vip = 1;
        homeReqBean.did = 46;
        homeReqBean.file = file;
        OkHttpHelper.getInstance().requestSync(homeReqBean, HomeResEvent.class, HomeResBean.class);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(HomeResEvent event) {
        HomeResBean homeResBean = (HomeResBean) event.resBean;
        textView.setText(new Gson().toJson(homeResBean));

    }

    public void postString(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

                    String postBody = ""
                            + "Releases\n"
                            + "--------\n"
                            + "\n"
                            + " * _1.0_ May 6, 2013\n"
                            + " * _1.1_ June 15, 2013\n"
                            + " * _1.2_ August 11, 2013\n";

                    Request request = new Request.Builder()
                            .url("https://api.github.com/markdown/raw")
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                            .build();

                    Response response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    LogUtils.i("" + response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void postMulti(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String IMGUR_CLIENT_ID = "...";
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                try {
                    LogUtils.i(Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.png");
                    // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("title", "Square Logo")
                            .addFormDataPart("image", "logo-square.png", RequestBody.create(MEDIA_TYPE_PNG, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/abc.png")))
                            .build();

                    Request request = new Request.Builder()
                            .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                            .url("https://api.imgur.com/3/image")
                            .post(requestBody)
                            .build();

                    Response response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    System.out.println(response.body().string());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    String[] urlArr = {
//            "http://download.mokeedev.com/ehaatd",
//            "http://download.mokeedev.com/ychykz",
//            "http://download.mokeedev.com/w3z2fr",
//            "http://download.mokeedev.com/5u5xfz",
//            "http://download.mokeedev.com/pdlwby",
//            "http://download.mokeedev.com/rlmbep",
//            "http://download.mokeedev.com/fpjd5v",
//            "http://download.mokeedev.com/i3aclf",
//            "http://download.mokeedev.com/xj0fng",
//            "http://download.mokeedev.com/uowje5",
            "http://download.mokeedev.com/41xryr"
    };
    int countIndex = 0;
    int count = ((int) (Math.random() * 100) + 1) * urlArr.length;

    public void postLoop(final View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Request request = new Request.Builder()
                        .url(urlArr[countIndex % urlArr.length])
                        .build();
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            call.cancel();
                            LogUtils.i(countIndex + " success " + request.url());
                            countIndex++;
                            if (countIndex <= count) {
                                postLoop(v);
                            }
                        }
                    }
                });
            }
        }).start();
    }


    private static class LoggingInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("{");
            FormBody formBody = (FormBody) request.body();
            for (int i = 0; i < formBody.size(); i++) {
                stringBuffer.append(formBody.name(i)).append(":").append(formBody.value(i));
                if (i + 1 < formBody.size()) {
                    stringBuffer.append(",");
                }
            }
            stringBuffer.append("}");

            long t1 = System.nanoTime();
            Logger.i(String.format("Sending request %s%nbody:%s", request.url(), stringBuffer.toString()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            ResponseBody responseBody = response.body();
            long contentLen = responseBody.contentLength();
            String result = "";

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Util.bomAwareCharset(buffer, response.body().contentType().charset());

            if (contentLen != 0) {
                result = buffer.clone().readString(charset);
                result = decodeUnicode(result);
            }

            Logger.i(String.format("Received response from %s in %.1fms%nbody:%s", response.request().url(), (t2 - t1) / 1e6d, result + " \nlen:" + result.length()));

            return response;
        }


        private String decodeUnicode(String theString) {
            char aChar;
            int len = theString.length();
            StringBuilder outBuffer = new StringBuilder(len);
            for (int x = 0; x < len;) {
                aChar = theString.charAt(x++);
                if (aChar == '\\') {
                    aChar = theString.charAt(x++);
                    if (aChar == 'u') {
                        // Read the xxxx
                        int value = 0;
                        for (int i = 0; i < 4; i++) {
                            aChar = theString.charAt(x++);
                            switch (aChar) {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    value = (value << 4) + aChar - '0';
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    value = (value << 4) + 10 + aChar - 'a';
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    value = (value << 4) + 10 + aChar - 'A';
                                    break;
                                default:
                                    throw new IllegalArgumentException(
                                            "Malformed   \\uxxxx   encoding.");
                            }

                        }
                        outBuffer.append((char) value);
                    } else {
                        if (aChar == 't')
                            aChar = '\t';
                        else if (aChar == 'r')
                            aChar = '\r';
                        else if (aChar == 'n')
                            aChar = '\n';
                        else if (aChar == 'f')
                            aChar = '\f';
                        outBuffer.append(aChar);
                    }
                } else
                    outBuffer.append(aChar);
            }
            return outBuffer.toString();
        }
    }


    public SSLSocketFactory setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

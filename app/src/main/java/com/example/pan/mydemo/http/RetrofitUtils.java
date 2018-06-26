package com.example.pan.mydemo.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Pan on 2017/11/18.
 */

public class RetrofitUtils {

    private static final String BASE_URL = "http://www.baidu.com";
    private static final int TIME = 60;

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(TIME, TimeUnit.SECONDS)
            .readTimeout(TIME, TimeUnit.SECONDS)
            .writeTimeout(TIME, TimeUnit.SECONDS)
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Request requestNew = request.newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("content_Type", "application/json;charset=utf-8")
                            .addHeader("Connection", "keep-alive")
                            .build();
                    return chain.proceed(requestNew);
                }
            })
            .build();
}

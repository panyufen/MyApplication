package com.example.pan.mydemo.http;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.http.base.HttpMethodType;
import com.example.pan.mydemo.http.bean.base.ReqBean;
import com.example.pan.mydemo.http.bean.base.ResBean;
import com.example.pan.mydemo.http.event.ResEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Pan on 2017/11/18.
 * OkHttp封装类
 */
public class OkHttpHelper {

    public static String BASE_URL = "http://d.lc86.net";

    private static OkHttpHelper okHttpHelper;

    private static final Set<Class<?>> BASE_CLASS_SET = new HashSet<>();

    static {
        BASE_CLASS_SET.add(String.class);
        BASE_CLASS_SET.add(CharSequence.class);
        BASE_CLASS_SET.add(Byte.class);
        BASE_CLASS_SET.add(Short.class);
        BASE_CLASS_SET.add(Integer.class);
        BASE_CLASS_SET.add(Float.class);
        BASE_CLASS_SET.add(Long.class);
        BASE_CLASS_SET.add(Double.class);
    }

    private static final int TIME = 60;

    private OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
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
                            .addHeader("User-Agent", "Mozilla/5.0 (Linux; LANCARE Android)")
                            .addHeader("Connection", "keep-alive")
                            .build();
                    return chain.proceed(requestNew);
                }
            })
            .addInterceptor(new LoggingInterceptor())
            .build();

    private OkHttpHelper() {

    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public static OkHttpHelper getInstance() {
        if (okHttpHelper == null) {
            synchronized (OkHttpHelper.class) {
                if (okHttpHelper == null) {
                    okHttpHelper = new OkHttpHelper();
                }
            }
        }
        return okHttpHelper;
    }

    public void setOkHttpClient(OkHttpClient client) {
        this.mOkHttpClient = client;
    }


    public Call requestSync(final ReqBean reqBean, Class<? extends ResEvent> resEvent, final Class<? extends ResBean> resBean) {
        Call call = null;
        HttpMethodType httpMethodType = reqBean.getMethodType();

        List<Field> reqFields = getFields(reqBean.getClass());
        MultipartBody.Builder multiFormBodyBuidler = new MultipartBody.Builder();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        int fileCount = 0;
        try {
            for (Field field : reqFields) {
                if (Modifier.isPrivate(field.getModifiers())
                        || Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {//如果是private static transient 则忽略
                    continue;
                }
                String fieldName = field.getName();
//                LogUtils.i("fieldName " + fieldName + " " + Modifier.toString(field.getModifiers()) + " " + field.getType().getSimpleName());
                Object val = field.get(reqBean);
                if (val != null) {
                    if (val instanceof File) { //如果是文件  先忽略
                        fileCount++;
                        File file = (File) val;
                        multiFormBodyBuidler.addFormDataPart(fieldName, file.getName(), RequestBody.create(MultipartBody.FORM, file));

                    } else if (val instanceof String) {
                        formBodyBuilder.add(fieldName, (String) val);
                        multiFormBodyBuidler.addFormDataPart(fieldName, (String) val);
                    } else if (BASE_CLASS_SET.contains(val.getClass())) {
                        formBodyBuilder.add(fieldName, String.valueOf(val));
                        multiFormBodyBuidler.addFormDataPart(fieldName, String.valueOf(val));
                    } else {
                        formBodyBuilder.add(fieldName, val.toString());
                        multiFormBodyBuidler.addFormDataPart(fieldName, val.toString());
                    }
                }
            }

            Request.Builder requestBuilder = new Request.Builder();
            String requestUrl = BASE_URL + reqBean.getPath();
            if (httpMethodType == HttpMethodType.GET || reqFields.size() == 0) {
                FormBody formBody = formBodyBuilder.build();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("?");
                for (int i = 0; i < formBody.size(); i++) {
                    stringBuilder.append(formBody.name(i)).append("=").append(formBody.value(i)).append("&");
                }
                requestUrl += stringBuilder.toString();
                requestBuilder.method("GET", null);
                LogUtils.i(String.format("NetStart: GET %n%s", requestUrl));
            } else {

                if (fileCount > 0) { //如果有文件 则进行转换成FormMulti方式 否则用urlencode方式
                    multiFormBodyBuidler.setType(MultipartBody.FORM);
                    requestBuilder.method("POST", multiFormBodyBuidler.build());
                } else {
                    requestBuilder.method("POST", formBodyBuilder.build());
                }
                LogUtils.i(String.format("NetStart: POST %s%n%s", requestUrl, new Gson().toJson(reqBean)));
            }

            requestBuilder.url(requestUrl);
            Request request = requestBuilder.build();
            final long t1 = System.nanoTime();
            call = mOkHttpClient.newCall(request);
            //设置返回Event
            final ResEvent resultResEvent = resEvent.newInstance();
            resultResEvent.reqBean = reqBean;
            //默认ResBean
            final ResBean defaultResBean = new ResBean();
            defaultResBean.res = 0;
            defaultResBean.msg = "error";
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    long t2 = System.nanoTime();
                    LogUtils.i(String.format("NetResponse: %s in %.1fms requestFailed", BASE_URL + reqBean.getPath(), ((t2 - t1) / 1e6d)));
                    EventBus.getDefault().post(resultResEvent);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    long t2 = System.nanoTime();
                    String resResult = response.body().string();
                    ResBean responseBean = new Gson().fromJson(resResult, resBean);
                    if (responseBean == null) {
                        responseBean = defaultResBean;
                    }
                    resultResEvent.resBean = responseBean;
                    LogUtils.i(String.format("NetResponse: %s in %.1fms %n%s", BASE_URL + reqBean.getPath(), ((t2 - t1) / 1e6d), new Gson().toJson(responseBean)));
                    EventBus.getDefault().post(resultResEvent);
                }
            });
            return call;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return call;
    }

    //获取参数列表
    private List<Field> getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        try {
            while (clazz != null && !clazz.equals(Object.class)) {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    list.add(field);
                }
                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

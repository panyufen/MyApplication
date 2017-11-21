package com.example.pan.mydemo.activity;

import android.os.Bundle;

import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends BaseActivity {


    /**
     * Retrofit 使用详解
     * http://blog.csdn.net/carson_ho/article/details/73732076
     * 使用 Retrofit 的步骤共有7个：
     * 步骤1：添加Retrofit库的依赖
     * 步骤2：创建 接收服务器返回数据 的类
     * 步骤3：创建 用于描述网络请求 的接口
     * 步骤4：创建 Retrofit 实例
     * 步骤5：创建 网络请求接口实例 并 配置网络请求参数
     * ：发送网络请求（异步 / 同步）
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
    }

}

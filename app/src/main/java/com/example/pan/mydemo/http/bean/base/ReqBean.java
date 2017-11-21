package com.example.pan.mydemo.http.bean.base;

import com.example.pan.mydemo.http.base.HttpMethodType;

/**
 * Created by Pan on 2017/11/18.
 */

public abstract class ReqBean {
    // POST ，GET
    public abstract HttpMethodType getMethodType();

    //地址（不包括主机）
    public abstract String getPath();

}

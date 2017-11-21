package com.example.pan.mydemo.http.bean;

import com.example.pan.mydemo.http.base.HttpMethodType;
import com.example.pan.mydemo.http.bean.base.ReqBean;

import java.io.File;

/**
 * Created by Pan on 2017/11/18.
 */
public class HomeReqBean extends ReqBean {


    /**
     * temp : 1
     * role : 1
     * city : 沈阳市
     * src : 0ca7a96d91ec798d25c827686e9b4962
     * city_id_change : 210100
     * isnew : 1
     * version : 3106230
     * uid : 926664
     * rank : member
     * ysApp : android
     * vip : 1
     * did : 46
     */

    public int temp;
    public int role;
    public String city;
    public String src;
    public int city_id_change;
    public int isnew;
    public int version;
    public int uid;
    public String rank;
    public String ysApp;
    public int vip;
    public int did;
    public File file;


    @Override
    public String getPath() {
        return "/2/home";
    }

    @Override
    public HttpMethodType getMethodType() {
        return HttpMethodType.POST;
    }
}

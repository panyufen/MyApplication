package com.example.pan.mydemo.http.event;

import com.example.pan.mydemo.http.bean.base.ReqBean;
import com.example.pan.mydemo.http.bean.base.ResBean;

/**
 * Created by Pan on 2017/11/18.
 */

public class ResEvent<T extends ResBean> {

    public ReqBean reqBean;

    public T resBean;

}

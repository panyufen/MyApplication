package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class VolleyActivity extends BaseActivity {
    private Toolbar mToolBar;
    TextView textView;

    RequestQueue mQueue;


    String url = "https://www.baidu.com";

    String url0 = "http://blog.isming.me/2014/05/11/use-network-in-android/";

    String url2 = "https://www.pt18.cn/webapi/v2.5/ver/?ver=1.3.1.1&requestApiUri=%2Fwebapi%2Fv2.5%2Fver";

    String url1 = "http://www.tuicool.com/comments/QNZFNr.json?page=1&last=0";



    Response.Listener<String> successListener;
    Response.ErrorListener errorListener;

    HttpURLConnection httpURLConnection;
    HttpsURLConnection httpsURLConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        mToolBar = setSupportActionBar(R.id.tool_bar);

        textView = (TextView)findViewById(R.id.content);

        mQueue = Volley.newRequestQueue(getApplication());

        successListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                textView.setText(s);
                LogUtils.i(""+s);
            }
        };

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                textView.setText(volleyError.getMessage());
            }
        };

        StringRequest stringRequest = new StringRequest(url2,successListener,errorListener);

        StringRequest stringRequest1 = new StringRequest(
                Request.Method.GET,
                url1,
                successListener,
                errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("word", "abc");
                map.put("params2", "value2");
                return map;
            }

            @Override
            protected String getParamsEncoding() {
                return "utf-8";
            }

        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LogUtils.i( response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtils.e( error.getMessage());
            }
        }){

        };

        mQueue.add(stringRequest);

    }


}
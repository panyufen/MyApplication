package com.example.pan.mydemo.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cus.pan.library.utils.Logger;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.paylib.alipay.OrderInfoUtil2_0;
import com.example.paylib.alipay.PayResult;

import java.util.Map;

public class PayActivity extends BaseActivity {

    //支付宝支付业务：入参app_id
    public static final String APPID = "2016080300159311";
    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCIFx4TK7N27c4SqOO22exEgq8eWz8feHeMs27pIEFDODFLTWhg2yxu1dRHT+QbNzjmRK3k46aBv7KF+apd3JK98mqIm6ZUnJWnKQxChxG3chOtUBmDy2ujtZbqFaDgHSZLtbc+uzs2LR8QQ0VisdGRML1TDP46jgllW2qJIZC8ggtfVHOB3Wtca2r1I0JF482NDYxMEmUImk6mxEuHlmNW1DeXt7opRYVbNLy8aguLJcgBXMjoARQmCMy0gaYMZlNuNf/pqnPBEQrK0fjb3nVBPrKvQutJC2Rnmev6T1bLph2QD6t0gqNP9QHLA9P4IVBHX/bK01jSfPt3TEcPF4UtAgMBAAECggEAWum+oKXwFQIWSWbkALIiYT5ASHJCf/6VNrxj2n8+ue4fGCQVF6czTrOwRRd81iWU8FdvJ9pg0IrEczXvdxa0PQpl2u7INfwJCYulcggTcM5XkM9SzDWnqczUjXp3Z2BxxeAWE2U20CFigBXAk4cg7fJ2d7XVXD6ozjo8uHFg5FyuK+dooIoYJoi1rtEe39tV40Shln2ealogz5Ue3X4/GOn5jGHlcC6WqQ7jTqLFApKtAaCrLYIpxAkCVL7Jvzh+h0lW+56ZhrNTYzYDm74qBfkf2/YrI47tPNZKcOHgAwKnJ0aS7CtuGQkz+5DAgVnmJnzpM83xKnlv+h/PLj1L7QKBgQDCoPPCYzzPqFatq46Sa7f787l1OgM1ox1xvIw7VTeX3zywOu8ARR6DWrhxfh2eXjC4aeCJRrXZSGJRiWk++OAF8kBRwyboDgcANz05XTwiPRToLliPKUgo8oqbpYtS7btgai7uIsvOyC5Yfk605azbhtVBsbSlX5yQxr5sDvdURwKBgQCzAMIvuDI+FbDI3ilWom2CNnEm537nZ6LpCD1GxBECwJbIkDzTNaJW9YxCvoLy2TDekPquAJvE01TH3N6g6uVyAHW+9i1Bp0f8qCTDCh9VswmCV3s/yKw68DBZbbDZK7AT84pKxzmaaVbR+AdW8rmk4PzxGUnlIXSbk1YkCGWY6wKBgBqVDxXypeCa1lPcBZn7+mIAFMltBfkycRgSr3h/GBu3ErvjILeBuTwpjN17YyI5LSBS3acCMF4f48XNJcHDDh3hy2UfaVXybIRcBxGpqUfn3c4kXlYXRlLJZaMChjfqXlg5yHUf7YQtlR7jCW7tm6FUFhZl5g1cNK8VZfauSJuLAoGAPcLBPV28WBd7y5J7iNy6E/dcyyOMcaMcXnLtUYFMrTpty/2YPvd+b6qBHnZFEC1H40L6exbSp/KSifL3DWZ+i/sEpUKjEsf42n/ZT0Iq1ZLUcRhl+ffvFNbSl8h1ADlGo1ugF3Tttw0pW4DMZ3+P9gd6vmUPTfNhHFyVmBEdnMkCgYAHM8GdvYagxJ+D8w7QouQBJCFbLNXbDFpbQYp98yiiV6hzrQtvg8yidbtCst/sdWMpJLiV0K+rit+CEKF5/J5sPZaxsJzNYjdN0Mle0yLAZ5+VKGhdeLAfEQnkuZnTu8yfesVTP5VDKtqrALLKzFHUHg3Akeub7zImj/Pyjw42Yw==";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果是沙箱环境 则需要设置
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        setContentView(R.layout.activity_pay);


        mHandler = new Handler() {
            @SuppressWarnings("unused")
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        @SuppressWarnings("unchecked")
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    default:
                        break;
                }
            }

            ;
        };

    }


    public void startAlipay(View view) {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Logger.i("orderinfo " + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void startWXpay(View view) {

    }
}

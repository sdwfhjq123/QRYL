package com.qryl.qryl.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.qryl.qryl.R;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.EncryptionByMD5;
import com.qryl.qryl.util.HttpUtil;
import com.qryl.qryl.util.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PayActivity";

    /**
     * 判断makelist还是normal的标识
     */
    private static final int ORDER_NORMAL = 111;
    private static final int ORDER_MAKELIST = 222;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;


    private LinearLayout llWx;
    private LinearLayout llZfb;
    private CheckBox cbWx;
    private CheckBox cbZfb;
    /**
     * 订单的价格
     */
    private String orderPrice;
    private String orderId;
    private int orderType;
    /**
     * 支付宝订单信息
     */
    private String dataAlipay;

    private String token;
    private int orderNormal;
    private String userId;

    /**
     * 处方订单id
     */
    private int prescribeId;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
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
                        //销毁界面
                        finish();
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        Intent intent = getIntent();
        orderNormal = intent.getIntExtra("order_normal", 0);
        orderPrice = intent.getStringExtra("order_price");
        if (orderNormal == ORDER_MAKELIST) {
            prescribeId = intent.getIntExtra("prescribeId", 0);
        } else if (orderNormal == ORDER_NORMAL) {
            orderId = intent.getStringExtra("order_id");
            orderType = intent.getIntExtra("order_type", 4);
        }

        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        userId = prefs.getString("user_id", "");
        initView();
    }

    private void initView() {
        hiddenView();
        llZfb = (LinearLayout) findViewById(R.id.ll_zfb);
        cbZfb = (CheckBox) findViewById(R.id.cb_zfb);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        TextView tvMoney = (TextView) findViewById(R.id.tv_money);
        llZfb.setOnClickListener(this);
        cbZfb.setOnClickListener(this);
        btnPay.setOnClickListener(this);

        //显示金钱
        tvMoney.setText(String.valueOf(orderPrice));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_zfb:
                if (cbZfb.isChecked()) {
                    cbZfb.setChecked(false);
                } else if (!cbZfb.isChecked()) {
                    cbZfb.setChecked(true);
                }
                break;
            case R.id.cb_zfb:
                break;
            case R.id.btn_pay:
//                //支付
//                if (cbWx.isChecked()) {//&& cbZfb.isChecked()
//                    Toast.makeText(this, "只能选择一个应用进行支付", Toast.LENGTH_SHORT).show();
//                } else if (cbWx.isChecked() && !cbZfb.isChecked()) {
//                    Log.i(TAG, "onClick: 调用了微信支付");
//                    //微信支付
//                    wxPay();
//                } else
                if (cbZfb.isChecked()) {//!cbWx.isChecked() &&
                    //从服务器获取支付宝订单信息
                    if (orderNormal == ORDER_NORMAL) {//普通的订单
                        postOrderInfoOnServer();
                    } else if (orderNormal == ORDER_MAKELIST) {
                        //开单子的订单
                        postListOrderInfoOnServer();
                    }
                    //支付宝支付
                    aliPay();
                }
                break;
        }
    }

    /**
     * 开单子的支付
     */
    private void postListOrderInfoOnServer() {
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        byte[] bytes = ("/test/order/buildPrescribeInfo-" + token + "-" + currentTimeMillis).getBytes();
        String sign = EncryptionByMD5.getMD5(bytes);
        Map<String, String> map = new HashMap<>();
        map.put("puId", userId);
        map.put("prescribeId", String.valueOf(prescribeId));
        map.put("sign", sign);
        map.put("tokenUserId", userId + "bh");
        map.put("timeStamp", String.valueOf(currentTimeMillis));
        HttpUtil.postAsyn(ConstantValue.URL + "/order/buildPrescribeInfo", map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String resultCode = jsonObject.getString("resultCode");
                    switch (resultCode) {
                        case "500":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PayActivity.this, "下单失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case "200"://成功时赋值
                            dataAlipay = jsonObject.getString("data");
                            break;
                        case "400": //错误时
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent("com.qryl.qryl.activity.BaseActivity.MustForceOfflineReceiver");
                                    sendBroadcast(intent);
                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 支付宝支付
     */
    private void aliPay() {
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!TextUtils.isEmpty(dataAlipay)) {
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(PayActivity.this);
                    // 调用支付接口，获取支付结果
                    Map<String, String> result = alipay.payV2(dataAlipay, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else {
            Toast.makeText(this, "网络繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 支付宝普通订单返回的数据
     *
     * @return 返回签名后的数据
     */
    private String postOrderInfoOnServer() {
        String currentTimeMillis = String.valueOf(System.currentTimeMillis());
        byte[] bytes = ("/test/order/buildOrderInfo-" + token + "-" + currentTimeMillis).getBytes();
        String sign = EncryptionByMD5.getMD5(bytes);
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("orderId", orderId);
        builder.add("orderType", String.valueOf(orderType));
        builder.add("sign", sign);
        builder.add("tokenUserId", userId + "bh");
        builder.add("timeStamp", currentTimeMillis);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + "/order/buildOrderInfo")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String resultCode = jsonObject.getString("resultCode");
                    switch (resultCode) {
                        case "500":
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PayActivity.this, "下单失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        case "200"://成功时赋值
                            dataAlipay = jsonObject.getString("data");
                            break;
                        case "400": //错误时
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent("com.qryl.qryl.activity.BaseActivity.MustForceOfflineReceiver");
                                    sendBroadcast(intent);
                                }
                            });
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return dataAlipay;
    }

    /**
     * 微信支付
     */
    private void wxPay() {
        IWXAPI wxapi = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APP_ID);
        wxapi.registerApp(ConstantValue.WX_APP_ID);
//        boolean isPaySupported = wxapi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
        PayReq payReq = new PayReq();
        payReq.appId = ConstantValue.WX_APP_ID;
        payReq.partnerId = "1900006771";
        payReq.prepayId = "wx20171106144014928044f27d0489404279";
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = "b3593d71a7ce427127b363116a203313";//随机字符串
        payReq.timeStamp = "1509950411";//时间戳
        payReq.sign = "91E57E4B8A53F4AD17D21AD42993BB6F";
        wxapi.sendReq(payReq);
    }

    private void hiddenView() {
        TextView tvReturn = (TextView) findViewById(R.id.tv_return);
        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        TextView tvHelp = (TextView) findViewById(R.id.tv_help);

        tvReturn.setVisibility(View.VISIBLE);
        tvLocation.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvHelp.setVisibility(View.GONE);

        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvTitle.setText("预约支付");
    }

}

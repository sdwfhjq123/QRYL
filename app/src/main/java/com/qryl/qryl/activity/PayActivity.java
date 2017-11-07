package com.qryl.qryl.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.qryl.qryl.util.AuthResult;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.PayResult;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "PayActivity";

    private LinearLayout llWx;
    private LinearLayout llZfb;
    private CheckBox cbWx;
    private CheckBox cbZfb;
    private TextView tvMoney;
    /**
     * 订单的价格
     */
    private double orderPrice;
    /**
     * 支付宝订单信息
     */
    private String dataAlipay;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

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
                    Log.i(TAG, "handleMessage: " + resultStatus);
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
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public static void actionStart(Context context, Double price) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("order_price", price);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Intent intent = getIntent();
        orderPrice = intent.getDoubleExtra("order_price", 0);
        initView();
    }

    private void initView() {
        hiddenView();
        llWx = (LinearLayout) findViewById(R.id.ll_wx);
        llZfb = (LinearLayout) findViewById(R.id.ll_zfb);
        cbWx = (CheckBox) findViewById(R.id.cb_wx);
        cbZfb = (CheckBox) findViewById(R.id.cb_zfb);
        Button btnPay = (Button) findViewById(R.id.btn_pay);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        llWx.setOnClickListener(this);
        llZfb.setOnClickListener(this);
        cbWx.setOnClickListener(this);
        cbZfb.setOnClickListener(this);
        btnPay.setOnClickListener(this);

        tvMoney.setText(String.valueOf(orderPrice));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wx:
                if (cbWx.isChecked()) {
                    cbWx.setChecked(false);
                    if (cbZfb.isChecked()) {
                        cbZfb.setChecked(false);
                    }
                } else if (!cbWx.isChecked()) {
                    cbWx.setChecked(true);
                    if (cbZfb.isChecked()) {
                        cbZfb.setChecked(false);
                    }
                }
                break;
            case R.id.ll_zfb:
                if (cbZfb.isChecked()) {
                    cbZfb.setChecked(false);
                    if (cbWx.isChecked()) {
                        cbWx.setChecked(false);
                    }
                } else if (!cbZfb.isChecked()) {
                    cbZfb.setChecked(true);
                    if (cbWx.isChecked()) {
                        cbWx.setChecked(false);
                    }
                }
                break;
            case R.id.cb_wx:
                break;
            case R.id.cb_zfb:
                break;
            case R.id.btn_pay:
                //支付
                if (cbWx.isChecked() && cbZfb.isChecked()) {
                    Toast.makeText(this, "只能选择一个应用进行支付", Toast.LENGTH_SHORT).show();
                } else if (cbWx.isChecked() && !cbZfb.isChecked()) {
                    Log.i(TAG, "onClick: 调用了微信支付");
                    //微信支付
                    wxPay();
                } else if (!cbWx.isChecked() && cbZfb.isChecked()) {
                    Log.i(TAG, "onClick: 调用了支付宝支付");
                    //支付宝支付
                    aliPay(0, 0);
                }
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay(int orderId, int orderType) {
        //从服务器获取支付宝订单信息
        postOrderInfoOnServer();
        Log.i(TAG, "aliPay: 支付宝订单信息" + dataAlipay);
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
    }

    private String postOrderInfoOnServer() {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + "/order/buildOrderInfo")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.i(TAG, "onResponse: 支付宝订单信息" + result);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    dataAlipay = jsonObject.getString("data");
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
        payReq.nonceStr = "b3593d71a7ce427127b363c16a203313";//随机字符串
        payReq.timeStamp = "1509950414";//时间戳
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

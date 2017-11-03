package com.qryl.qryl.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.qryl.qryl.util.PayResult;
import com.qryl.qryl.util.SignUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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

    public static final String PARTNER = "2017102009411573";

    // 商户收款账号
    public static final String SELLER = "2354507474@qq.com";

    // 商户私钥，pkcs8格式
    public static final String RSA2_PRIVATE = "MIIEogIBAAKCAQEAul06bnuk3ZXIG8jC/RlRlBCGaIdYAsXUHDPbFyCi3Sr06liauPB5uMbWVxwBVlIbejCYjdPynMO2FPEZlaYUPS+z581bhNuhhzun3M089KlY4zqQP21Cy/SUU7DjO+egc50LFGZXFU2hVSwTxunRrsIQUS8DxuOcTP+2sapRm+r3dFtO61l5sbPjOQMLfuiuB6qWhk0gMZOIHlnlx1IargRQY1G9ROFoWAn/7fSj0RnKMw9bVLelkuoNwS/SCvu9uUfYzLO5TJw2MDDqI5AlWmH1nRaK7QrEFI8k2if64yJQHd8jI1pxZ5uAyBbb1eZELduknE5U0XQludrF7a4rCwIDAQABAoIBAH0ISUiQmca0U+IYKoHmN64v/A0rKKgLk8gsHkSA9+OMi26ibYPAitmmRW2B83+3bInpCqC03yO/xmx8aV7WYuO+GmRdiZY0SEXTh0aDR+8ZovIoe1iidFsjx1Py7DFnsGWAqktQcgJv59qdzNL6Ulx+BLaC5XTNhjK+qRK/oysILRJGac9ZOkopueJJyrTDnqL8dxfa742CmzMnr4RjjXmGUVrvukHdcv0rPsmI78hiiEm9GpyAdNMDXjRRu5W2+P8m2trpdjvhaYhqhRkgDLpR1eqxQTqxBDCjRBQ1tMdarV0SmNwYMsg5IMk8u+nJNEGZ9nZeRPUE3vZXUMn9bgECgYEA9yyFQAmqDKDnBEBKaz8vHsjNg4+KSQJpkFULCz69pmiaQiCgIkHu3HpwRSIQAHmuWe653DwtOvMYXfkb8Kpln9pPV+J1kudMiIwxxtc9jmeQUuimpPCEjhts0uFP3xyYfcFR0F1E8M9aEtq2pMCNNrzypNpxfkN5F6EYKWPqywECgYEAwQTUm56E6HL+NXAvSL4Z9+pA6Id8RajTUQmcKHPklkpRXrMgnizmEe6HxKelKuBmEoJVS+WhV77nNFzTYzMxT7QDyAMkkOaaWjzF5t7nUJ52XTN+7RiCs2Ajpjq3/6jNIoG4w4eoOX8TIWuZlM05/wa13gzWd3UjU/IHXDkxcgsCgYA0a5m68hHBaeJ0sVnXEuhgY//J9ghC4aMXvCGCegTopOiKO7cabNdGpSToVIgGQcgrRIjgX1bMWMADNhCp4sl4tGj9X4bF6A4AK5Nm80EX0Xj3TGYDNws1xDU5KBWzYLIqgXRjd1RmqeMVepMfr8KDKGFhev5048RrsXuZU5p5AQKBgD0i4Iyh+wr9UVNvwypRPDxwMDr2nwAZZ0Vlu9Z8TszjoT5TkmNHaWAIo2xAhWo9RVdbfNTbWO1IBEdrl3D2SAosxH3XsP9Ma1tloHFt7Op2JpuCshM7DjrumpwOQTVzK/ZgDDuyfjAn9dFCGZUuI86JO5Wnj06rBZOTN42Yd/bdAoGAd5/8k0CendL2QpMSuTxfJiiyaw1Mr6kFUHDxwZMYdc/V0Q7TA6cPoHXFxn9ZjmLK6OhSY0FL+npncmBKadic6brk6MRtP8fNNnWaC0Lzig0+vCFPb1JFuptoFGKqlz+oi5vrl6+RRKs6LYDLLrYkU6q07ojHrZgR3TkfUvNmacM=";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    Log.e(TAG, "handleMessage: 获得的resultStatus: " + resultStatus);
                    Log.e(TAG, "handleMessage: 获得的resultInfo: " + resultInfo);
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
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA2_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
//        try {
//            /**
//             * 仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

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

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA2_PRIVATE, true);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        //return "sign_type=\"RSA\"";
        return "sign_type=\"RSA2\"";
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }


    /**
     * 微信支付
     */
    private void wxPay() {
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

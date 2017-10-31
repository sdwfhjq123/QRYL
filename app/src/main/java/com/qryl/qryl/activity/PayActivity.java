package com.qryl.qryl.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qryl.qryl.R;

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
                    aliPay();
                }
                break;
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {

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

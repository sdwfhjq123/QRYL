package com.qryl.qryl.wxapi;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qryl.qryl.R;
import com.qryl.qryl.util.ConstantValue;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, ConstantValue.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onReq(BaseReq req) {
        Log.i(TAG, "onReq: 微信支付回调事务:" + req.transaction);
        if (req.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            if (req.transaction.equals(0)){
//
//            }
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onResp(BaseResp resp) {
        Log.i(TAG, "onPayFinish, errCode = " + resp.errCode);

        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("微信支付结果" + String.valueOf(resp.errCode));
            builder.show();
        }
    }
}
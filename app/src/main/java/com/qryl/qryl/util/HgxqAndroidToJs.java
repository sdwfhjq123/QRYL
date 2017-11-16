package com.qryl.qryl.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.qryl.qryl.activity.PayActivity;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;


/**
 * Created by yinhao on 2017/9/26.
 */

public class HgxqAndroidToJs {
    private static final String TAG = "HgxqAndroidToJs";
    private static final int ORDER_NORMAL = 111;
    private static final int ORDER_MAKELIST = 222;

    private Activity activity;
    private Context context;

    public HgxqAndroidToJs(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }

    /**
     * 根布局点击返回销毁页面
     */
    @JavascriptInterface
    public void finishActivity() {
        this.activity.finish();
    }

    @JavascriptInterface
    public void pay(String orderId, int orderType, String price) {
        Log.i(TAG, "pay: order_price" + price + ",order_id" + orderId + ",order_type" + orderType);
        Toast.makeText(context, "pay: order_price" + price + ",order_id" + orderId + ",order_type" + orderType, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("order_price", price);
        intent.putExtra("order_id", orderId);
        intent.putExtra("order_type", orderType);
        intent.putExtra("order_normal", ORDER_NORMAL);
        context.startActivity(intent);
        activity.finish();
    }

    @JavascriptInterface
    public String getToken() {
        SharedPreferences prefs = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return token;
    }

    @JavascriptInterface
    public void forceOffline() {
        Intent intent = new Intent("com.qryl.qryl.activity.BaseActivity.MustForceOfflineReceiver");
        context.sendBroadcast(intent);
    }

}

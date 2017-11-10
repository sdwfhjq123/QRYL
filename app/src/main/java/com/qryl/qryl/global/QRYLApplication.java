package com.qryl.qryl.global;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.qryl.qryl.util.ConstantValue;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.sms.SMSSDK;

/**
 * Created by hp on 2017/8/16.\
 * 自定义Application，进行全局初始化处理
 */

public class QRYLApplication extends LitePalApplication {

    private static final String TAG = "QRYLApplication全局application";

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    /**
     * 微信应用的appKey
     */
    private static final String APP_ID_WX = ConstantValue.WX_APP_ID;
    /**
     * IWXAPI是第三方app和微信通信的openapi接口
     */
    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();
        //获取主线程的id
        mainThreadId = Process.myTid();

        //初始化JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //初始化极光短信
        SMSSDK.getInstance().initSdk(this);
        SMSSDK.getInstance().setDebugMode(true);
        SMSSDK.getInstance().setIntervalTime(60000);

        register2WX();
    }

    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    /**
     * 注册到微信
     */
    @SuppressLint("LongLogTag")
    private void register2WX() {
        //AppId wxd06bc8ef2fb9118b
        //通过WXAPIFactroy工厂，获取IWXAPI实例
        api = WXAPIFactory.createWXAPI(this, APP_ID_WX, true);
        api.registerApp(APP_ID_WX);
        boolean wxAppSupportAPI = api.isWXAppSupportAPI();
        boolean wxAppInstalled = api.isWXAppInstalled();
        Log.i(TAG, "register2WX: 是否安装了微信app: " + wxAppInstalled + " ,是否支持wxapi: " + wxAppSupportAPI);
    }

}

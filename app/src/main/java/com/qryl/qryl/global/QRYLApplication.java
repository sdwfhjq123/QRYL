package com.qryl.qryl.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import org.litepal.LitePalApplication;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.sms.SMSSDK;

/**
 * Created by hp on 2017/8/16.\
 * 自定义Application，进行全局初始化处理
 */

public class QRYLApplication extends LitePalApplication {

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

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
}

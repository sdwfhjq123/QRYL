package com.qryl.qryl.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.qryl.qryl.activity.login.LoginActivity;
import com.qryl.qryl.util.ActivityCollector;



@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private ForceOfflineReceiver receiver;
    private MustForceOfflineReceiver mustForceOfflineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.qryl.qryl.activity.BaseActivity.ForceOfflineReceiver");
        receiver = new ForceOfflineReceiver();
        registerReceiver(receiver, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.qryl.qryl.activity.BaseActivity.MustForceOfflineReceiver");
        mustForceOfflineReceiver = new MustForceOfflineReceiver();
        registerReceiver(mustForceOfflineReceiver, intentFilter2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (mustForceOfflineReceiver != null) {
            unregisterReceiver(mustForceOfflineReceiver);
            mustForceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    class ForceOfflineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("退出登录");
            builder.setMessage("您确定要退出当前账号吗");
            builder.setCancelable(false);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefsUserId = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
                    prefsUserId.edit().clear().apply();
                    SharedPreferences prefsImg = context.getSharedPreferences("image", Context.MODE_PRIVATE);
                    prefsImg.edit().clear().apply();
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }

    class MustForceOfflineReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("下线通知");
            builder.setMessage("您已长时间未登录，请重新登录");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences prefsUserId = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
                    prefsUserId.edit().clear().apply();
                    SharedPreferences prefsImg = context.getSharedPreferences("image", Context.MODE_PRIVATE);
                    prefsImg.edit().clear().apply();
                    ActivityCollector.finishAll();
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }
            });
            builder.show();
        }
    }
}

package com.qryl.qryl.activity.H5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.HgxqAndroidToJs;

import static android.view.KeyEvent.KEYCODE_BACK;

public class XzxqActivity extends BaseActivity {
    private static final String TAG = "XzxqActivity";
    private static final String URL_XZ = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_medicalStaff.html";
    private static final String URL_AM = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_massager.html";
    private static final String URL_MY = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_motherBaby.html";

    private WebView webview;
    private String userId;
    private int id;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);
        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        Log.i(TAG, "onCreate: " + userId);
        Log.i(TAG, "传给H5的类型: " + type);
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabasePath(XzxqActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this), "qrylhg");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + id + "," + userId + ")");
            }
        });
        if (type == 2) {
            webview.loadUrl(URL_XZ);
        } else if (type == 3) {
            webview.loadUrl(URL_AM);
        } else if (type == 4) {
            webview.loadUrl(URL_MY);
        }
        // webview.loadUrl(URL_XZ);

    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}

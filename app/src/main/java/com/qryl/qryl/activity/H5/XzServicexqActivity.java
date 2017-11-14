package com.qryl.qryl.activity.H5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.HgxqAndroidToJs;
import com.qryl.qryl.view.ProgressWebview;

public class XzServicexqActivity extends BaseActivity {
    private static final String TAG = "XzxqActivity";
    private static final String URL = ConstantValue.URL_H5 + "/patient/serve_priority_serve_details.html";
    private ProgressWebview webview;
    private String userId;
    private int id;
    private int type;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);
        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        userId = prefs.getString("user_id", "");
        Log.i(TAG, "onCreate: " + userId);
        Log.i(TAG, "onCreate: Token:"+token);
        initView();
    }

    private void initView() {
        webview = (ProgressWebview) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDatabasePath(XzServicexqActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this, this), "qrylhg");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + type + "," + id + "," + userId + "," + "'" + token + "'" + ")");
            }
        });
        webview.loadUrl(URL);
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.destroy();
            webview = null;
        }
    }

}

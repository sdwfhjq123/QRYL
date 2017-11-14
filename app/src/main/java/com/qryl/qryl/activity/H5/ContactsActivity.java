package com.qryl.qryl.activity.H5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.HgxqAndroidToJs;
import com.qryl.qryl.view.ProgressWebview;

import static android.view.KeyEvent.KEYCODE_BACK;

public class ContactsActivity extends BaseActivity {

    private static final String TAG = "ContactsActivity";
    private static final String URL = ConstantValue.URL_H5 + "/patient/manage_contact.html";
    private ProgressWebview webview;
    private SharedPreferences prefs;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        Log.i(TAG, "onCreate: 截获的id " + userId);
        initView();
    }

    private void initView() {
        webview = (ProgressWebview) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);// 为WebView使能JavaScript

        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(ContactsActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this, this), "qrylhg");
        webSettings.setAppCacheEnabled(false);
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + userId + ")");
            }
        });
        webview.setWebChromeClient(new WebChromeClient());
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

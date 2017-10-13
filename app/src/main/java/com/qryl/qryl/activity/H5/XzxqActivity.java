package com.qryl.qryl.activity.H5;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.util.HgxqAndroidToJs;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by yinhao on 2017/9/28.
 */

public class XzxqActivity extends BaseActivity {

    private int id;
    private int type;
    private WebView webview;
    private static final String URL = "http://192.168.2.187/patient/serve_priority_serve_details.html";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_contacts);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new HgxqAndroidToJs(this), "qrylService");
        webview.setWebViewClient(new WebViewClient() {
             @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + type+","+id + ")");
            }
        });
        webview.loadUrl(URL);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

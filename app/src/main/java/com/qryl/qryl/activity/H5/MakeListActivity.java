package com.qryl.qryl.activity.H5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.BaseActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.HgxqAndroidToJs;

public class MakeListActivity extends BaseActivity {

    private static final String TAG = "MakeListActivity";
    private static final String URL = ConstantValue.URL_H5 + "/medical/make_list_details.html";
    private WebView webview;
    private SharedPreferences prefs;
    private String userId;
    private int orderId;

    /**
     * @param context
     * @param orderId 订单id
     */
    public static void actionStart(Context context, int orderId) {
        Intent intent = new Intent(context, MakeListActivity.class);
        intent.putExtra("order_id", orderId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        Log.i(TAG, "onCreate: 截获的id " + userId);
        orderId = getIntent().getIntExtra("order_id", 0);
        initView();
    }

    private void initView() {
        webview = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setJavaScriptEnabled(true);// 为WebView使能JavaScript
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setDatabasePath(MakeListActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this), "qrylhg");
        webSettings.setAppCacheEnabled(false);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + orderId + "," + userId + ")");
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
        webview.destroy();
    }
}

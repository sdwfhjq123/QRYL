package com.qryl.qryl.activity.H5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
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

public class HgxqActivity extends BaseActivity {
    private static final String TAG = "HgxqActivity";
    private static final String URL = ConstantValue.URL_H5 + "/patient/carer_details.html";
    private ProgressWebview webview;
    /**
     * 病患端登录的id
     */
    private String userId;
    /**
     * 人员列表的id
     */
    private int id;
    /**
     * 医护端用户登录的id
     */
    private int loginId;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        loginId = intent.getIntExtra("login_id", 0);
        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        token = prefs.getString("token", "");
        Log.i(TAG, "用户的id:" + userId + ",点击的护士的id:" + id + ",医护端登录的id" + loginId);
        initView();
    }

    private void initView() {
        webview = (ProgressWebview) findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDatabasePath(HgxqActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this, this), "qrylhg");
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //医护端登录id 列表id 病患端登录id
                webview.loadUrl("javascript:getId(" + loginId + "," + id + "," + userId + "," + "'" + token + "'" + ")");
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

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

public class XzxqActivity extends BaseActivity {
    private static final String TAG = "XzxqActivity";
    private static final String URL_XZ = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_medicalStaff.html";
    private static final String URL_AM = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_massager.html";
    private static final String URL_MY = ConstantValue.URL_H5 + "/patient/worker_priority_worker_datails_motherBaby.html";

    private ProgressWebview webview;
    /**
     * 病患端用户登录的id
     */
    private String userId;
    /**
     * 医护端人员的id，用于生成订单
     */
    private int id;
    /**
     * 类型
     */
    private int type;
    /**
     * 人员列表表的id，用于显示详情
     */
    private int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        type = intent.getIntExtra("type", 0);
        listId = intent.getIntExtra("list_id", 0);
        SharedPreferences prefs = getSharedPreferences("user_id", Context.MODE_PRIVATE);
        userId = prefs.getString("user_id", "");
        Log.i(TAG, "传给H5的类型: " + type);
        Log.i(TAG, "医护端用户id: " + id);
        Log.i(TAG, "列表的id: " + listId);
        Log.i(TAG, "病患端用户id " + userId);
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
        webSettings.setBlockNetworkImage(true);

        webSettings.setDatabasePath(XzxqActivity.this.getApplicationContext().getCacheDir().getAbsolutePath());
        webview.addJavascriptInterface(new HgxqAndroidToJs(this, this), "qrylhg");
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                webview.loadUrl("javascript:getId(" + id + "," + listId + "," + userId + ")");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webview != null) {
            webview.destroy();
            webview = null;
        }
    }
}

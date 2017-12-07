package com.qryl.qryl.fragment.one;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qryl.qryl.R;
import com.qryl.qryl.activity.H5.HgxqActivity;
import com.qryl.qryl.util.ConstantValue;
import com.qryl.qryl.util.UIUtils;

/**
 * Created by hp on 2017/8/16.
 */

public class MsgFragment extends android.support.v4.app.Fragment {

    private static final String URL = ConstantValue.URL_H5 + "/patient/myNews.html";

    private WebView mWebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = UIUtils.inflate(R.layout.fragment_msg);
        mWebView = (WebView) view.findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                mWebView.loadUrl(URL);
            }
        });
        mWebView.loadUrl(URL);
        return view;
    }
}

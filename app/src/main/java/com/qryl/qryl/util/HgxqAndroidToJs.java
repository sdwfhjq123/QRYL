package com.qryl.qryl.util;

import android.app.Activity;
import android.webkit.JavascriptInterface;


/**
 * Created by yinhao on 2017/9/26.
 */

public class HgxqAndroidToJs {

    private Activity activity;

    public HgxqAndroidToJs(Activity activity) {
        this.activity = activity;
    }

    /**
     * 根布局点击返回销毁页面
     */
    @JavascriptInterface
    public void finishActivity() {
        this.activity.finish();
    }
}

package com.qryl.qryl.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yinhao on 2017/9/21.
 */

public class HttpUtil {

    /**
     * 根据不同的参数获取不同的服务列表
     */
    public static void handleDataFromService(String params, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("id", params);
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(ConstantValue.URL+"/services/getServiceById")
                .build();
        client.newCall(request).enqueue(callback);
    }
}

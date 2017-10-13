package com.qryl.qryl.util;

import com.google.gson.Gson;
import com.qryl.qryl.VO.ServiceVO.ItemList;

/**
 * Created by yinhao on 2017/9/21.
 */

public class HandleJsonUtil {
    /**
     * 解析json类
     */
    public static void handleJsonOfService(String json) {
        Gson gson = new Gson();
        ItemList itemList = gson.fromJson(json, ItemList.class);

    }
}

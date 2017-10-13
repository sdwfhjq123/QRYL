package com.qryl.qryl.VO.XzVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/22.
 */

public class XzData {
    private int total;
    private List<XzInfo> data;

    public XzData(int total, List<XzInfo> data) {
        this.total = total;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<XzInfo> getData() {
        return data;
    }

    public void setData(List<XzInfo> data) {
        this.data = data;
    }
}

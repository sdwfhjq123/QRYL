package com.qryl.qryl.VO.HgPersonVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/19.
 */

public class Hg {

    private Data data;
    private String resultCode;

    public Hg(String resultCode, int total, Data data) {
        this.resultCode = resultCode;
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

package com.qryl.qryl.VO.HgPersonVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/19.
 */

public class Hg {
    private List<Data> data;
    private String resultCode;

    public Hg(List<Data> data, String resultCode) {
        this.data = data;
        this.resultCode = resultCode;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}

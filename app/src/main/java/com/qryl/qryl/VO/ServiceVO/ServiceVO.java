package com.qryl.qryl.VO.ServiceVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/21.
 */

public class ServiceVO {
    private List<Data> data;
    private String resultCode;

    public ServiceVO(List<Data> data, String resultCode) {
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

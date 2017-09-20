package com.qryl.qryl.VO.HgPersonVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/19.
 */

public class Data {
    private List<DataArea> data;
    private int total;

    public Data(List<DataArea> data, int total) {
        this.data = data;
        this.total = total;
    }

    public List<DataArea> getData() {
        return data;
    }

    public void setData(List<DataArea> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

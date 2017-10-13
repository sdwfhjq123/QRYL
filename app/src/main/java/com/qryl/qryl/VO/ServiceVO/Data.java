package com.qryl.qryl.VO.ServiceVO;


import java.util.List;

/**
 * Created by yinhao on 2017/9/21.
 */

public class Data {

    private int id;
    private String name;
    private List<ItemList> itemList;

    public Data(int id, String name, List<ItemList> itemList) {
        this.id = id;
        this.name = name;
        this.itemList = itemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ItemList> getItemList() {
        return itemList;
    }

    public void setItemList(List<ItemList> itemList) {
        this.itemList = itemList;
    }
}

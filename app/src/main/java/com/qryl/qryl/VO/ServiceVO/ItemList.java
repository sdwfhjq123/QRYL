package com.qryl.qryl.VO.ServiceVO;

/**
 * Created by yinhao on 2017/9/21.
 */

public class ItemList {

    private int id;
    private String name;
    private String headshotImg;
    private String abstracts;
    private String content;

    public ItemList(int id, String name, String headshotImg, String abstracts, String content) {
        this.id = id;
        this.name = name;
        this.headshotImg = headshotImg;
        this.abstracts = abstracts;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getHeadshotImg() {
        return headshotImg;
    }

    public void setHeadshotImg(String headshotImg) {
        this.headshotImg = headshotImg;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }
}

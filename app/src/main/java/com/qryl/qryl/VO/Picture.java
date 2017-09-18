package com.qryl.qryl.VO;

/**
 * Created by hp on 2017/9/18.
 * 轮播图片的实体类
 */

public class Picture {
    private int id;
    private String imgUrl;
    private int positionId;
    private String httpUrl;
    private String note;

    public Picture(int id, String imgUrl, int positionId, String httpUrl, String note) {
        this.id = id;
        this.imgUrl = imgUrl;
        this.positionId = positionId;
        this.httpUrl = httpUrl;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

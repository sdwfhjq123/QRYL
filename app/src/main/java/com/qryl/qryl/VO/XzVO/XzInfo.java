package com.qryl.qryl.VO.XzVO;

/**
 * Created by yinhao on 2017/9/22.
 */

public class XzInfo {
    private int id;
    private String realName;
    private int workYears;
    private String headShotImg;
    private String professionIds;
    private int status;

    public XzInfo(int id, String realName, int workYears, String headShotImg, String professionIds, int status) {
        this.id = id;
        this.realName = realName;
        this.workYears = workYears;
        this.headShotImg = headShotImg;
        this.professionIds = professionIds;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getWorkYears() {
        return workYears;
    }

    public void setWorkYears(int workYears) {
        this.workYears = workYears;
    }

    public String getHeadShotImg() {
        return headShotImg;
    }

    public void setHeadShotImg(String headShotImg) {
        this.headShotImg = headShotImg;
    }

    public String getProfessionIds() {
        return professionIds;
    }

    public void setProfessionIds(String professionIds) {
        this.professionIds = professionIds;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

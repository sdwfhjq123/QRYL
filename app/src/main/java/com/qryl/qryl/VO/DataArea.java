package com.qryl.qryl.VO;

/**
 * Created by hp on 2017/9/20.
 */

public class DataArea {
    private int id;
    private int loginId;
    private String realName;
    private int gender;
    private int age;
    private int workYears;
    private String headshotImg;

    public String getHeadshotImg() {
        return headshotImg;
    }

    public void setHeadshotImg(String headshotImg) {
        this.headshotImg = headshotImg;
    }

    //未使用到的

    public DataArea(int id, int loginId, String realName, int gender, int age, int workYears, String headshotImg) {
        this.id = id;
        this.loginId = loginId;
        this.realName = realName;
        this.gender = gender;
        this.age = age;
        this.workYears = workYears;
        this.headshotImg = headshotImg;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
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

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWorkYears() {
        return workYears;
    }

    public void setWorkYears(int workYears) {
        this.workYears = workYears;
    }
}

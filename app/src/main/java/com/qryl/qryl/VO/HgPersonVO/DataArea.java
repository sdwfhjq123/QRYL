package com.qryl.qryl.VO.HgPersonVO;

/**
 * Created by hp on 2017/9/20.
 */

public class DataArea {
    private int id;
    private String realName;
    private int gender;
    private int age;
    private int workYears;

    public DataArea(int id, String realName, int gender, int age, int workYears) {
        this.id = id;
        this.realName = realName;
        this.gender = gender;
        this.age = age;
        this.workYears = workYears;
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

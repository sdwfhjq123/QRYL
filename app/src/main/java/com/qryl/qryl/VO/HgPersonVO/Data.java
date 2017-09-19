package com.qryl.qryl.VO.HgPersonVO;

/**
 * Created by yinhao on 2017/9/19.
 */

public class Data {
    private int id;
    private String realName;
    private int age;
    private int workYears;

    public Data(int id, String realName, int age, int workYears) {
        this.id = id;
        this.realName = realName;
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

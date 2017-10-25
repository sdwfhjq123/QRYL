package com.qryl.qryl.VO.XzVO;

import java.util.List;

/**
 * Created by yinhao on 2017/9/22.
 */

public class XzInfo {
    private int id;
    private int loginId;
    private String realName;
    private int workYears;
    private String headShotImg;
    private String professionIds;
    private int status;
    private int roleType;
    private String hospitalName;
    private String departmentName;
    private String professionNames;

    public XzInfo(int id, int loginId, String realName, int workYears, String headShotImg, String professionIds, int status, int roleType, String hospitalName, String departmentName, String professionNames) {
        this.id = id;
        this.loginId = loginId;
        this.realName = realName;
        this.workYears = workYears;
        this.headShotImg = headShotImg;
        this.professionIds = professionIds;
        this.status = status;
        this.roleType = roleType;
        this.hospitalName = hospitalName;
        this.departmentName = departmentName;
        this.professionNames = professionNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    public String getProfessionNames() {
        return professionNames;
    }

    public void setProfessionNames(String professionNames) {
        this.professionNames = professionNames;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

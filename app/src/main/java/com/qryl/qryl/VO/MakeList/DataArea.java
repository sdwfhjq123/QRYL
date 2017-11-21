package com.qryl.qryl.VO.MakeList;

import java.util.List;

/**
 * Created by hp on 2017/10/30.
 */

public class DataArea {
    private int id;
    private Double price;
    private Patient patient;
    private DoctorNurse doctorNurse;
    private Long createTime;
    private int payed;

    public int getId() {
        return id;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public DoctorNurse getDoctorNurse() {
        return doctorNurse;
    }

    public void setDoctorNurse(DoctorNurse doctorNurse) {
        this.doctorNurse = doctorNurse;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}

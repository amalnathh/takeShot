package com.deftech.takeshot.model;

import java.io.Serializable;

public class VaccCheckerModel implements Serializable {
    int districtId;
    int age;
    String fee;

    public VaccCheckerModel(int districtId, int age, String fee) {
        this.districtId = districtId;
        this.age = age;
        this.fee = fee;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFee() {
        return fee;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public int getDistrictId() {
        return districtId;
    }
}

package com.deftech.takeshot.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    String name;
    String state;
    String district;
    String lattitude;
    String longitude;
    int userCode;
    int age;

    String districtId;
    String dose;
    long userId;
    ArrayList<CentersIndividual> selectedCentersArray;

    public User(String name,
                long userId,
                String lattitude,
                String longitude,
                String district,
                String state,
                int age,
                String dose,
                String districtId,
                int userCode,
                ArrayList<CentersIndividual> selectedCentersArray) {
        this.district = district;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.name = name;
        this.dose = dose;
        this.userId=userId;
        this.state=state;
        this.userCode = userCode;
        this.age = age;
        this.districtId = districtId;
        this.selectedCentersArray =selectedCentersArray;
    }

    public ArrayList<CentersIndividual> getSelectedCentersArray() {
        return selectedCentersArray;
    }

    public void setSelectedCentersArray(ArrayList<CentersIndividual> selectedCentersArray) {
        this.selectedCentersArray = selectedCentersArray;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDose() {
        return dose;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public int getAge() {
        return age;
    }



    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public String getLattitude() {
        return lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getState() {
        return state;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
}

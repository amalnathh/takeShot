package com.deftech.takeshot.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Centers implements Serializable {
    String center_name;
    String block_name;
    int pincode;
    String fee_type;
    String address;
    String district_name;
    ArrayList<Sessions> sessions;

    public  Centers(Object name, Object block_name, Object pincode, Object fee_type, Object address, Object district_name, ArrayList<Sessions> sessions) {
        this.center_name = (String) name;
        this.block_name = (String) block_name;
        this.pincode = (int) pincode;
        this.fee_type = (String) fee_type;
        this.address = (String) address;
        this.district_name = (String) district_name;
        this.sessions = sessions;
    }
    public String getAddress() {
        return address;
    }

    public String getBlock_name() {
        return block_name;
    }

    public String getCenter_name() {
        return center_name;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public String getFee_type() {
        return fee_type;
    }


    public int getPincode() {
        return pincode;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public void setCenter_name(String center_name) {
        this.center_name = center_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public ArrayList<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Sessions> sessions) {
        this.sessions = sessions;
    }
}

package com.deftech.takeshot.model;


import java.io.Serializable;
import java.util.ArrayList;

public class CentersIndividual implements Serializable {
    int center_id;
    String name;
    String address;
    String state_name;
    String district_name;
    String block_name;
    String lat;
    String lon;
    String fee;
    boolean isSelected;
    ArrayList<Sessions> sessions;

    public CentersIndividual(Object center_id, Object name, Object address, Object state_name, Object district_name, Object block_name, Object lat, Object lon) {
        this.center_id = (int) center_id;
        this.address = (String) address;
        this.block_name = (String) block_name;
        this.district_name = (String) district_name;
        this.name = (String) name;
        this.lat = (String) lat;
        this.lon = (String) lon;
        this.state_name = (String) state_name;
        this.fee = "fee";
        this.sessions = new ArrayList<>();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CentersIndividual(Object center_id, Object name, Object address, Object state_name, Object district_name, Object block_name, Object lat, Object lon, Object sessions, Object fee) {
        this.center_id = (int) center_id;
        this.address = (String) address;
        this.block_name = (String) block_name;
        this.district_name = (String) district_name;
        this.name = (String) name;
        this.lat = (String) lat;
        this.lon = (String) lon;
        this.state_name = (String) state_name;
        this.fee = (String) fee;
        this.sessions = (ArrayList<Sessions>) sessions;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public ArrayList<Sessions> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Sessions> sessions) {
        this.sessions = sessions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getCenter_id() {
        return center_id;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLat() {
        return lat;
    }

    public String getState_name() {
        return state_name;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getLon() {
        return lon;
    }

}

package com.deftech.takeshot.model;

import java.io.Serializable;

public class Districts implements Serializable {

    private String district_id;
    private String district_name;

    public Districts(Object district_id, Object district_name) {
        this.district_id = district_id.toString();
        this.district_name = district_name.toString();
    }

    public String getId() {
        return district_id;
    }

    public void setId(String id) {
        this.district_id = district_id;
    }

    public String getName() {
        return district_name;
    }

    public void setName(String name) {
        this.district_name = district_name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return district_name;
    }

}

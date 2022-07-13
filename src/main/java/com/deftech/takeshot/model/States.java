package com.deftech.takeshot.model;

import java.io.Serializable;

public class States implements Serializable {
    private String state_id;
    private String state_name;

    public States(Object state_id, Object state_name) {
        this.state_id = state_id.toString();
        this.state_name=state_name.toString();
    }


    public String getId() {
        return state_id;
    }

    public void setId(String id) {
        this.state_id = state_id;
    }

    public String getName() {
        return state_name;
    }

    public void setName(String name) {
        this.state_name = state_name;
    }


    //to display object as a string in spinner
    @Override
    public String toString() {
        return state_name;
    }


}

package com.deftech.takeshot.model;

import org.json.JSONArray;

import java.io.Serializable;

public class Sessions implements Serializable {
    private String session_id;
    private String date;
    private int available_capacity_dose2;
    private int available_capacity_dose1;
    private int available_capacity;
    private int min_age;
    private String vaccine;
    private JSONArray slots;

    public Sessions(Object session_id, Object date, Object available_capacity, Object min_age,Object vaccine, JSONArray slots) {
        this.session_id = (String) session_id;
        this.date = (String) date;
        this.available_capacity = (int) available_capacity;
        this.min_age = (int) min_age;
        this.slots =  slots;
        this.vaccine = (String) vaccine;
    }

    public void setSlots(JSONArray slots) {
        this.slots = slots;
    }

    public int getAvailable_capacity_dose2() {
        return available_capacity_dose2;
    }

    public int getAvailable_capacity_dose1() {
        return available_capacity_dose1;
    }

    public void setAvailable_capacity_dose2(int available_capacity_dose2) {
        this.available_capacity_dose2 = available_capacity_dose2;
    }

    public void setAvailable_capacity_dose1(int available_capacity_dose1) {
        this.available_capacity_dose1 = available_capacity_dose1;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public void setAvailable_capacity(int available_capacity) {
        this.available_capacity = available_capacity;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public JSONArray getSlots() {
        return slots;
    }

    public int getMin_age() {
        return min_age;
    }

    public int getAvailable_capacity() {
        return available_capacity;
    }

    public String getDate() {
        return date;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}

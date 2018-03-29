package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 13-12-2017.
 */

public class SelectAvailabilityModel {

    private String ah_day_id, day_name, ah_lawyer_availability_id,start_time,end_time;

    public String getAh_day_id() {
        return ah_day_id;
    }

    public void setAh_day_id(String ah_day_id) {
        this.ah_day_id = ah_day_id;
    }

    public String getDay_name() {
        return day_name;
    }

    public void setDay_name(String day_name) {
        this.day_name = day_name;
    }

    public String getAh_lawyer_availability_id() {
        return ah_lawyer_availability_id;
    }

    public void setAh_lawyer_availability_id(String ah_lawyer_availability_id) {
        this.ah_lawyer_availability_id = ah_lawyer_availability_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

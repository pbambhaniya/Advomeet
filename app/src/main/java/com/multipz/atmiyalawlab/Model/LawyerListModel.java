package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 11-01-2018.
 */

public class LawyerListModel {
    String ah_users_id,full_name,experienceinyear,experienceinmonth,profile_img,city_name,avg_rating,total_rating_count,total_case_assign,is_available;
    int case_request_id,case_id,case_request_status_id,ah_lawyer_id;

    public int getAh_lawyer_id() {
        return ah_lawyer_id;
    }

    public void setAh_lawyer_id(int ah_lawyer_id) {
        this.ah_lawyer_id = ah_lawyer_id;
    }

    public int getCase_request_id() {
        return case_request_id;
    }

    public void setCase_request_id(int case_request_id) {
        this.case_request_id = case_request_id;
    }

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public int getCase_request_status_id() {
        return case_request_status_id;
    }

    public void setCase_request_status_id(int case_request_status_id) {
        this.case_request_status_id = case_request_status_id;
    }

    public String getAh_users_id() {
        return ah_users_id;
    }

    public void setAh_users_id(String ah_users_id) {
        this.ah_users_id = ah_users_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getExperienceinyear() {
        return experienceinyear;
    }

    public void setExperienceinyear(String experienceinyear) {
        this.experienceinyear = experienceinyear;
    }

    public String getExperienceinmonth() {
        return experienceinmonth;
    }

    public void setExperienceinmonth(String experienceinmonth) {
        this.experienceinmonth = experienceinmonth;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(String avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getTotal_rating_count() {
        return total_rating_count;
    }

    public void setTotal_rating_count(String total_rating_count) {
        this.total_rating_count = total_rating_count;
    }

    public String getTotal_case_assign() {
        return total_case_assign;
    }

    public void setTotal_case_assign(String total_case_assign) {
        this.total_case_assign = total_case_assign;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }
}

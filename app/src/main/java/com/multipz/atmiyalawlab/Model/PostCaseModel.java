package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 11-12-2017.
 */

public class PostCaseModel {
    String full_name, profile_img, year_of_experience, laywertype,lawyerid;
    private int idstatus, ah_case_post_user_id;

    public String getLawyerid() {
        return lawyerid;
    }

    public void setLawyerid(String lawyerid) {
        this.lawyerid = lawyerid;
    }

    public int getIdstatus() {
        return idstatus;
    }

    public void setIdstatus(int idstatus) {
        this.idstatus = idstatus;
    }

    public int getAh_case_post_user_id() {
        return ah_case_post_user_id;
    }

    public void setAh_case_post_user_id(int ah_case_post_user_id) {
        this.ah_case_post_user_id = ah_case_post_user_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getYear_of_experience() {
        return year_of_experience;
    }

    public void setYear_of_experience(String year_of_experience) {
        this.year_of_experience = year_of_experience;
    }

    public String getLaywertype() {
        return laywertype;
    }

    public void setLaywertype(String laywertype) {
        this.laywertype = laywertype;
    }
}

package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 08-12-2017.
 */

public class SearchAdvocateModel {

    String ah_users_id, full_name, year_of_experience, ah_lawyer_bookmark_id, profile_img, ah_lawyer_specialist_id, city_name, title, avg_rating, total_rating_count, total_case_assign, is_available, is_bookmark;
    boolean ischeck;

    public SearchAdvocateModel(String kk, String criminal, String s, boolean b) {

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

    public String getIs_bookmark() {
        return is_bookmark;
    }

    public void setIs_bookmark(String is_bookmark) {
        this.is_bookmark = is_bookmark;
    }

    public String getAh_lawyer_bookmark_id() {
        return ah_lawyer_bookmark_id;
    }

    public void setAh_lawyer_bookmark_id(String ah_lawyer_bookmark_id) {
        this.ah_lawyer_bookmark_id = ah_lawyer_bookmark_id;
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

    public String getYear_of_experience() {
        return year_of_experience;
    }

    public void setYear_of_experience(String year_of_experience) {
        this.year_of_experience = year_of_experience;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getAh_lawyer_specialist_id() {
        return ah_lawyer_specialist_id;
    }

    public void setAh_lawyer_specialist_id(String ah_lawyer_specialist_id) {
        this.ah_lawyer_specialist_id = ah_lawyer_specialist_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /*  public SearchAdvocateModel(String lawyer_name, String lawyer_type, String img, boolean ischeck) {
        this.lawyer_name = lawyer_name;
        this.lawyer_type = lawyer_type;
        this.img = img;
        this.ischeck = ischeck;
    }*/


    public boolean isIscheck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
}

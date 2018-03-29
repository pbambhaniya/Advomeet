package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 01-12-2017.
 */

public class FeedBackModel {

    int feedback_id, ah_user_id;
    String full_name, profile_img, retting, message, feedback_on;

    public int getFeedback_id() {
        return feedback_id;
    }

    public void setFeedback_id(int feedback_id) {
        this.feedback_id = feedback_id;
    }

    public int getAh_user_id() {
        return ah_user_id;
    }

    public void setAh_user_id(int ah_user_id) {
        this.ah_user_id = ah_user_id;
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

    public String getRetting() {
        return retting;
    }

    public void setRetting(String retting) {
        this.retting = retting;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFeedback_on() {
        return feedback_on;
    }

    public void setFeedback_on(String feedback_on) {
        this.feedback_on = feedback_on;
    }
}

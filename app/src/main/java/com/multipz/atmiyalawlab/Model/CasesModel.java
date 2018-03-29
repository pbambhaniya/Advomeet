package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 23-12-2017.
 */

public class CasesModel {
    String city_name, title, description, case_type, case_status, cased_by_name, profile_img, created_on;
    int case_request_id, case_id, city_id, case_type_id, status_id, cased_by_user_id;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCase_type() {
        return case_type;
    }

    public void setCase_type(String case_type) {
        this.case_type = case_type;
    }

    public String getCase_status() {
        return case_status;
    }

    public void setCase_status(String case_status) {
        this.case_status = case_status;
    }

    public String getCased_by_name() {
        return cased_by_name;
    }

    public void setCased_by_name(String cased_by_name) {
        this.cased_by_name = cased_by_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
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

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getCase_type_id() {
        return case_type_id;
    }

    public void setCase_type_id(int case_type_id) {
        this.case_type_id = case_type_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getCased_by_user_id() {
        return cased_by_user_id;
    }

    public void setCased_by_user_id(int cased_by_user_id) {
        this.cased_by_user_id = cased_by_user_id;
    }
}

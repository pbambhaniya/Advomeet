package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 12-01-2018.
 */

public class ModelCourt {
    private int ah_court_id = 0;
    private String court_name;
    private Boolean is_selected = false;

    public ModelCourt(int ah_court_id, String court_name, Boolean is_selected) {
        this.ah_court_id = ah_court_id;
        this.court_name = court_name;
        this.is_selected = is_selected;
    }

    public ModelCourt(int ah_court_id, String court_name) {
        this.ah_court_id = ah_court_id;
        this.court_name = court_name;

    }

    public ModelCourt() {

    }

    public int getAh_court_id() {
        return ah_court_id;
    }

    public void setAh_court_id(int ah_court_id) {
        this.ah_court_id = ah_court_id;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }
}

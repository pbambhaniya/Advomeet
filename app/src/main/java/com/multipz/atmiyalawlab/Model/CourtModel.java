package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 18-01-2018.
 */

public class CourtModel {

    boolean is_selected;
    String ah_lawyer_court_id,ah_court_id,court_name;

    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getAh_lawyer_court_id() {
        return ah_lawyer_court_id;
    }

    public void setAh_lawyer_court_id(String ah_lawyer_court_id) {
        this.ah_lawyer_court_id = ah_lawyer_court_id;
    }

    public String getAh_court_id() {
        return ah_court_id;
    }

    public void setAh_court_id(String ah_court_id) {
        this.ah_court_id = ah_court_id;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }
}

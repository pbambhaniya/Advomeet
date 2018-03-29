package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 12-01-2018.
 */

public class ModelSpecialization {
    private String ah_lawyer_type_id, lawyer_type;
    private Boolean is_selected = false;

    public ModelSpecialization(String ah_lawyer_type_id, String lawyer_type) {
        this.ah_lawyer_type_id = ah_lawyer_type_id;
        this.lawyer_type = lawyer_type;
    }

    public ModelSpecialization() {

    }

    public String getAh_lawyer_type_id() {
        return ah_lawyer_type_id;
    }

    public void setAh_lawyer_type_id(String ah_lawyer_type_id) {
        this.ah_lawyer_type_id = ah_lawyer_type_id;
    }

    public String getLawyer_type() {
        return lawyer_type;
    }

    public void setLawyer_type(String lawyer_type) {
        this.lawyer_type = lawyer_type;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(Boolean is_selected) {
        this.is_selected = is_selected;
    }
}

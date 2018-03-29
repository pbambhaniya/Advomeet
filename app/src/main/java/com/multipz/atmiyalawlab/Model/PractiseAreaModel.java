package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 18-01-2018.
 */

public class PractiseAreaModel {
    boolean is_selected;

    public boolean isIs_selected() {
        return is_selected;
    }



    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getAh_lawyer_specialist_id() {
        return ah_lawyer_specialist_id;
    }

    public void setAh_lawyer_specialist_id(String ah_lawyer_specialist_id) {
        this.ah_lawyer_specialist_id = ah_lawyer_specialist_id;
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

    String ah_lawyer_specialist_id,ah_lawyer_type_id,lawyer_type;


}

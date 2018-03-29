package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 18-12-2017.
 */

public class LanguageModel {

    boolean is_selected;

    public boolean isIs_selected() {
        return is_selected;
    }



    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    public String getAh_lawyer_language_id() {
        return ah_lawyer_language_id;
    }

    public void setAh_lawyer_language_id(String ah_lawyer_language_id) {
        this.ah_lawyer_language_id = ah_lawyer_language_id;
    }

    public String getAh_language_id() {
        return ah_language_id;
    }

    public void setAh_language_id(String ah_language_id) {
        this.ah_language_id = ah_language_id;
    }

    public String getLanguage_name() {
        return language_name;
    }

    public void setLanguage_name(String language_name) {
        this.language_name = language_name;
    }

    String ah_lawyer_language_id, ah_language_id, language_name;


}

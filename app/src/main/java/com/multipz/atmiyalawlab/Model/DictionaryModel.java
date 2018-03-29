package com.multipz.atmiyalawlab.Model;

import java.io.Serializable;

/**
 * Created by Admin on 10-01-2018.
 */

public class DictionaryModel implements Serializable {
    String ah_dictionary_id;
    String title;
    String description;

    public String getAh_dictionary_id() {
        return ah_dictionary_id;
    }

    public void setAh_dictionary_id(String ah_dictionary_id) {
        this.ah_dictionary_id = ah_dictionary_id;
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




}

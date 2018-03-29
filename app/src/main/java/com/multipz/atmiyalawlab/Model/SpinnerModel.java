package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 31-07-2017.
 */

public class SpinnerModel {

    String id,name;

    public SpinnerModel() {
    }


    public SpinnerModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setAll(String title) {
        setName(title);
    }
}

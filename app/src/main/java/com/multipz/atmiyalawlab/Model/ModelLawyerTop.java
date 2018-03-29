package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 05-01-2018.
 */

public class ModelLawyerTop {
    String img, text;

    public ModelLawyerTop(String img, String text) {
        this.img = img;
        this.text = text;

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

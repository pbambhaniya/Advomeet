package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 30-11-2017.
 */

public class ChatModel {

    String img, text;

    public ChatModel(String img, String text) {
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

package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 22-02-2018.
 */

public class CommunicationModel {
String communication_id;
    String communication_type_id;
    String communication_type;
    String price;
    String price_type_id;
    String price_type;
    String op_expression;
    String is_multiply_diff;

    public String getCommunication_id() {
        return communication_id;
    }

    public void setCommunication_id(String communication_id) {
        this.communication_id = communication_id;
    }

    public String getCommunication_type_id() {
        return communication_type_id;
    }

    public void setCommunication_type_id(String communication_type_id) {
        this.communication_type_id = communication_type_id;
    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_type_id() {
        return price_type_id;
    }

    public void setPrice_type_id(String price_type_id) {
        this.price_type_id = price_type_id;
    }

    public String getPrice_type() {
        return price_type;
    }

    public void setPrice_type(String price_type) {
        this.price_type = price_type;
    }

    public String getOp_expression() {
        return op_expression;
    }

    public void setOp_expression(String op_expression) {
        this.op_expression = op_expression;
    }

    public String getIs_multiply_diff() {
        return is_multiply_diff;
    }

    public void setIs_multiply_diff(String is_multiply_diff) {
        this.is_multiply_diff = is_multiply_diff;
    }


    public boolean isIs_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }

    boolean is_selected;

}

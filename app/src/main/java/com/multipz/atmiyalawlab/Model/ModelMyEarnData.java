package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 10-01-2018.
 */

public class ModelMyEarnData {
    private String communication_type, full_name, profile_img, amount, comm_date, start_time, end_time, updatedon;
    private int communication_id, communication_type_id, ah_users_id;

    public ModelMyEarnData() {

    }

    public String getCommunication_type() {
        return communication_type;
    }

    public void setCommunication_type(String communication_type) {
        this.communication_type = communication_type;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComm_date() {
        return comm_date;
    }

    public void setComm_date(String comm_date) {
        this.comm_date = comm_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getUpdatedon() {
        return updatedon;
    }

    public void setUpdatedon(String updatedon) {
        this.updatedon = updatedon;
    }

    public int getCommunication_id() {
        return communication_id;
    }

    public void setCommunication_id(int communication_id) {
        this.communication_id = communication_id;
    }

    public int getCommunication_type_id() {
        return communication_type_id;
    }

    public void setCommunication_type_id(int communication_type_id) {
        this.communication_type_id = communication_type_id;
    }

    public int getAh_users_id() {
        return ah_users_id;
    }

    public void setAh_users_id(int ah_users_id) {
        this.ah_users_id = ah_users_id;
    }
}

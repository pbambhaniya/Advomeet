package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 16-02-2018.
 */

public class CallListModel {

    String communication_id;
    String communication_type_id;
    String communication_type;
    String comm_date;
    String requested_user_id;
    String lawyer_id;

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

    public String getComm_date() {
        return comm_date;
    }

    public void setComm_date(String comm_date) {
        this.comm_date = comm_date;
    }

    public String getRequested_user_id() {
        return requested_user_id;
    }

    public void setRequested_user_id(String requested_user_id) {
        this.requested_user_id = requested_user_id;
    }

    public String getLawyer_id() {
        return lawyer_id;
    }

    public void setLawyer_id(String lawyer_id) {
        this.lawyer_id = lawyer_id;
    }

    public String getLawyer_name() {
        return lawyer_name;
    }

    public void setLawyer_name(String lawyer_name) {
        this.lawyer_name = lawyer_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOthercharges() {
        return othercharges;
    }

    public void setOthercharges(String othercharges) {
        this.othercharges = othercharges;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    String lawyer_name;
    String profile_img;
    String start_time;
    String end_time;
    String amount;
    String othercharges;
    String created_date;
    String status;
    String payment_id;
    String payment_status;

}

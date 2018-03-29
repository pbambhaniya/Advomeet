package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 28-02-2018.
 */

public class SubscriptionPlanModel {

    private int ah_subscription_id, ah_subscription_type_id, validity;
    private String subscription_type, subscription_plan, subscription_details, price;

    public int getAh_subscription_id() {
        return ah_subscription_id;
    }

    public void setAh_subscription_id(int ah_subscription_id) {
        this.ah_subscription_id = ah_subscription_id;
    }

    public int getAh_subscription_type_id() {
        return ah_subscription_type_id;
    }

    public void setAh_subscription_type_id(int ah_subscription_type_id) {
        this.ah_subscription_type_id = ah_subscription_type_id;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }

    public String getSubscription_type() {
        return subscription_type;
    }

    public void setSubscription_type(String subscription_type) {
        this.subscription_type = subscription_type;
    }

    public String getSubscription_plan() {
        return subscription_plan;
    }

    public void setSubscription_plan(String subscription_plan) {
        this.subscription_plan = subscription_plan;
    }

    public String getSubscription_details() {
        return subscription_details;
    }

    public void setSubscription_details(String subscription_details) {
        this.subscription_details = subscription_details;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

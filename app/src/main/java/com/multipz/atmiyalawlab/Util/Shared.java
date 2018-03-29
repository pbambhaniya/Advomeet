package com.multipz.atmiyalawlab.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Shared {
    SharedPreferences pref;
    Editor edit;

    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String USERTYPE = "usertype";
    private static final String USER_ID = "ah_users_id";
    private static final String ListQualification = "list";
    private static final String ListCourt = "courtlist";
    private static final String ListSpecialization = "courtSpecialization";
    private static final String mobileno = "mobileno";

    private static final String USER_NAME = "username";
    private static final String USER_EMAIL = "email";
    private static final String communication_id = "communication_id";
    private static final String communication_type_id = "communication_type_id";
    private static final String mobile_no = "mobile_no";
    private static final int appointment_id = 0;
    private static final int ah_subscription_id = 0;
    private static final String subscription_price = "subscription_price";
    private static final String IS_SUBSCIRIBE = "IsSubscribe";
    private static final String Answercall = "Answercall";

    public Shared(Context c) {
        // TODO Auto-generated constructor stub

        pref = c.getSharedPreferences("file_pref", Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    /******************************************************************************************/

    public String getAnswercall() {

        String userID = pref.getString(Answercall, "");
        return userID;
    }

    public void setAnswercall(String uid) {
        edit.putString(Answercall, uid);
        edit.commit();
    }


    public int getAh_subscription_id() {

        int userID = pref.getInt("ah_subscription_id", 0);
        return userID;
    }

    public void setAh_subscription_id(int sid) {
        edit.putInt("ah_subscription_id", sid);
        edit.commit();
    }

    public String getSubscription_price() {

        String price = pref.getString(subscription_price, "");
        return price;
    }

    public void setSubscription_price(String sid) {
        edit.putString(subscription_price, sid);
        edit.commit();
    }

    /******************************************************************************************/

    public String getUserId() {

        String userID = pref.getString(USER_ID, "");
        return userID;
    }

    public void setUserId(String uid) {
        edit.putString(USER_ID, uid);
        edit.commit();
    }

    public String getMobileno() {

        String mno = pref.getString(mobile_no, "");
        return mno;
    }

    public void setMobileno(String mobno) {
        edit.putString(mobile_no, mobno);
        edit.commit();
    }

    public int getAppointment_id() {

        int userID = pref.getInt("appointment_id", 0);
        return userID;
    }

    public void setAppointment_id(int aid) {
        edit.putInt("appointment_id", aid);
        edit.commit();
    }


    public String getUsertype() {

        String usertype = pref.getString(USERTYPE, "");
        return usertype;
    }


    public void setCommunication_id(String cid) {
        edit.putString(communication_id, cid);
        edit.commit();
    }


    public String getCommunication_id() {

        String comid = pref.getString(communication_id, "");
        return comid;
    }

    public void setCommunication_type_id(int cid) {
        edit.putInt(communication_type_id, cid);
        edit.commit();
    }


    public int getCommunication_type_id() {
        int comid = pref.getInt(communication_type_id, 0);
        return comid;
    }

    public void setUsertype(String utype) {
        edit.putString(USERTYPE, utype);
        edit.commit();
    }


    public String getUserName() {

        String username = pref.getString(USER_NAME, "");
        return username;
    }

    public void setUserName(String uname) {
        edit.putString(USER_NAME, uname);
        edit.commit();
    }

    public String getUserEmail() {

        String email = pref.getString(USER_EMAIL, "");
        return email;
    }

    public void setUserEmail(String email) {
        edit.putString(USER_EMAIL, email);
        edit.commit();
    }


    public String getListQualification() {

        String qlist = pref.getString(ListQualification, "");
        return qlist;
    }

    public void setListQualification(String list) {
        edit.putString(ListQualification, list);
        edit.commit();
    }

    public String getListCourt() {

        String qlist = pref.getString(ListCourt, "");
        return qlist;
    }

    public void setListCourt(String list) {
        edit.putString(ListCourt, list);
        edit.commit();
    }


    public String getListSpecialization() {

        String qlist = pref.getString(ListSpecialization, "");
        return qlist;
    }

    public void setListSpecialization(String list) {
        edit.putString(ListSpecialization, list);
        edit.commit();
    }


    public void putBoolean(String key, boolean b) {
        edit.putBoolean(key, b);
        edit.commit();
    }

    public boolean getBoolean(String key, boolean b) {
        return pref.getBoolean(key, b);
    }

    public void putString(String key, String def) {
        edit.putString(key, def);
        edit.commit();
    }

    public String getString(String key, String def) {
        return pref.getString(key, def);
    }

    public void putInt(String key, int def) {
        edit.putInt(key, def);
        edit.commit();
    }


    public int getInt(String key) {
        return pref.getInt(key, 0);
    }

    /****************************************Set Login********************************************/


    public void setlogin(boolean login) {

        edit.putBoolean(IS_LOGIN, login);
        // commit changes
        edit.commit();
    }


    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public boolean isSubsciption() {
        return pref.getBoolean(IS_SUBSCIRIBE, false);
    }

    public void setIsSubsciribe(boolean subsciribe) {

        edit.putBoolean(IS_SUBSCIRIBE, subsciribe);
        // commit changes
        edit.commit();
    }

    public void logout() {
        edit.clear();
        edit.commit();
    }

    /****************************************End Login********************************************/


}

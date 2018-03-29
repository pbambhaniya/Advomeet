package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 11-01-2018.
 */

public class ModelAddQualification {
    private String university = "", passingyear = "", graduationid = "", degreeid = "";
    private String uni = "", pyaer = "";

    public ModelAddQualification(String university, String passing_year, String graduate_id, String degree_id) {
        this.university = university;
        this.passingyear = passing_year;
        this.graduationid = graduate_id;
        this.degreeid = degree_id;
    }

   /* public ModelAddQualification(String university, String passing_year, String txtuni, String pyaer, String graduate_id, String degree_id) {
        this.university = university;
        this.passingyear = passing_year;
        this.graduationid = graduate_id;
        this.degreeid = degree_id;
        this.uni = txtuni;
        this.pyaer = pyaer;
    }*/

    public ModelAddQualification() {

    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getPassing_year() {
        return passingyear;
    }

    public void setPassing_year(String passing_year) {
        this.passingyear = passing_year;
    }

    public String getGraduate_id() {
        return graduationid;
    }

    public void setGraduate_id(String graduate_id) {
        this.graduationid = graduate_id;
    }

    public String getDegree_id() {
        return degreeid;
    }

    public void setDegree_id(String degree_id) {
        this.degreeid = degree_id;
    }
}

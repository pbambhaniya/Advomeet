package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 08-01-2018.
 */

public class ModelJudgement {
    private String judgementTitle, jdate;

    public ModelJudgement(String judgementTitle, String jdate) {
        this.judgementTitle = judgementTitle;
        this.jdate = jdate;
    }

    public String getJudgementTitle() {
        return judgementTitle;
    }

    public void setJudgementTitle(String judgementTitle) {
        this.judgementTitle = judgementTitle;
    }

    public String getJdate() {
        return jdate;
    }

    public void setJdate(String jdate) {
        this.jdate = jdate;
    }
}

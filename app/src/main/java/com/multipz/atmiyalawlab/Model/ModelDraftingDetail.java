package com.multipz.atmiyalawlab.Model;

/**
 * Created by Admin on 11-01-2018.
 */

public class ModelDraftingDetail {
    private int drafting_id, ah_drafting_subtype_id;
    private String ah_drafting_subtype, drafting_doc_url;

    public int getDrafting_id() {
        return drafting_id;
    }

    public void setDrafting_id(int drafting_id) {
        this.drafting_id = drafting_id;
    }

    public int getAh_drafting_subtype_id() {
        return ah_drafting_subtype_id;
    }

    public void setAh_drafting_subtype_id(int ah_drafting_subtype_id) {
        this.ah_drafting_subtype_id = ah_drafting_subtype_id;
    }

    public String getAh_drafting_subtype() {
        return ah_drafting_subtype;
    }

    public void setAh_drafting_subtype(String ah_drafting_subtype) {
        this.ah_drafting_subtype = ah_drafting_subtype;
    }

    public String getDrafting_doc_url() {
        return drafting_doc_url;
    }

    public void setDrafting_doc_url(String drafting_doc_url) {
        this.drafting_doc_url = drafting_doc_url;
    }
}

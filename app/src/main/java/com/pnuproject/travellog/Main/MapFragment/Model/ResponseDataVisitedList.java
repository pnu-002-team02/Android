package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDataVisitedList {

    @SerializedName("visitlist")
    @Expose
    private List<String> visitlist = null;

    @SerializedName("retcode")
    private int retcode;

    public List<String> getVisitlist() {
        return visitlist;
    }

    public void setVisitlist(List<String> visitlist) {
        this.visitlist = visitlist;
    }

    public ResponseDataVisitedList withVisitlist(List<String> visitlist) {
        this.visitlist = visitlist;
        return this;
    }

    public int getSuccess() {
        return retcode;
    }

}

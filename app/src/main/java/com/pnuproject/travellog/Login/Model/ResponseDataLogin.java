package com.pnuproject.travellog.Login.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseDataLogin {
    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;

    @SerializedName("username")
    private String username;


    @SerializedName("visitlist")
    private String[] visit_list;


    public String getUsername() {
        return username;
    }

    public String[] getVisitList() {
        return visit_list;
    }
    public String getMessage() {
        return msg;
    }
    public int getSuccess() {
        return retcode;
    }
}

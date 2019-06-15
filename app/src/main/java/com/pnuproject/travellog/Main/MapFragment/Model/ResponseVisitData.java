package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseVisitData {
    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;



    public int getSuccess() {
        return retcode;
    }

    public String getMessage() {
        return msg;
    }

}

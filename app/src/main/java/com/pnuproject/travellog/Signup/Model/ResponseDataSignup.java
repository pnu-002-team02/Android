package com.pnuproject.travellog.Signup.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseDataSignup {
    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;

    public String getMessage() {
        return msg;
    }
    public int getSuccess() {
        return retcode;
    }
}

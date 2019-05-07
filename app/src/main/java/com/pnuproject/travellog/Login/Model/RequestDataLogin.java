package com.pnuproject.travellog.Login.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataLogin implements Serializable {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public RequestDataLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

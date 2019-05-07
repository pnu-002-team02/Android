package com.pnuproject.travellog.Signup.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataSignup implements Serializable {
    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("name")
    private String name;

    @SerializedName("password2")
    private String password2;

    public RequestDataSignup(String name, String email,String password,String password2) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password2 = password2;
    }

    public String getPassword2() {
        return password;
    }
    public String getPassword() {
        return password2;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
}
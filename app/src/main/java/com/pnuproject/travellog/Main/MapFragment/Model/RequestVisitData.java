package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.SerializedName;

public class RequestVisitData {
    @SerializedName("email")
    private String email;

    @SerializedName("add")
    private String add;

    public RequestVisitData(String email,String add){
        this.email = email;
        this.add = add;
    }

    public String getEmail(){return email;}
    public String getAdd(){return add;}

}

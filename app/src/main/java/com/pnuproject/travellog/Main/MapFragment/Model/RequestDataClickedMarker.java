package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestDataClickedMarker implements Serializable {
    @SerializedName("tName")
    private String tName;

    public RequestDataClickedMarker(String tName) {
        this.tName = tName;
    }

    public String getTName() {
        return tName;
    }
}

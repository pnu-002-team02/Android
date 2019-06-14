package com.pnuproject.travellog.Main.MapFragment.Model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products {
    @SerializedName("name")
    @Expose
    private String productName;

    @SerializedName("gps")
    @Expose
    private String gps;

    public Products(String productName, String gps) {
        this.productName = productName;
        this.gps = gps;
    }

    @Override
    public String toString() {
        return productName + ", " + gps + "$";
    }

    public String getProductName() {
        return productName;
    }

    public String getGps() {
        return gps;
    }

    public void setProductName() {
        this.productName = productName;
    }

    public void setGps() {
        this.gps = gps;
    }


}

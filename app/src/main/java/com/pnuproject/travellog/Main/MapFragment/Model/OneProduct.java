package com.pnuproject.travellog.Main.MapFragment.Model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OneProduct {
    @SerializedName("_id")
    private String _id;

    @SerializedName("name")
    @Expose
    private String productName;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("gps")
    @Expose
    private String gps;

    @SerializedName("productImage")
    @Expose
    private String productImage;


    @Override
    public String toString() {
        return productName + "$" + info + "$" + gps + "$" + productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName() {
        this.productName = productName;
    }

    public OneProduct withProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo() {
        this.info = info;
    }

    public OneProduct withInfo(String info) {
        this.info = info;
        return this;
    }

    public String getGps() {
        return gps;
    }

    public void setGps() {
        this.gps = gps;
    }

    public OneProduct withGPs(String gps) {
        this.gps = gps;
        return this;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage() {
        this.productImage = productImage;
    }

    public OneProduct withProductImage(String productImage) {
        this.productImage = productImage;
        return this;
    }

}

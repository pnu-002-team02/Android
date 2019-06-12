package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataMarker {
//    @SerializedName("name")
//    private String productName;
//
//    @SerializedName("info")
//    private String info;
//
//    @SerializedName("gps")
//    private String gps;
//
//    @SerializedName("productImage")
//    private String productImage;

    @SerializedName("products")
    private List<Products> products = null;

    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;

//    public String getProductName() {
//        return productName;
//    }
//    public String getInfo() {
//        return info;
//    }
//    public String getGps() {
//        return gps;
//    }
//    public String getProductImage() {
//        return productImage;
//    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public int getSuccess() {
        return retcode;
    }

    public String getMessage() {
        return msg;
    }

}

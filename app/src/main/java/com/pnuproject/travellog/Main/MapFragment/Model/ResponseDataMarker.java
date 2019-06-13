package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseDataMarker {

    @SerializedName("products")
    private List<Products> products = null;

    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;

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

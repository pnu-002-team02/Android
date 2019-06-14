package com.pnuproject.travellog.Main.MapFragment.Model;

import com.google.gson.annotations.SerializedName;
import com.pnuproject.travellog.Main.MapFragment.Controller.ClickedMarkerDialog;

import java.util.List;

public class ResponseDataClickedMarker {
    @SerializedName("product")
    private OneProduct product;
    //private List<Products> products = null;

    @SerializedName("retcode")
    private int retcode;

    @SerializedName("msg")
    private String msg;


    public OneProduct getOneProduct() {
        return product;
    }

    public void setOneProduct(OneProduct product) {
        this.product = product;
    }

    public ResponseDataClickedMarker withOneProduct(OneProduct product) {
        this.product = product;
        return this;
    }

    public int getSuccess() {
        return retcode;
    }

    public String getMessage() {
        return msg;
    }
}

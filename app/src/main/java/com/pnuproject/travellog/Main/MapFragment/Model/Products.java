package com.pnuproject.travellog.Main.MapFragment.Model;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Products {
    @SerializedName("name")
    @Expose
    private String productName;

//    @SerializedName("info")
//    @Expose
//    private String info;

    @SerializedName("gps")
    @Expose
    private String gps;

//    @SerializedName("productImage")
//    @Expose
//    private String productImage;
//
//    @SerializedName("_id")
//    private String _id;


//    public Products(String productName, String info, String gps, String productImage) {
//        this.productName = productName;
//        this.info = info;
//        this.gps = gps;
//        this.productImage = productImage;
//    }

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
//
//    public String getInfo() {
//        return info;
//    }

    public String getGps() {
        return gps;
    }

//    public String getProductImage() {
//        return productImage;
//    }

    public void setProductName() {
        this.productName = productName;
    }

//    public void setInfo() {
//        this.info = info;
//    }

    public void setGps() {
        this.gps = gps;
    }

//    public void setProductImage() {
//        this.productImage = productImage;
//    }
//

}

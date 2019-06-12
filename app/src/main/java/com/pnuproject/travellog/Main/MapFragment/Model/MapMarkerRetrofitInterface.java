package com.pnuproject.travellog.Main.MapFragment.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MapMarkerRetrofitInterface {
    @GET("/products")
    //Call<List<ResponseDataMarker>> getMarker(@Body RequestDataMarker markerData);
    Call<ResponseDataMarker> getMarker();
}

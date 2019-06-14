package com.pnuproject.travellog.Main.MapFragment.Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MapClickedMarkerRetrofitInterface {
    @POST("/products/findOne")
    Call<ResponseDataClickedMarker> getClickedMarker(@Body RequestDataClickedMarker clickedMarkerData);
}

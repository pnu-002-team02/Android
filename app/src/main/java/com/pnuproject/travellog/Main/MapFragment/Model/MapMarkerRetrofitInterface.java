package com.pnuproject.travellog.Main.MapFragment.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapMarkerRetrofitInterface {
    @GET("/products")
    Call<ResponseDataMarker> getMarker();

    @GET("/register/{email}")
    Call<ResponseDataVisitedList> getVisitedList(@Path("email") String email);
}

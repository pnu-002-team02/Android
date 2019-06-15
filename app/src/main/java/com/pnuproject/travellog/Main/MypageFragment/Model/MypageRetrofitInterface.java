package com.pnuproject.travellog.Main.MypageFragment.Model;

import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataMarker;
import com.pnuproject.travellog.Main.MapFragment.Model.ResponseDataVisitedList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MypageRetrofitInterface {
    @GET("/products")
    Call<ResponseDataMarker> getMarker();

    @GET("/register/{email}")
    Call<ResponseDataVisitedList> getVisitedList(@Path("email") String email);
}

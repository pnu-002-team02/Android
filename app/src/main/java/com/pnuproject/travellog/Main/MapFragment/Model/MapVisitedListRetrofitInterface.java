package com.pnuproject.travellog.Main.MapFragment.Model;

import com.pnuproject.travellog.Signup.Model.ResponseDataCheckID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapVisitedListRetrofitInterface {
    @GET("/register/")
    Call<ResponseDataVisitedList> getVisitedList(@Query("email") String email);
}

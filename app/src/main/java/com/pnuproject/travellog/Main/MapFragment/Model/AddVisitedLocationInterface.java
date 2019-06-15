package com.pnuproject.travellog.Main.MapFragment.Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface AddVisitedLocationInterface {
    @PUT("register/visited")
    Call<ResponseVisitData> putResultData(@Body RequestVisitData param);
}

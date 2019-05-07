package com.pnuproject.travellog.Login.Model;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginRetrofitInterface {
    @POST("/login")
    Call<ResponseDataLogin> doLogin(@Body RequestDataLogin loginData);
}

package com.pnuproject.travellog.Signup.Model;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface SignupRetrofitInterface {
    @GET("/useridconfirm")
    Call<ResponseDataCheckID> checkEmail(@Query("email") String code);

    @POST("/register")
    Call<ResponseDataSignup> doSignup(@Body RequestDataSignup signupData);
}

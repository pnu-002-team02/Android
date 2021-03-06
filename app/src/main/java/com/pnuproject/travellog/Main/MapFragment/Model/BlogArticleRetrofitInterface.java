package com.pnuproject.travellog.Main.MapFragment.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface BlogArticleRetrofitInterface {
    @GET("/v2/search/blog")
    @Headers({"Authorization: KakaoAK cf326dcc411729ebc208b22c43b83e0c"})
    Call<ResponseDataBlog> getBlogData(@Query("query") String query, @Query("size") int size, @Query("page") int page);

}
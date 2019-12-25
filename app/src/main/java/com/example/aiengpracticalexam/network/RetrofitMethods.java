package com.example.aiengpracticalexam.network;

import com.example.aiengpracticalexam.models.ClsHitResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitMethods {

    @GET("search_by_date")
    Call<ClsHitResponse> getHitAPI(@Query("tags") String tags, @Query("page") int pageNo);
}

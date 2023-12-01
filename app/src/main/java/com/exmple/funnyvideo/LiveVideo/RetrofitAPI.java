package com.exmple.funnyvideo.LiveVideo;

import com.exmple.funnyvideo.LiveVideo.Model.RecyclerData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {

    @GET("attitude/random/")
    Call<RecyclerData> getAllCourses(@Query("currentpage") int currentPage);
}


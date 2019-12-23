package com.shahim.themovieapp.api;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface APIInterface {
    @GET("islot/v14/allVideos")
    @Headers({ "Accept: application/json" })
    Call<ArrayList<VideoItem>>getVideoList();
}
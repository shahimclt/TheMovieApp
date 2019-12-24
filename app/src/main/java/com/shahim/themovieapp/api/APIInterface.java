package com.shahim.themovieapp.api;


import com.shahim.themovieapp.api.Pojo.MovieDetail;
import com.shahim.themovieapp.api.Pojo.SearchResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/?apikey=16ec2533")
    @Headers({ "Accept: application/json" })
    Call<SearchResult>searchMovie(@Query("s") String keyword, @Query("page") Integer page);

    @GET("/?apikey=16ec2533&plot=full")
    @Headers({ "Accept: application/json" })
    Call<MovieDetail>getMovieDetails(@Query("i") String imdbId);
}
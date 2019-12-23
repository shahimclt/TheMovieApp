package com.shahim.themovieapp.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shahi on 10-06-2017.
 */

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


            retrofit = new Retrofit.Builder()
                    .baseUrl("http://classicludo.com/apis/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

}

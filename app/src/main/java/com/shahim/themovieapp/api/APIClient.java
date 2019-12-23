package com.shahim.themovieapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.omdbapi.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }

        return retrofit;
    }

}

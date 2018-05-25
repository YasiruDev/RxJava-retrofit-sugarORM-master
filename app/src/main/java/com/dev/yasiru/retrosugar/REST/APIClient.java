package com.dev.yasiru.retrosugar.REST;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shalu on 1/25/2017.
 */
public class APIClient {

    public static String BASE_URL="http://api.themoviedb.org/3/";
    public static Retrofit retrofit=null;

    public static Retrofit getClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}

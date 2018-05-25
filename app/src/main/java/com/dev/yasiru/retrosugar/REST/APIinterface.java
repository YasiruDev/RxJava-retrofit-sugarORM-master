package com.dev.yasiru.retrosugar.REST;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Shalu on 1/25/2017.
 */
public interface APIinterface {

    @GET("movie/top_rated")
    Call<MovieResponce> getTopRatedMovies(@Query("api_key") String apiKey);
}

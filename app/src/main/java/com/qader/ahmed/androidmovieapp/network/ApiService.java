package com.qader.ahmed.androidmovieapp.network;

import com.qader.ahmed.androidmovieapp.model.Movies;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @GET("search/movie?api_key=b3070a5d3abfb7c241d2688d066914e7")
    Observable<Movies> getSearchResult(@Query("query") String query, @Query("page") int page);

}

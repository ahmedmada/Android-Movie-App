package com.qader.ahmed.androidmovieapp.network;

import android.content.Context;

import com.qader.ahmed.androidmovieapp.model.Movies;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetSearchResult {


    Context context;

    public GetSearchResult(Context context){
        this.context = context;
    }

    public Observable<Movies> execute(String query, int page) {
        return getApiService(context).getSearchResult(query,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public ApiService getApiService(Context Context){
        return NetworkClient.getRetrofit(Context)
                .create(ApiService.class);
    }

}

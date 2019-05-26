package com.qader.ahmed.androidmovieapp.presentation.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.qader.ahmed.androidmovieapp.model.Movies;
import com.qader.ahmed.androidmovieapp.network.GetSearchResult;
import com.qader.ahmed.androidmovieapp.util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import rx.Subscriber;

public class MainPresenter implements MainContract.Presenter {

    private static final String TAG = "MainPresenter";

    private MainContract.View view;
    private int page = 1;

    private GetSearchResult getSearchResult;
    private Context context;
    private Set<String> searchKey;

    public MainPresenter(GetSearchResult getSearchResult, Context context) {
        this.getSearchResult = getSearchResult;
        this.context = context;
    }

    @Override
    public void onViewStarted(MainContract.View view) {
        attachedView(view);

        view.showLoading(false);
    }


    @Override
    public void onViewResumed(MainContract.View view) {
        attachedView(view);
    }

    @Override
    public void onSearchQueried(final String query) {
        getSearchResult.execute(query,page).subscribe(new Subscriber<Movies>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: onSearchQueried");
            }

            @Override
            public void onError(Throwable e) {
                defaultErrorHandling(e);
            }

            @Override
            public void onNext(Movies movies) {
                if (!movies.getResults().isEmpty()) {
                    view.showContent(movies.getResults(), true);

                    searchKey = PrefUtils.getSearchKeys(context);

                    if (searchKey == null)
                        searchKey = new HashSet<>();

                    Log.v("mmmmmmmmmmm","query = "+query);
                    Log.v("mmmmmmmmmmm","searchKey = "+searchKey.size());

                    searchKey.add(query);
                    Log.v("mmmmmmmmmmm","new searchKey = "+searchKey.size());
                    PrefUtils.removeAllSharedPreferences(context);
                    PrefUtils.storeSearchKeys(context,searchKey);

                } else {
                    view.showNotFoundError();
                }
            }
        });
    }


    private void attachedView(MainContract.View view) {
        this.view = view;
    }

    private void loadMovies(final boolean isRefresh) {
        getSearchResult.execute(getReleaseDate(), page).subscribe(new Subscriber<Movies>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: loadMovies");
            }

            @Override
            public void onError(Throwable e) {
                view.showError();
                defaultErrorHandling(e);
            }

            @Override
            public void onNext(Movies movies) {
                view.showContent(movies.getResults(), isRefresh);

            }
        });
    }

    @Override
    public void onScrollToBottom() {
        page++;
        view.showLoading(true);
        loadMovies(false);
    }

    @Override
    public void onViewPaused(MainContract.View view) {
        detachView();
    }

    private void detachView() {
        this.view = null;
    }

    private void defaultErrorHandling(Throwable e) {
        Log.e(TAG, Log.getStackTraceString(e));
    }

    @VisibleForTesting
    public String getReleaseDate() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        return format1.format(cal.getTime());
    }

}

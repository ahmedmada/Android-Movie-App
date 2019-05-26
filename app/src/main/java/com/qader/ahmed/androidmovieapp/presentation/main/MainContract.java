package com.qader.ahmed.androidmovieapp.presentation.main;


import com.qader.ahmed.androidmovieapp.model.Result;

import java.util.List;

public interface MainContract {

    interface View {

        void showLoading(boolean isRefresh);

        void showContent(List<Result> movies, boolean isRefresh);

        void showError();

        void showNotFoundError();

    }

    interface Presenter {
        void onViewStarted(View view);

        void onViewPaused(View view);

        void onScrollToBottom();

        void onViewResumed(View view);

        void onSearchQueried(String query);
    }

}

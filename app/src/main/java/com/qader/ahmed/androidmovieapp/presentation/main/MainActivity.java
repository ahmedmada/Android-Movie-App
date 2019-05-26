package com.qader.ahmed.androidmovieapp.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.qader.ahmed.androidmovieapp.R;
import com.qader.ahmed.androidmovieapp.model.Result;
import com.qader.ahmed.androidmovieapp.network.GetSearchResult;
import com.qader.ahmed.androidmovieapp.presentation.detail.DetailActivity;
import com.qader.ahmed.androidmovieapp.util.CheckInternet;
import com.qader.ahmed.androidmovieapp.util.PrefUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends FragmentActivity implements
        MainContract.View,
        SwipeRefreshLayout.OnRefreshListener, EndlessScrollListener.ScrollToBottomListener, MoviesAdapter.ItemClickListener {
    private static final String TAG = "MainActivity";

    MainContract.Presenter presenter;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView contentView;
    @BindView(R.id.textView)
    View errorView;
    @BindView(R.id.textView2)
    View errorNotFound;
    @BindView(R.id.progressBar)
    View loadingView;
    @BindView(R.id.searchViewComponent)
    AppCompatAutoCompleteTextView searchViewComponent;

    private String refreshQuery;

    private MoviesAdapter moviesAdapter;
    private EndlessScrollListener endlessScrollListener;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        presenter = new MainPresenter(new GetSearchResult(this),this);
        presenter.onViewStarted(this);

        setupContentView();
        setupSearchView();
    }

    List<String> allKeys = new ArrayList<>();

    private void setupSearchView() {

        Set<String> getSearch = PrefUtils.getSearchKeys(this);
        if (getSearch != null){

            List<String> keys = new ArrayList<>();
            keys.addAll(getSearch);
            if (keys != null) {
                if (keys.size() > 9)
                    for (int i = 0 ; i < 10 ; i++){
                        allKeys.add(keys.get(i));
                    }
                else
                    allKeys = keys;

                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, android.R.layout.select_dialog_item, allKeys);
                searchViewComponent.setAdapter(adapter);
            }
        }
        searchViewComponent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (searchViewComponent.getText().toString().length() > 3) {
                        refreshQuery = searchViewComponent.getText().toString();
                        presenter.onSearchQueried(searchViewComponent.getText().toString());
                    }else
                        Toast.makeText(MainActivity.this, "Please ,write correct name.", Toast.LENGTH_SHORT).show();
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

        searchViewComponent.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                searchViewComponent.showDropDown();
                return false;
            }
        });


        searchViewComponent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                refreshQuery = allKeys.get(pos);
                presenter.onSearchQueried(allKeys.get(pos));
                closeKeyboard();
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        presenter.onViewStarted(this);
//    }

    private void setupContentView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager, this);
        contentView.setLayoutManager(linearLayoutManager);
        contentView.addOnScrollListener(endlessScrollListener);
    }

    @Override
    public void onRefresh() {
        endlessScrollListener.onRefresh();
        presenter.onSearchQueried(refreshQuery);
        closeKeyboard();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onViewResumed(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewPaused(this);
    }

    @Override
    public void onScrollToBottom() {
        presenter.onScrollToBottom();
    }

    @Override
    public void showLoading(boolean isRefresh) {
        if (isRefresh) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        } else {
//            loadingView.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            errorNotFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void showContent(List<Result> movies, boolean isRefresh) {
        if (moviesAdapter == null) {
            moviesAdapter = new MoviesAdapter(movies, this, this);
            contentView.setAdapter(moviesAdapter);
        } else {
            if (isRefresh) {
                moviesAdapter.clear();
            }
            moviesAdapter.addAll(movies);
            moviesAdapter.notifyDataSetChanged();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1500);

        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        errorNotFound.setVisibility(View.GONE);
    }


    @Override
    public void showError() {
        hiddeContent();
        errorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNotFoundError() {
        hiddeContent();
        errorNotFound.setVisibility(View.VISIBLE);
    }

    private void hiddeContent() {
        swipeRefreshLayout.setRefreshing(false);
        loadingView.setVisibility(View.GONE);
        contentView.setVisibility(View.GONE);
    }

//send data between activites
    @Override
    public void onItemClick(Result movie) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("movie", movie);
        startActivity(i);
    }

    @OnClick(R.id.textView)
    void onClickErrorView() {
        presenter.onViewStarted(this);
    }

    public void searchFilm(View view) {

        if (searchViewComponent.getText().toString().length() > 3) {
            refreshQuery = searchViewComponent.getText().toString();
            presenter.onSearchQueried(searchViewComponent.getText().toString());
        }else
            Toast.makeText(MainActivity.this, "Please ,write correct name.", Toast.LENGTH_SHORT).show();

        closeKeyboard();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

package com.qader.ahmed.androidmovieapp.presentation.detail;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qader.ahmed.androidmovieapp.R;
import com.qader.ahmed.androidmovieapp.model.Result;
import com.qader.ahmed.androidmovieapp.util.StringFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends FragmentActivity {


    @BindView(R.id.container)
    View contentView;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.overviewHeader)
    View overviewHeader;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.genresTextView)
    TextView genresTextView;
    @BindView(R.id.dateTextView)
    TextView dateTextView;
    @BindView(R.id.nameTextView)
    TextView nameTextView;

    private StringFormatter stringFormatter;
    private Result movie;

    public DetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        stringFormatter = new StringFormatter();

        movie = (Result) getIntent().getSerializableExtra("movie");

        setTitle(movie.getTitle());

        initData();
    }
    private String IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    private void initData() {

        dateTextView.setText(stringFormatter.getDate(movie.getReleaseDate()));
        nameTextView.setText(movie.getTitle());
        overviewTextView.setText(stringFormatter.getOverview(movie.getOverview()));

        loadPoster(IMAGE_PATH+movie.getPosterPath());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void loadPoster(String fullImageUrl) {
        Glide.with(this)
                .load(fullImageUrl)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

}

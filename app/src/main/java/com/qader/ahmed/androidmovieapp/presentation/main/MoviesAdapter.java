package com.qader.ahmed.androidmovieapp.presentation.main;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qader.ahmed.androidmovieapp.R;
import com.qader.ahmed.androidmovieapp.model.Result;
import com.qader.ahmed.androidmovieapp.util.StringFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    private List<Result> movies;
    private Activity activity;
    private ItemClickListener itemClickListener;
    private StringFormatter stringFormatter;
    private String IMAGE_PATH = "http://image.tmdb.org/t/p/w500";


    MoviesAdapter(List<Result> movies, Activity activity, ItemClickListener itemClickListener) {
        this.movies = movies;
        this.activity = activity;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        stringFormatter = new StringFormatter();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Result movie = movies.get(position);

            Glide.with(activity)
                    .load(IMAGE_PATH+movie.getPosterPath())
                    .centerCrop()
                    .crossFade()
                    .into(holder.imageView);


        holder.releaseTextView.setText(stringFormatter.getReleaseDate(movie));
        holder.titleTextView.setText(movie.getTitle());
        holder.genreTextView.setText(movie.getOverview());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(movie);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void clear() {
        movies.clear();
    }

    public void addAll(List<Result> movies) {
        this.movies.addAll(movies);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.releaseTextView)
        TextView releaseTextView;
        @BindView(R.id.titleTextView)
        TextView titleTextView;
        @BindView(R.id.genreTextView)
        TextView genreTextView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    interface ItemClickListener {

        void onItemClick(Result result);

    }

}

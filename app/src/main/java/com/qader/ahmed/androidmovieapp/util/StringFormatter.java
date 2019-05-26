package com.qader.ahmed.androidmovieapp.util;

import android.text.TextUtils;

import com.qader.ahmed.androidmovieapp.model.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringFormatter {


    public String getReleaseDate(Result movie) {
        if (movie.getReleaseDate().equals("")) {
            return "...";
        }
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = form.parse(movie.getReleaseDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat postFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        return postFormatter.format(date);
    }

    public String getDate(String releaseDate) {
        return TextUtils.isEmpty(releaseDate) ? "-" : releaseDate;
    }

    public String getOverview(String overview) {
        return TextUtils.isEmpty(overview) ? "-" : overview;
    }

}

package com.qader.ahmed.androidmovieapp.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

public class CheckInternet {

    @SuppressLint("MissingPermission")
    public static boolean isNetwork(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable()
                && connectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}

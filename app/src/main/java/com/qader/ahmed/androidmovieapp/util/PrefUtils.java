package com.qader.ahmed.androidmovieapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class PrefUtils {

    public PrefUtils() {
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }
 
    public static void storeSearchKeys(Context context, Set<String> searchKey) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putStringSet("searchKey", searchKey);
        editor.commit();
    }
 
    public static Set<String> getSearchKeys(Context context) {
        return getSharedPreferences(context).getStringSet("searchKey", null);
    }
    public static void removeAllSharedPreferences(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

}
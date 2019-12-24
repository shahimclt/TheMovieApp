package com.shahim.themovieapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shahim.themovieapp.api.Pojo.Movie;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarksManagerSingleton {

    private BookmarksManagerSingleton() {

    }

    private static BookmarksManagerSingleton _instance;

    public static BookmarksManagerSingleton sharedInstance(Context c) {
        if (_instance==null) {
            _instance = new BookmarksManagerSingleton();
            _instance.loadBookmarks(c);
        }
        return _instance;
    }

    private static final String SP_KEY = "tmabookmarks";

    private ArrayList<Movie> mBookmarks;

    private void loadBookmarks(Context c) {
        mBookmarks = new ArrayList<>();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(SP_KEY, "[]");
        Type type = new TypeToken<List<Movie>>(){}.getType();
        List<Movie> movies = gson.fromJson(json, type);
        mBookmarks.addAll(movies);
    }

    private void saveBookmarks(Context c) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(c);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        List<Movie> movies= mBookmarks;

        String json = gson.toJson(movies);
        prefsEditor.putString(SP_KEY, json);
        prefsEditor.apply();
    }

    public void addBookmark(Context c, Movie m) {
        for (Movie mBookmark : mBookmarks) {
            if(mBookmark.getImdbID().equals(m.getImdbID())) {
                return;
            }
        }
        mBookmarks.add(m);
        saveBookmarks(c);
    }

    public void removeBookmark(Context c, Movie m) {
        removeBookmark(c,m.getImdbID());
    }

    public void removeBookmark(Context c, String id) {
        for (Movie mBookmark : mBookmarks) {
            if(mBookmark.getImdbID().equals(id)) {
                mBookmarks.remove(mBookmark);
                saveBookmarks(c);
                break;
            }
        }
    }

    public ArrayList<Movie> getBookmarks() {
        ArrayList<Movie> arr = new ArrayList<>();
        arr.addAll(mBookmarks);
        return arr;
    }

    public boolean isBookmarked(Context c, Movie m) {
        for (Movie mBookmark : mBookmarks) {
            if(mBookmark.getImdbID().equals(m.getImdbID())) {
                return true;
            }
        }
        return false;
    }

}

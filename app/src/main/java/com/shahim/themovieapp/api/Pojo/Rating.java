package com.shahim.themovieapp.api.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shahim.themovieapp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Rating {

    @StringDef(value={ROTTEN_TOMATOES, IMDB, METACRITIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RatingProvider{}
    public static final String ROTTEN_TOMATOES = "Rotten Tomatoes";
    public static final String IMDB = "Internet Movie Database";
    public static final String METACRITIC = "Metacritic";

    @SerializedName("Source")
    @Expose
    private String source;
    @SerializedName("Value")
    @Expose
    private String value;

    public @RatingProvider String getSource() {
        return source;
    }

    public @DrawableRes int getSourceIcon() {
        switch (source) {
            case ROTTEN_TOMATOES: return R.drawable.ic_rottentomatoes;
            case IMDB: return R.drawable.ic_imdb;
            case METACRITIC: return R.drawable.ic_metacritic;
            default: return R.drawable.ic_star_half_24dp;
        }
    }

    public String getValue() {
        return value;
    }
}

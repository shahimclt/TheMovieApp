package com.shahim.themovieapp.api.Pojo;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.shahim.themovieapp.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OmdbShow {

    @StringDef(value={MOVIE, SERIES, EPISODE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowType{}
    public static final String MOVIE = "movie";
    public static final String SERIES = "series";
    public static final String EPISODE = "episode";

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Year")
    @Expose
    private String year;
    @SerializedName("imdbID")
    @Expose
    private String imdbID;
    @SerializedName("Type")
    @Expose
    private @ShowType String type;
    @SerializedName("Poster")
    @Expose
    private String poster;

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public @ShowType String getType() {
        return type;
    }

    public @DrawableRes int getTypeIcon() {
        switch (type) {
            case MOVIE: return R.drawable.ic_local_movies_24dp;
            case SERIES: return R.drawable.ic_live_tv_24dp;
            default: return R.drawable.ic_local_movies_24dp;
        }
    }

    public String getPoster() {
        return poster;
    }

}

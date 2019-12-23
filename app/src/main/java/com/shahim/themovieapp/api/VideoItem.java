package com.shahim.themovieapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoItem {

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("sources")
    @Expose
    private List<String> sources = null;

    @SerializedName("subtitle")
    @Expose
    private String subtitle;

    @SerializedName("thumb")
    @Expose
    private String thumb;

    @SerializedName("title")
    @Expose
    private String title;

    public String getDescription() {
        return description;
    }

    public List<String> getSources() {
        return sources;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getThumb() {
        return thumb;
    }

    public String getTitle() {
        return title;
    }
}

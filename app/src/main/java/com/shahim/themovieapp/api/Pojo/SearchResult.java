package com.shahim.themovieapp.api.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    @SerializedName("Search")
    @Expose
    private List<OmdbShow> search = null;

    @SerializedName("totalResults")
    @Expose
    private String totalResults;

    @SerializedName("Response")
    @Expose
    private String response;

    @SerializedName("Error")
    @Expose
    private String error;

    public List<OmdbShow> getSearch() {
        return search;
    }

    public Integer getTotalResults() {
        try {
            return Integer.parseInt(totalResults);
        }
        catch (Exception e) {
            return 0;
        }
    }

    public Boolean getResponse() {
        return Boolean.parseBoolean(response);
    }

    public String getError() {
        return error;
    }
}

package com.example.tvshowapp.responses;

import com.example.tvshowapp.models.TVShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//получение  объектов для главной

public class TVShowsResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("pages")
    private int totalPages;

    @SerializedName("tv_shows")
    private List<TVShow> tvShows;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TVShow> getTvShows() {
        return tvShows;
    }
}

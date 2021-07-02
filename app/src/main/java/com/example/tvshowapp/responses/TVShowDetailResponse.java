package com.example.tvshowapp.responses;

import com.example.tvshowapp.models.TVShowsDetails;
import com.google.gson.annotations.SerializedName;


public class TVShowDetailResponse {

    @SerializedName("tvShow")
    private TVShowsDetails tvShowsDetails;

    public TVShowsDetails getTvShowsDetails() {
        return tvShowsDetails;
    }

}

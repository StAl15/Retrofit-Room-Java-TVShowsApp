package com.example.tvshowapp.listeners;

import com.example.tvshowapp.models.TVShow;

public interface WatchlistListener {

    void onTVShowClicked(TVShow tvShow);

    void removeTVShowFromList(TVShow tvShow, int position);

}

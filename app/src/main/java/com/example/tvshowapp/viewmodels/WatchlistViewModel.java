package com.example.tvshowapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.tvshowapp.database.TVShowsDatabase;
import com.example.tvshowapp.models.TVShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {

    private TVShowsDatabase tvShowsDatabase;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);


    }

    public Flowable<List<TVShow>> loadWatchList() {

        return tvShowsDatabase.tvShowDao().getWatchList();
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().deleteFromWatchlist(tvShow);
    }

}

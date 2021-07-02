package com.example.tvshowapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvshowapp.database.TVShowsDatabase;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.repositories.TVShowDetailsRepository;
import com.example.tvshowapp.responses.TVShowDetailResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

//вывод инфы в подрбности
public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public LiveData<TVShowDetailResponse> getTVShowDetails(String tvShowId) {
        return tvShowDetailsRepository.getTVShowsDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().addToWatchlist(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId) {
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow) {
        return tvShowsDatabase.tvShowDao().deleteFromWatchlist(tvShow);
    }

}

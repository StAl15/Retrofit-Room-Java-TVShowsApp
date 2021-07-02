package com.example.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowapp.repositories.MostPopularTVShowsRepositories;
import com.example.tvshowapp.responses.TVShowsResponse;

//вывод программ
public class MostPopularTVShowsViewModel extends ViewModel {

    private MostPopularTVShowsRepositories mostPopularTVShowsRepositories;

    public MostPopularTVShowsViewModel() {
        mostPopularTVShowsRepositories = new MostPopularTVShowsRepositories();

    }

    public LiveData<TVShowsResponse> getMostPopularTVShows(int page) {
        return mostPopularTVShowsRepositories.getMostPopularTVShows(page);
    }


}

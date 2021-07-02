package com.example.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvshowapp.repositories.SearchTVShowRepository;
import com.example.tvshowapp.responses.TVShowsResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;

    public SearchViewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TVShowsResponse> searchTVShow(String query, int page){
        return searchTVShowRepository.searchTVShow(query, page);
    }
}

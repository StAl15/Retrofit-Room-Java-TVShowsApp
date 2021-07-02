package com.example.tvshowapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvshowapp.network.ApiClient;
import com.example.tvshowapp.network.ApiService;
import com.example.tvshowapp.responses.TVShowDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//место хранения подробной инфы
public class TVShowDetailsRepository {

    private ApiService apiService;

    public TVShowDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowDetailResponse> getTVShowsDetails(String tvShowId) {
        MutableLiveData<TVShowDetailResponse> data = new MutableLiveData<>();
        apiService.getTVShowsDetails(tvShowId).enqueue(new Callback<TVShowDetailResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailResponse> call, @NonNull Response<TVShowDetailResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TVShowDetailResponse> call, @NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}

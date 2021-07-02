package com.example.tvshowapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//получение JSON - объектов из api от episodate
public class ApiClient {

    private static Retrofit retrofit;

    public static  Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://www.episodate.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

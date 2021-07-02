package com.example.tvshowapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.tvshowapp.R;
import com.example.tvshowapp.adapters.WatchlistAdapter;
import com.example.tvshowapp.databinding.ActivityWatchListBinding;
import com.example.tvshowapp.listeners.WatchlistListener;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.utilities.TempDataHolder;
import com.example.tvshowapp.viewmodels.WatchlistViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class WatchListActivity extends AppCompatActivity implements WatchlistListener {

    private ActivityWatchListBinding activityWatchListBinding;
    private WatchlistViewModel viewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list);
        doInitialisation();
    }

    private void doInitialisation() {
        viewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchListBinding.imageBack.setOnClickListener(view -> onBackPressed());
        watchlist = new ArrayList<>();
        loadWatchlist();

    }

    private void loadWatchlist() {
        activityWatchListBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.loadWatchList().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchListBinding.setIsLoading(false);
                    if (watchlist.size() > 0) {
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter(watchlist, this);
                    activityWatchListBinding.watchlistRecyclerView.setAdapter(watchlistAdapter);
                    activityWatchListBinding.watchlistRecyclerView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();

                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED){
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }

    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);

    }

    @Override
    public void removeTVShowFromList(TVShow tvShow, int position) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.removeTVShowFromWatchlist(tvShow)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    watchlist.remove(position);
                    watchlistAdapter.notifyItemChanged(position);
                    watchlistAdapter.notifyItemRangeChanged(position, watchlistAdapter.getItemCount());
                    compositeDisposable.dispose();
                }));
    }
}
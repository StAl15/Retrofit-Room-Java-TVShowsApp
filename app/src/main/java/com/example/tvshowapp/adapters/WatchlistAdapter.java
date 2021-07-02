package com.example.tvshowapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowapp.R;
import com.example.tvshowapp.databinding.ItemContainerTvShowBinding;
import com.example.tvshowapp.listeners.WatchlistListener;
import com.example.tvshowapp.models.TVShow;

import java.util.List;


//получени данных и вывод из адаптера
public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowsViewHolder> {

    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding tvShowBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_tv_show, parent, false
        );
        return new TVShowsViewHolder(tvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapter.TVShowsViewHolder holder, int position) {
        holder.bindTVShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    class TVShowsViewHolder extends RecyclerView.ViewHolder {


        private ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowsViewHolder(ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTVShow(TVShow tvShow) {
            itemContainerTvShowBinding.setTvShow(tvShow);
            itemContainerTvShowBinding.executePendingBindings();
            itemContainerTvShowBinding.getRoot().setOnClickListener(v -> watchlistListener.onTVShowClicked(tvShow));
            itemContainerTvShowBinding.imageDelete.setOnClickListener(v -> watchlistListener.removeTVShowFromList(tvShow, getAdapterPosition()));
            itemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }

    }

}

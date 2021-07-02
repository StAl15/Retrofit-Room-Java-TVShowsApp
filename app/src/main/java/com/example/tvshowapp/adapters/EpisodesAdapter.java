package com.example.tvshowapp.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvshowapp.R;
import com.example.tvshowapp.databinding.ItemContainerEpisodeBinding;
import com.example.tvshowapp.models.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<Episode> episodes;
    private LayoutInflater layoutInflater;

    public EpisodesAdapter(List<Episode> episodes) {

        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerEpisodeBinding itemContainerEpisodeBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_episode, parent, false
        );
        return new EpisodeViewHolder(itemContainerEpisodeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        holder.bindEpisode(episodes.get(position));
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private ItemContainerEpisodeBinding itemContainerEpisodeBinding;

        public EpisodeViewHolder(ItemContainerEpisodeBinding itemContainerEpisodeBinding) {
            super(itemContainerEpisodeBinding.getRoot());
            this.itemContainerEpisodeBinding = itemContainerEpisodeBinding;
        }

        public void bindEpisode(Episode episode) {
            String title = "Season ";
            String season = episode.getSeason();
            if (season.length() == 1) {
                season = "0".concat(season);
            }
            String episodenumber = episode.getEpisode();
            if (episodenumber.length() == 1) {
                episodenumber = "0".concat(episodenumber);
            }
            episodenumber = " Episode ".concat(episodenumber);
            title = title.concat(season).concat(episodenumber);
            itemContainerEpisodeBinding.setTitle(title);
            itemContainerEpisodeBinding.setName(episode.getName());
            itemContainerEpisodeBinding.setAirDate(episode.getAirDate());

        }

    }

}

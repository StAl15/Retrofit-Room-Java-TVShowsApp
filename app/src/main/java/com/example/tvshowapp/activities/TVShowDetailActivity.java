package com.example.tvshowapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tvshowapp.R;
import com.example.tvshowapp.adapters.EpisodesAdapter;
import com.example.tvshowapp.adapters.ImageSliderAdapter;
import com.example.tvshowapp.databinding.ActivityTvshowDetailBinding;
import com.example.tvshowapp.databinding.LayoutEpisodesBottomSheetBinding;
import com.example.tvshowapp.models.TVShow;
import com.example.tvshowapp.utilities.TempDataHolder;
import com.example.tvshowapp.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

//подробности сериала
public class TVShowDetailActivity extends AppCompatActivity {

    private ActivityTvshowDetailBinding activityTvshowDetailBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TVShow tvShow;
    private Boolean isTVShowAvailableWatchlist = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTvshowDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_detail);
        doInitialization();

    }

    //инициализация инфы
    private void doInitialization() {
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        activityTvshowDetailBinding.imageBack.setOnClickListener(view -> onBackPressed());
        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTVShowWatchlist();
        getTVShowDetails();

    }

    private void checkTVShowWatchlist() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShow -> {
                    isTVShowAvailableWatchlist = true;
                    activityTvshowDetailBinding.imageWatchlist.setImageResource(R.drawable.ic_aded);
                    compositeDisposable.dispose();
                }));

    }

    //показ всей инфы
    private void getTVShowDetails() {
        activityTvshowDetailBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailResponse -> {
                    activityTvshowDetailBinding.setIsLoading(false);
                    if (tvShowDetailResponse.getTvShowsDetails() != null) {
                        if (tvShowDetailResponse.getTvShowsDetails().getPictures() != null) {
                            loadImagesSlider(tvShowDetailResponse.getTvShowsDetails().getPictures());
                        }
                        activityTvshowDetailBinding.setTvShowImageURL(
                                tvShowDetailResponse.getTvShowsDetails().getImagePath()
                        );
                        activityTvshowDetailBinding.imageTVShow.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.setDescription( //описание из html
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailResponse.getTvShowsDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        activityTvshowDetailBinding.textDescription.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.textReadMore.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.textReadMore.setOnClickListener(view -> {
                            if (activityTvshowDetailBinding.textReadMore.getText().toString().equals("Read More")) {
                                activityTvshowDetailBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                activityTvshowDetailBinding.textDescription.setEllipsize(null);
                                activityTvshowDetailBinding.textReadMore.setText(R.string.read_less);
                            } else {
                                activityTvshowDetailBinding.textDescription.setMaxLines(4);
                                activityTvshowDetailBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                activityTvshowDetailBinding.textReadMore.setText(R.string.read_more);
                            }
                        });
                        activityTvshowDetailBinding.setRating(
                                String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        Double.parseDouble(tvShowDetailResponse.getTvShowsDetails().getRating())
                                )
                        );
                        if (tvShowDetailResponse.getTvShowsDetails().getGenres() != null) {
                            activityTvshowDetailBinding.setGenre(tvShowDetailResponse.getTvShowsDetails().getGenres()[0]);
                        } else {
                            activityTvshowDetailBinding.setGenre("N/A");
                        }
                        activityTvshowDetailBinding.setRuntime(tvShowDetailResponse.getTvShowsDetails().getRuntime() + " Min");
                        activityTvshowDetailBinding.viewDivider1.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.layoutMisc.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.viewDivider2.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailResponse.getTvShowsDetails().getUrl()));
                            startActivity(intent);
                        });
                        activityTvshowDetailBinding.buttonWebsite.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonEpisodes.setVisibility(View.VISIBLE);
                        activityTvshowDetailBinding.buttonEpisodes.setOnClickListener(v -> {
                            if (episodesBottomSheetDialog == null) {
                                episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailActivity.this);
                                layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );
                                episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                                layoutEpisodesBottomSheetBinding.episodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailResponse.getTvShowsDetails().getEpisodes())

                                );
                                layoutEpisodesBottomSheetBinding.textTitle.setText(
                                        String.format("Episodes | %s", tvShow.getName())
                                );
                                layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> {
                                    episodesBottomSheetDialog.dismiss();
                                });

                                //----- Optional section start ------//
                                FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(
                                        com.google.android.material.R.id.design_bottom_sheet
                                );
                                if (frameLayout != null) {
                                    BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                    bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                }
                                //----- Optional section end ------//

                                episodesBottomSheetDialog.show();

                            }
                        });

                        activityTvshowDetailBinding.imageWatchlist.setOnClickListener(v -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if (isTVShowAvailableWatchlist) {
                                compositeDisposable.add(tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailBinding.imageWatchlist.setImageResource(R.drawable.ic_watchlist);
                                            Toast.makeText(getApplicationContext(), "Removed from watchedlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                            } else {
                                compositeDisposable.add(tvShowDetailsViewModel.addToWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailBinding.imageWatchlist.setImageResource(R.drawable.ic_aded);
                                            Toast.makeText(getApplicationContext(), "Added to watchlist", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        })
                                );
                            }
                        });
                        activityTvshowDetailBinding.imageWatchlist.setVisibility(View.VISIBLE);

                        loadBasicTVShowDetails();
                    }
                }
        );
    }

    //загрузка изображений в слайдере
    private void loadImagesSlider(String[] sliderImages) {
        activityTvshowDetailBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(sliderImages));
        activityTvshowDetailBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.viewFadingEdge.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        activityTvshowDetailBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position); //для подсветки текущего изображения
            }
        });

    }

    //для отображения количества картинок в слайдере
    private void setupSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 8, 0); //расположение индикаторов по центру
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.background_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailBinding.layoutSliderIndicators.addView(indicators[i]);
        }
        activityTvshowDetailBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);

    }


    //подсветка текущего изображения из слайдера
    private void setCurrentSliderIndicator(int position) {
        int childCount = activityTvshowDetailBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailBinding.layoutSliderIndicators.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_slider_indicator_inactive)
                );
            }
        }
    }

    private void loadBasicTVShowDetails() {

        activityTvshowDetailBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailBinding.setNetworkCountry(
                tvShow.getNetwork() + " (" +
                        tvShow.getCountry() + ")"
        );
        activityTvshowDetailBinding.setStatus(tvShow.getStatus());
        activityTvshowDetailBinding.setStartedDate(tvShow.getStartDate());


        activityTvshowDetailBinding.textName.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textNetworkCountry.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailBinding.textStarted.setVisibility(View.VISIBLE);


    }
}